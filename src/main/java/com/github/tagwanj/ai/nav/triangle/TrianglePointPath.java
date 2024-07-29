
package com.github.tagwanj.ai.nav.triangle;

import com.github.tagwanj.ai.pfa.Connection;
import com.github.tagwanj.math.*;
import com.github.tagwanj.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NavMesh 生成坐标路径点
 *
 * @author jsjolund
 */
public class TrianglePointPath implements Iterable<com.github.tagwanj.math.Vector3> {
    public static final com.github.tagwanj.math.Vector3 V3_UP = com.github.tagwanj.math.Vector3.Y;
    public static final com.github.tagwanj.math.Vector3 V3_DOWN = new com.github.tagwanj.math.Vector3(V3_UP).scl(-1);

    /**
     * 在三角形边上的交叉点 <br>
     * A point where an edge on the navmesh is crossed.
     */
    private class EdgePoint {
        /**
         * Triangle which must be crossed to reach the next path point.
         */
        public Triangle toNode;
        /**
         * Triangle which was crossed to reach this point.
         */
        public Triangle fromNode;
        /**
         * Path edges connected to this point. Can be used for spline generation at some
         * point perhaps...
         */
        public List<TriangleEdge> connectingEdges = new ArrayList<TriangleEdge>();
        /**
         * The point where the path crosses an edge.
         */
        public com.github.tagwanj.math.Vector3 point;

        public EdgePoint(com.github.tagwanj.math.Vector3 point, Triangle toNode) {
            this.point = point;
            this.toNode = toNode;
        }
    }

    /**
     * @ 漏斗算法 <br>
     * http://blog.csdn.net/yxriyin/article/details/39207709 <br>
     * Plane funnel for the Simple Stupid Funnel Algorithm
     */
    private class Funnel {

        public final Plane leftPlane = new Plane(); // 左平面，高度为y轴
        public final Plane rightPlane = new Plane();
        public final com.github.tagwanj.math.Vector3 leftPortal = new com.github.tagwanj.math.Vector3(); // 路径左顶点，
        public final com.github.tagwanj.math.Vector3 rightPortal = new com.github.tagwanj.math.Vector3(); // 路径右顶点
        public final com.github.tagwanj.math.Vector3 pivot = new com.github.tagwanj.math.Vector3(); // 漏斗点，路径的起点或拐点

        /**
         * 设置左平面
         *
         * @param pivot
         * @param leftEdgeVertex 边左顶点
         */
        public void setLeftPlane(com.github.tagwanj.math.Vector3 pivot, com.github.tagwanj.math.Vector3 leftEdgeVertex) {
            leftPlane.set(pivot, tmp1.set(pivot).add(V3_UP), leftEdgeVertex);
            leftPortal.set(leftEdgeVertex);
        }

        /**
         * 设置右平面
         *
         * @param pivot
         * @param rightEdgeVertex
         */
        public void setRightPlane(com.github.tagwanj.math.Vector3 pivot, com.github.tagwanj.math.Vector3 rightEdgeVertex) {
            rightPlane.set(pivot, tmp1.set(pivot).add(V3_UP), rightEdgeVertex); // 高度
            rightPlane.normal.scl(-1); // 平面方向取反
            rightPlane.d = -rightPlane.d;
            rightPortal.set(rightEdgeVertex);
        }

        /**
         * 设置平面
         *
         * @param pivot
         * @param edge  边
         */
        public void setPlanes(com.github.tagwanj.math.Vector3 pivot, TriangleEdge edge) {
            setLeftPlane(pivot, edge.leftVertex);
            setRightPlane(pivot, edge.rightVertex);
        }

        /**
         * 测试点在左平面内侧还是外侧（前或后）
         *
         * @param point
         * @return
         */
        public Plane.PlaneSide sideLeftPlane(com.github.tagwanj.math.Vector3 point) {
            return leftPlane.testPoint(point);
        }

        /**
         * 测试点在右平面内侧还是外侧
         *
         * @param point
         * @return
         */
        public Plane.PlaneSide sideRightPlane(com.github.tagwanj.math.Vector3 point) {
            return rightPlane.testPoint(point);
        }
    }

    private final Plane crossingPlane = new Plane(); // 横跨平面
    private final com.github.tagwanj.math.Vector3 tmp1 = new com.github.tagwanj.math.Vector3();
    private final com.github.tagwanj.math.Vector3 tmp2 = new com.github.tagwanj.math.Vector3();
    private List<Connection<Triangle>> nodes; // 路径连接点
    private com.github.tagwanj.math.Vector3 start; // 起点
    private com.github.tagwanj.math.Vector3 end; // 终点
    private Triangle startTri; // 起始三角形
    private EdgePoint lastPointAdded; // 最后一个边点
    private List<com.github.tagwanj.math.Vector3> vectors = new ArrayList<com.github.tagwanj.math.Vector3>(); // 路径坐标点
    private List<EdgePoint> pathPoints = new ArrayList<EdgePoint>();
    private TriangleEdge lastEdge; // 最后一个边

    @Override
    public Iterator<com.github.tagwanj.math.Vector3> iterator() {
        return vectors.iterator();
    }

    private TriangleEdge getEdge(int index) {
        return (TriangleEdge) ((index == nodes.size()) ? lastEdge : nodes.get(index));
    }

    private int numEdges() {
        return nodes.size() + 1;
    }

    /**
     * 计算路径点 <br>
     * Calculate the shortest path through the navigation mesh triangles.
     *
     * @param trianglePath
     * @param calculateCrossPoint true 计算三角形的交叉点，false 只计算拐点
     */
    public void calculateForGraphPath(TriangleGraphPath trianglePath, boolean calculateCrossPoint) {
        clear();
        nodes = trianglePath.nodes;
        this.start = new com.github.tagwanj.math.Vector3(trianglePath.start);
        this.end = new com.github.tagwanj.math.Vector3(trianglePath.end);
        this.startTri = trianglePath.startTri;

        // 矫正开始坐标
        // Check that the start point is actually inside the start triangle, if not,
        // project it to the closest
        // triangle edge. Otherwise the funnel calculation might generate spurious path
        // segments.
        Ray ray = new Ray(tmp1.set(V3_UP).scl(1000).add(start), tmp2.set(V3_DOWN)); // 起始坐标从上向下的射线
        if (!Intersector.intersectRayTriangle(ray, startTri.a, startTri.b, startTri.c, null)) {
            float minDst = Float.POSITIVE_INFINITY;
            com.github.tagwanj.math.Vector3 projection = new com.github.tagwanj.math.Vector3(); // 规划坐标
            com.github.tagwanj.math.Vector3 newStart = new com.github.tagwanj.math.Vector3(); // 新坐标
            float dst;
            // A-B
            if ((dst = GeometryUtil.nearestSegmentPointSquareDistance(projection, startTri.a, startTri.b,
                    start)) < minDst) {
                minDst = dst;
                newStart.set(projection);
            }
            // B-C
            if ((dst = GeometryUtil.nearestSegmentPointSquareDistance(projection, startTri.b, startTri.c,
                    start)) < minDst) {
                minDst = dst;
                newStart.set(projection);
            }
            // C-A
            if ((dst = GeometryUtil.nearestSegmentPointSquareDistance(projection, startTri.c, startTri.a,
                    start)) < minDst) {
                minDst = dst;
                newStart.set(projection);
            }
            start.set(newStart);
        }
        if (nodes.size() == 0) { // 起点终点在同一三角形中
            addPoint(start, startTri);
            addPoint(end, startTri);
        } else {
            lastEdge = new TriangleEdge(nodes.get(nodes.size() - 1).getToNode(), nodes.get(nodes.size() - 1).getToNode(), end,
                    end);
            calculateEdgePoints(calculateCrossPoint);
        }
    }

    /**
     * 清理数据 <br>
     * Clear the stored path data.
     */
    public void clear() {
        vectors.clear();
        pathPoints.clear();
        start = null;
        end = null;
        startTri = null;
        lastPointAdded = null;
        lastEdge = null;
    }

    /**
     * @param index
     * @return
     * @ 获取寻路向量点 A path point which crosses one or more edges in the navigation
     * mesh.
     */
    public com.github.tagwanj.math.Vector3 getVector(int index) {
        return vectors.get(index);
    }

    /**
     * The number of path points.
     *
     * @return
     */
    public int getSize() {
        return vectors.size();
    }

    /**
     * All vectors in the path.
     *
     * @return
     */
    public List<com.github.tagwanj.math.Vector3> getVectors() {
        return vectors;
    }

    /**
     * The triangle which must be crossed to reach the next path point.
     *
     * @param index
     * @return
     */
    public Triangle getToTriangle(int index) {
        return pathPoints.get(index).toNode;
    }

    /**
     * The triangle from which must be crossed to reach this point.
     *
     * @param index
     * @return
     */
    public Triangle getFromTriangle(int index) {
        return pathPoints.get(index).fromNode;
    }

    /**
     * The navmesh edge(s) crossed at this path point.
     *
     * @param index
     * @return
     */
    public List<TriangleEdge> getCrossedEdges(int index) {
        return pathPoints.get(index).connectingEdges;
    }

    /**
     * 添加坐标点
     *
     * @param point
     * @param toNode
     */
    private void addPoint(com.github.tagwanj.math.Vector3 point, Triangle toNode) {
        addPoint(new EdgePoint(point, toNode));
    }

    /**
     * 添加坐标点
     *
     * @param edgePoint
     */
    private void addPoint(EdgePoint edgePoint) {
        vectors.add(edgePoint.point);
        pathPoints.add(edgePoint);
        lastPointAdded = edgePoint;
    }

    /**
     * 根据三角形路径计算最短路径坐标点 <br>
     * http://blog.csdn.net/yxriyin/article/details/39207709 Calculate the shortest
     * point path through the path triangles, using the Simple Stupid Funnel
     * Algorithm.
     *
     * @return
     */
    private void calculateEdgePoints(boolean calculateCrossPoint) {
        TriangleEdge edge = getEdge(0);
        addPoint(start, edge.fromNode);
        lastPointAdded.fromNode = edge.fromNode;

        Funnel funnel = new Funnel();
        funnel.pivot.set(start); // 起点为漏斗点
        funnel.setPlanes(funnel.pivot, edge); // 设置第一对平面

        int leftIndex = 0; // 左顶点索引
        int rightIndex = 0; // 右顶点索引
        int lastRestart = 0;

        for (int i = 1; i < numEdges(); ++i) {
            edge = getEdge(i); // 下一条边

            Plane.PlaneSide leftPlaneLeftDP = funnel.sideLeftPlane(edge.leftVertex);
            Plane.PlaneSide leftPlaneRightDP = funnel.sideLeftPlane(edge.rightVertex);
            Plane.PlaneSide rightPlaneLeftDP = funnel.sideRightPlane(edge.leftVertex);
            Plane.PlaneSide rightPlaneRightDP = funnel.sideRightPlane(edge.rightVertex);

            // 右顶点在右平面里面
            if (rightPlaneRightDP != Plane.PlaneSide.Front) {
                // 右顶点在左平面里面
                if (leftPlaneRightDP != Plane.PlaneSide.Front) {
                    // Tighten the funnel. 缩小漏斗
                    funnel.setRightPlane(funnel.pivot, edge.rightVertex);
                    rightIndex = i;
                } else {
                    // Right over left, insert left to path and restart scan from portal left point.
                    // 右顶点在左平面外面，设置左顶点为漏斗顶点和路径点，从新已该漏斗开始扫描
                    if (calculateCrossPoint) {
                        calculateEdgeCrossings(lastRestart, leftIndex, funnel.pivot, funnel.leftPortal);
                    } else {
                        vectors.add(new com.github.tagwanj.math.Vector3(funnel.leftPortal));
                    }
                    funnel.pivot.set(funnel.leftPortal);
                    i = leftIndex;
                    rightIndex = i;
                    if (i < numEdges() - 1) {
                        lastRestart = i;
                        funnel.setPlanes(funnel.pivot, getEdge(i + 1));
                        continue;
                    }
                    break;
                }
            }
            // 左顶点在左平面里面
            if (leftPlaneLeftDP != Plane.PlaneSide.Front) {
                // 左顶点在右平面里面
                if (rightPlaneLeftDP != Plane.PlaneSide.Front) {
                    // Tighten the funnel.
                    funnel.setLeftPlane(funnel.pivot, edge.leftVertex);
                    leftIndex = i;
                } else {
                    // Left over right, insert right to path and restart scan from portal right
                    // point.
                    if (calculateCrossPoint) {
                        calculateEdgeCrossings(lastRestart, rightIndex, funnel.pivot, funnel.rightPortal);
                    } else {
                        vectors.add(new com.github.tagwanj.math.Vector3(funnel.rightPortal));
                    }
                    funnel.pivot.set(funnel.rightPortal);
                    i = rightIndex;
                    leftIndex = i;
                    if (i < numEdges() - 1) {
                        lastRestart = i;
                        funnel.setPlanes(funnel.pivot, getEdge(i + 1));
                        continue;
                    }
                    break;
                }
            }
        }
        if (calculateCrossPoint) {
            calculateEdgeCrossings(lastRestart, numEdges() - 1, funnel.pivot, end);
        } else {
            vectors.add(new com.github.tagwanj.math.Vector3(end));
        }

        for (int i = 1; i < pathPoints.size(); i++) {
            EdgePoint p = pathPoints.get(i);
            p.fromNode = pathPoints.get(i - 1).toNode;
        }
        return;
    }

    /**
     * 计算存储和边的交叉点<br>
     * Store all edge crossing points between the start and end indices. If the path
     * crosses exactly the start or end points (which is quite likely), store the
     * edges in order of crossing in the EdgePoint data structure.
     * <p/>
     * Edge crossings are calculated as intersections with the plane from the start,
     * end and up vectors.
     *
     * @param startIndex
     * @param endIndex
     * @param startPoint
     * @param endPoint
     */
    private void calculateEdgeCrossings(int startIndex, int endIndex, com.github.tagwanj.math.Vector3 startPoint, com.github.tagwanj.math.Vector3 endPoint) {

        if (startIndex >= numEdges() || endIndex >= numEdges()) {
            return;
        }
        crossingPlane.set(startPoint, tmp1.set(startPoint).add(V3_UP), endPoint);

        EdgePoint previousLast = lastPointAdded;

        TriangleEdge edge = getEdge(endIndex);
        EdgePoint end = new EdgePoint(new com.github.tagwanj.math.Vector3(endPoint), edge.toNode);

        for (int i = startIndex; i < endIndex; i++) {
            edge = getEdge(i);

            if (edge.rightVertex.equals(startPoint) || edge.leftVertex.equals(startPoint)) {
                previousLast.toNode = edge.toNode;
                if (!previousLast.connectingEdges.contains(edge)) {
                    previousLast.connectingEdges.add(edge);
                }

            } else if (edge.leftVertex.equals(endPoint) || edge.rightVertex.equals(endPoint)) {
                if (!end.connectingEdges.contains(edge)) {
                    end.connectingEdges.add(edge);
                }

            } else if (Intersector.intersectSegmentPlane(edge.leftVertex, edge.rightVertex, crossingPlane, tmp1)
                    && !Float.isNaN(tmp1.x + tmp1.y + tmp1.z)) {
                if (i != startIndex || i == 0) {
                    lastPointAdded.toNode = edge.fromNode;
                    EdgePoint crossing = new EdgePoint(new Vector3(tmp1), edge.toNode);
                    crossing.connectingEdges.add(edge);
                    addPoint(crossing);
                }
            }
        }
        if (endIndex < numEdges() - 1) {
            end.connectingEdges.add(getEdge(endIndex));
        }
        if (!lastPointAdded.equals(end)) {
            addPoint(end);
        }
    }

}
