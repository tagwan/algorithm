package com.github.tagwan.structure

import com.github.tagwan.math.Vector2
import java.util.*

// Axis-Aligned Bounding Box
data class AABBBox(var id: Long = 0, val minX: Int, val minY: Int, val maxX: Int, val maxY: Int) {

    constructor(minX: Int, minY: Int, maxX: Int, maxY: Int): this(0, minX, minY, maxX, maxY)

    /**
     * AABB的碰撞
     * @param box1 AABB
     * @param box2 AABB
     * @return Boolean
     */
    infix fun intersects(other: AABBBox): Boolean {
        return !(this.maxX <= other.minX || this.minX >= other.maxX || this.maxY <= other.minY || this.minY >= other.maxY)
    }

    /**
     * AABB与线段的碰撞
     * @param line LineSegment
     * @return Boolean
     */
    infix fun intersects(line: LineSegment): Boolean {
        // 检查线段是否与 AABB 的边界相交
        val left = line.p1.x <= this.maxX && line.p2.x >= this.minX
        val right = line.p1.x >= this.minX && line.p2.x <= this.maxX
        val top = line.p1.y <= this.maxY && line.p2.y >= this.minY
        val bottom = line.p1.y >= this.minY && line.p2.y <= this.maxY

        // 如果线段与 AABB 的任意边界相交，表示发生碰撞
        return left || right || top || bottom
    }
}

data class LineSegment(val p1: Vector2, val p2: Vector2)

/**
 * 扫描线算法实现快速查找所所有碰撞的对应关系
 * @param boxes List<AABBBox>
 * @return List<Pair<AABBBox, AABBBox>>
 */
fun findIntersectingRectangles(boxes: List<AABBBox>): List<Pair<AABBBox, AABBBox>> {
    // 按照y1坐标从小到大排序
    val sortedBoxes = boxes.sortedBy { it.minY }

    // 使用一个set记录当前扫描线上的矩形
    val activeRectangles = LinkedList<AABBBox>()
    val intersectingPairs = LinkedList<Pair<AABBBox, AABBBox>>()

    for (rect in sortedBoxes) {
        // 从activeRectangles中移除所有y2小于当前矩形y1的矩形
        activeRectangles.removeIf { it.maxY < rect.minY }

        // 检查当前矩形是否与activeRectangles中的矩形相交或相邻
        for (otherRect in activeRectangles) {
            otherRect.intersects(rect)
            if (otherRect.intersects(rect)) {
                intersectingPairs.add(Pair(rect, otherRect))
            }
        }

        // 将当前矩形添加到activeRectangles中
        activeRectangles.add(rect)
    }

    return intersectingPairs
}

/**
 * 快速统计出所有相交的组
 * @param boxes List<AABBBox>
 * @return List<List<AABBBox>>
 */
fun findIntersectingGroups(boxes: List<AABBBox>): List<List<AABBBox>> {
    val n = boxes.size
    val uf = UnionFind(n)

    for (i in boxes.indices) {
        for (j in i + 1 until n) {
            if (boxes[i].intersects(boxes[j])) {
                uf.union(i, j)
            }
        }
    }

    val groups = HashMap<Int, LinkedList<AABBBox>>()
    for ((i, v) in boxes.withIndex()) {
        val groupIndex = uf.find(i)
        groups.getOrPut(groupIndex) {
            LinkedList()
        }.add(v)
    }
    return groups.values.filter { it.isNotEmpty() }
}