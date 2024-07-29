package com.github.tagwan.math

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

/**
 * 一个long存两个int
 *
 * @param other other位低32位存储
 * @return
 */
//fun Int.combine(other: Int): Long {
//    return this.toLong().shl(32) or other.toLong()
//}

infix fun Int.combine(other: Int): Long {
    return other.toLong() and 0xFFFFFFFFL or (this.toLong() shl 32 and -0x100000000L)
}

/**
 * 将一个long拆分两个int
 *
 * @return
 */
fun Long.split(): Pair<Int, Int> {
    return Pair(this.shr(32).toInt(), (this and Long.MAX_VALUE).toInt())
}

fun Int.toBinaryLong(): Long {
    if (this >= 0) {
        return this.toLong()
    }

    return (-this).toLong() or 0xFFFFFFFF
}

/**
 * byte[]转int
 * @return Int
 */
fun ByteArray.readInt32(): Int {
    return (this[3].toInt()) and 0xFF or (
            (this[2].toInt()) and 0xFF shl 8) or (
            (this[1].toInt()) and 0xFF shl 16) or (
            (this[0].toInt()) and 0xFF shl 24)
}

/**
 * int转byte[]
 * @return ByteArray
 */
fun Int.readByteArray(): ByteArray {
    return byteArrayOf(
        (this shr 24 and 0xFF).toByte(),
        (this shr 16 and 0xFF).toByte(),
        (this shr 8 and 0xFF).toByte(),
        (this and 0xFF).toByte()
    )
}

/**
 * 二分法取整数平方根
 */
fun Long.sqrt(): Long {
    val v = this@sqrt
    if (v < 0) {
        return 0
    }
    if (v <= 1) {
        return v
    }

    var high = v
    var low = 0L
    while (low + 1 < high) {
        val x = low + (high - low).shr(1)
        if (x > v / x) {
            high = x
        } else {
            low = x
        }
    }
    return low
}

/**
 * To redis rank score
 *
 * @param updateTime
 * @return
 */
fun Long.toRedisRankScore(updateTime: Long): Double {
    return this + 1 - updateTime / 10.0.pow(log10(updateTime.toDouble()).toInt() + 1.toDouble())
}

/**
 * 对入参保留最多两位小数(舍弃末尾的0)，如:
 * 3.345->3.34
 * 3.40->3.4
 * 3.0->3
 */
fun Double.getNoMoreThanTwoDigits(): String {
    val format = DecimalFormat("0.##")
    // 未保留小数的舍弃规则，RoundingMode.FLOOR表示直接舍弃。
    format.roundingMode = RoundingMode.FLOOR
    return format.format(this)
}