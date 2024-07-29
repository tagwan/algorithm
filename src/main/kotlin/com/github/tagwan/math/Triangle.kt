//package com.github.tagwan.math
//
//import java.io.Serializable
//import java.util.*
//import kotlin.math.abs
//import kotlin.math.max
//import kotlin.math.min
//
//data class Triangle(
//    val a: Vector2,
//    val b: Vector2,
//    val c: Vector2
//) : Serializable {
//
//    /**
//     * 获取三角形内所有点(三角形栅格化)
//     * <p>
//     *  Bresenham算法
//     *  参考链接
//     *  http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html
//     *  https://www.cnblogs.com/ourroad/archive/2013/05/14/3078841.html
//     */
//    fun findAllPointInTriangle(): List<Vector2> {
//
//        // 首先通过y坐标升序对三个顶点进行排序，因此v1是最高顶点
//        val vList = listOf(a, b, c).sortedBy {
//            it.y.combine(it.x)
//        }
//
//        //在这里我们知道v1.y <= v2.y <= v3.y
//        val v1 = vList[0]
//        val v2 = vList[1]
//        val v3 = vList[2]
//
//        val posList = LinkedList<Vector2>()
//
//        when {
//            v2.y == v3.y -> {
//                //平底三角
//                fillFlatTriangle(posList, v1, v2, v3)
//            }
//            v1.y == v2.y -> {
//                //平顶三角
//                fillFlatTriangle(posList, v3, v1, v2)
//            }
//            else -> {
//                //将三角形分成一个顶部平坦和一个底部平坦
//                val v4 = Vector2(v1.x + ((v2.y - v1.y).toFloat() / (v3.y - v1.y) * (v3.x - v1.x)).toInt(), v2.y)
//                fillFlatTriangle(posList, v1, v2, v4)
//                fillFlatTriangle(posList, v3, v2, v4)
//            }
//        }
//
//        return posList
//    }
//
//
//    /**
//     * 填充平底/平顶三角形
//     * <p>
//     *     v2.y == v3.y
//     *
//     * @param posList
//     * @param v1
//     * @param v2
//     * @param v3
//     */
//    fun fillFlatTriangle(posList: LinkedList<Vector2>, v1: Vector2, v2: Vector2, v3: Vector2) {
//        if (v2.y != v3.y) {
//            return
//        }
//
//        var minV = v2
//        var maxV = v3
//        if (v2.x > v3.x) {
//            minV = v3
//            maxV = v2
//        }
//
//        posList.add(v1)
//
//        var left = v1
//        var right = v1
//        for (y in min(v1.y, minV.y)..max(v1.y, minV.y)) {
//            val newleft = stepLineGrid(left, minV)
//            if (newleft == null) {
//                break
//            }
//            val newRight = stepLineGrid(right, maxV)
//            if (newRight == null) {
//                break
//            }
//            left = newleft
//            right = newRight
//
//            for (x in left.x..right.x) {
//                posList.add(Vector2(x, left.y))
//            }
//        }
//    }
//
//    /**
//     * Bresenham算法栅格化，y方向步进
//     *
//     * @param v1
//     * @param v2
//     * @return
//     */
//    fun stepLineGrid(v1: Vector2, v2: Vector2): Vector2? {
//
//        val dx = abs(v1.x - v2.x)
//        val dy = abs(v1.y - v2.y)
//        var x = v1.x
//        var y = v1.y
//        val sx = if (v2.x > v1.x) 1 else -1
//        val sy = if (v2.y > v1.y) 1 else -1
//
//        if (dx > dy) {
//            var e = -dx
//            for (i in 0 until dx) {
//                x += sx
//                e += 2 * dy
//                if (e >= 0) {
//                    y += sy
//                    e -= 2 * dx
//
//                    return Vector2(x, y)
//                }
//            }
//        } else {
//            var e = -dy
//            for (i in 0 until dy) {
//                y += sy
//                e += 2 * dx
//                if (e >= 0) {
//                    x += sx
//                    e -= 2 * dy
//                }
//                return Vector2(x, y)
//            }
//        }
//
//        return null
//    }
//
//    /**
//     * 检测点是否在三角形内
//     *
//     * @param p
//     * @return
//     */
//    infix fun embed(p: Vector2): Boolean {
//        var va = 0
//        var vb = 0
//        var vc = 0
//
//        val ma = Vector2(p.x - a.x, p.y - a.y)
//        val mb = Vector2(p.x - b.x, p.y - b.y)
//        val mc = Vector2(p.x - c.x, p.y - c.y)
//
//        /*向量叉乘*/
//        va = ma.x * mb.y - ma.y * mb.x
//        vb = mb.x * mc.y - mb.y * mc.x
//        vc = mc.x * ma.y - mc.y * ma.x
//
//        return (va <= 0 && vb <= 0 && vc <= 0) || (va > 0 && vb > 0 && vc > 0)
//    }
//}