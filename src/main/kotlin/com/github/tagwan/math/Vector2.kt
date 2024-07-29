package com.github.tagwan.math


import com.github.tagwan.other.Fix64
import com.github.tagwan.other.toFix64
import java.io.Serializable

data class Vector2(val x: Long, val y: Long) : Serializable {

    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    infix fun distance(other: Vector2): Fix64 {
        val magnitude = this sqrMagnitude other
        return Fix64.sqrt(magnitude.toFix64())
    }

    private infix fun sqrMagnitude(other: Vector2): Long {
        return ((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y))
    }
}

//要按照螺旋顺序 方向
const val Right = 0
const val Down = 1
const val Left = 2
const val Up = 3
const val Depth = 1000

fun Vector2.spiral(find: (x: Long, y: Long) -> Boolean): Vector2? {
    var posX = this@spiral.x
    var posY = this@spiral.y

    var dir = Right
    var len = 1
    var count = 0
    for (round in 0..Depth) {
        for (i in 0 until len) {
            when (dir) {
                Left -> posX--
                Right -> posX++
                Up -> posY--
                Down -> posY++
            }

            if (posX < 0 || posY < 0) {
                continue
            }

            if (find(posX, posY)) {
                return Vector2(posX, posY)
            }
        }
        count++
        if (count == 2) {
            count = 0
            len++
        }
        dir = (dir + 1) % 4
    }

    return null
}