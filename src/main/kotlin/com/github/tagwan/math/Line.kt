package com.github.tagwan.math

import java.io.Serializable
import java.util.*
import kotlin.math.abs

/**
 * 线段
 *
 * @param v1
 * @param v2
 */
data class Line(
    val v1: Vector2,
    val v2: Vector2
): Serializable {


    /**
     * 线段栅格化
     *
     * <p>
     *     Bresenham算法
     *
     * @return
     */
    fun grids(): List<Vector2> {
        val posList = LinkedList<Vector2>()
        posList.add(v1)

        val dx = abs(v1.x - v2.x)
        val dy = abs(v1.y - v2.y)
        var x = v1.x
        var y = v1.y
        val sx = if (v2.x > v1.x) 1 else -1
        val sy = if (v2.y > v1.y) 1 else -1

        if (dx > dy) {
            var e = -dx
            for (i in 0 until dx) {
                x += sx
                e += 2 * dy
                if (e >= 0) {
                    y += sy
                    e -= 2 * dx
                }
                posList.add(Vector2(x, y))
            }
        } else {
            var e = -dy
            for (i in 0 until dy) {
                y += sy
                e += 2 * dx
                if (e >= 0) {
                    x += sx
                    e -= 2 * dy
                }
                posList.add(Vector2(x, y))
            }
        }
        return posList
    }

}