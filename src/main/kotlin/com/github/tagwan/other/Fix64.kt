package com.github.tagwan.other


/**
 * 定点数
 * <p>
 *     后12位[Fix64.FRACTIONAL_PLACES]作为小数处理
 *
 * @param rawValue
 * @author jdg
 */
class Fix64 private constructor(
    val rawValue: Long
) : Number(), Comparable<Fix64> {
    companion object {

        const val SIZE_BYTES: Int = 8
        const val SIZE_BITS: Int = 64

        const val PI: Long = 12868L
        const val PiTimes2: Long = 25736L
        const val FRACTIONAL_PLACES: Int = 12
        const val ONE: Long = 1L shl FRACTIONAL_PLACES

        val one: Fix64 = Fix64(ONE)
        val zero: Fix64 = Fix64(0L)
        val pi = Fix64(PI)

        fun fromRaw(rawValue: Long): Fix64 {
            return Fix64(rawValue)
        }

        fun fromRaw(rawValue: Int): Fix64 {
            return Fix64(rawValue.toLong())
        }

        /**
         * Sqrt
         *
         * @param f
         * @param numberIterations
         * @return
         */
        fun sqrt(f: Fix64, numberIterations: Int): Fix64 {
            if (f.rawValue < 0) {
                throw ArithmeticException("sqrt error")
            }

            if (f.rawValue == 0L) return Fix64.zero

            var k: Fix64 = f + one shr 1
            for (i in 0 until numberIterations) k = k + f / k shr 1

            return if (k.rawValue < 0) throw ArithmeticException("Overflow") else k
        }

        fun sqrt(f: Fix64): Fix64 {
            var numberOfIterations: Byte = 8
            if (f.rawValue > 0x64000) numberOfIterations = 12
            if (f.rawValue > 0x3e8000) numberOfIterations = 16
            return sqrt(f, numberOfIterations.toInt())
        }


        fun pow(x: Fix64, y: Int): Fix64 {
            if (y == 1) return x
            var result = zero
            val tmp: Fix64 = pow(x, y / 2)
            result = if (y and 1 != 0) // 奇数
            {
                x * tmp * tmp
            } else {
                tmp * tmp
            }

            return result
        }

        /**
         * Sin
         *
         * @param i
         * @return
         */
        fun sin(i: Fix64): Fix64 {
            var i = i
            var j = 0.toFix64()
            while (i < zero) {
                i += fromRaw(25736)
            }
            if (i > fromRaw(25736)) i %= fromRaw(25736)
            val k: Fix64 = i * fromRaw(10) / fromRaw(714)
            if (i !== zero && i !== fromRaw(6434) && i !== fromRaw(12868) && i !== fromRaw(19302) && i !== Fix64.fromRaw(25736)) j = i * Fix64.fromRaw(100) / Fix64.fromRaw(714) - k * Fix64.fromRaw(10)
            if (k <= fromRaw(90)) return sinLookup(k, j)
            if (k <= fromRaw(180)) return sinLookup(fromRaw(180) - k, j)
            return if (k <= fromRaw(270)) -sinLookup(k - fromRaw(180), j) else -sinLookup(fromRaw(360) - k, j)
        }

        fun sinLookup(i: Fix64, j: Fix64): Fix64 {
            return if (j > 0.toFix64() && j < fromRaw(10) && i < fromRaw(90)) fromRaw(SIN_TABLE[i.rawValue.toInt()]) +
                (fromRaw(SIN_TABLE[(i.rawValue + 1).toInt()]) - fromRaw(SIN_TABLE[i.rawValue.toInt()])) /
                fromRaw(10) * j else fromRaw(SIN_TABLE[i.rawValue.toInt()])
        }

        fun abs(value: Fix64): Fix64 {
            return Fix64(if (value.rawValue > 0) value.rawValue else -value.rawValue)
        }
    }

    //constructor(value: Int) : this(value * ONE)

    /**
     * Plus
     *
     * @param other
     * @return
     */
    operator fun plus(other: Fix64): Fix64 {
        return Fix64(this.rawValue + other.rawValue)
    }

    operator fun plus(other: Long): Fix64 {
        return this + other.toFix64()
    }

    operator fun plus(other: Int): Fix64 {
        return this + other.toFix64()
    }

    operator fun plus(other: Short): Fix64 {
        return this + other.toFix64()
    }

    operator fun plus(other: Float): Fix64 {
        return this + other.toFix64()
    }

    operator fun plus(other: Double): Fix64 {
        return this + other.toFix64()
    }

    operator fun minus(other: Fix64): Fix64 {
        return Fix64(this.rawValue - other.rawValue)
    }

    operator fun minus(other: Long): Fix64 {
        return this - other.toFix64()
    }

    operator fun minus(other: Int): Fix64 {
        return this - other.toFix64()
    }

    operator fun minus(other: Short): Fix64 {
        return this - other.toFix64()
    }

    operator fun minus(other: Float): Fix64 {
        return this - other.toFix64()
    }

    operator fun minus(other: Double): Fix64 {
        return this - other.toFix64()
    }

    operator fun times(other: Fix64): Fix64 {
        return Fix64((this.rawValue * other.rawValue) shr FRACTIONAL_PLACES)
    }

    operator fun times(other: Long): Fix64 {
        return this * other.toFix64()
    }

    operator fun times(other: Int): Fix64 {
        return this * other.toFix64()
    }

    operator fun times(other: Short): Fix64 {
        return this * other.toFix64()
    }

    operator fun times(other: Float): Fix64 {
        return this * other.toFix64()
    }

    operator fun times(other: Double): Fix64 {
        return this * other.toFix64()
    }

    operator fun div(other: Fix64): Fix64 {
        return Fix64((this.rawValue shl FRACTIONAL_PLACES) / other.rawValue)
    }

    operator fun div(other: Long): Fix64 {
        return this / other.toFix64()
    }

    operator fun div(other: Int): Fix64 {
        return this / other.toFix64()
    }

    operator fun div(other: Short): Fix64 {
        return this / other.toFix64()
    }

    operator fun div(other: Float): Fix64 {
        return this / other.toFix64()
    }

    operator fun div(other: Double): Fix64 {
        return this / other.toFix64()
    }

    /**
     * 左移 同Java <<
     *
     * @param amount
     */
    infix fun shl(amount: Int): Fix64 {
        return Fix64(rawValue shl amount)
    }

    /**
     * 右移 同Java >>
     *
     * @param amount
     */
    infix fun shr(amount: Int): Fix64 {
        return Fix64(rawValue shr amount)
    }

    /**
     * 取余 %
     *
     * @param other
     * @return
     */
    operator fun rem(other: Fix64): Fix64 {
        return Fix64(this.rawValue % other.rawValue)
    }

    /**
     * +a
     *
     */
    operator fun unaryPlus(): Fix64 {
        return Fix64(+this.rawValue)
    }

    /**
     * -a
     *
     */
    operator fun unaryMinus(): Fix64 {
        return Fix64(-this.rawValue)
    }


    override fun equals(other: Any?) = when {
        // 使用恒等运算符来判断两个参数是否同一个对象的引用
        other === this -> true // 表示比较对象地址
        other !is Fix64 -> false
        else -> this.rawValue == other.rawValue
    }


    override fun toShort(): Short {
        return this.toLong().toShort()
    }

    /**
     * Converts this [Long] value to [Int].
     *
     * If this value is in [Int.MIN_VALUE]..[Int.MAX_VALUE], the resulting `Int` value represents
     * the same numerical value as this `Long`.
     *
     * The resulting `Int` value is represented by the least significant 32 bits of this `Long` value.
     */
    override fun toInt(): Int {
        return this.toLong().toInt()
    }

    /** Returns this value. */
    override fun toLong(): Long {
        return this.rawValue shr FRACTIONAL_PLACES
    }

    /**
     * Converts this [Long] value to [Float].
     *
     * The resulting value is the closest `Float` to this `Long` value.
     * In case when this `Long` value is exactly between two `Float`s,
     * the one with zero at least significant bit of mantissa is selected.
     */
    override fun toFloat(): Float {
        return this.rawValue.toFloat() / ONE
    }

    override fun toByte(): Byte {
        return this.rawValue.toByte()
    }

    override fun toChar(): Char {
        return this.toFloat().toChar()
    }

    /**
     * Converts this [Long] value to [Double].
     *
     * The resulting value is the closest `Double` to this `Long` value.
     * In case when this `Long` value is exactly between two `Double`s,
     * the one with zero at least significant bit of mantissa is selected.
     */
    override fun toDouble(): Double {
        return this.rawValue.toDouble() / ONE
    }

    override fun compareTo(other: Fix64): Int {
        return this.rawValue.compareTo(other.rawValue)
    }

    override fun hashCode(): Int {
        return rawValue.hashCode()
    }

    override fun toString(): String {
        return this.toFloat().toString()
    }
}

fun Long.toFix64(): Fix64 = Fix64.fromRaw(this * Fix64.ONE)

fun Int.toFix64(): Fix64 = Fix64.fromRaw(this * Fix64.ONE)

fun Short.toFix64(): Fix64 = Fix64.fromRaw(this * Fix64.ONE)

fun Float.toFix64(): Fix64 = Fix64.fromRaw((this * Fix64.ONE).toLong())

fun Double.toFix64(): Fix64 = Fix64.fromRaw((this * Fix64.ONE).toLong())

operator fun Short.times(other: Fix64): Fix64 = other * this
operator fun Short.plus(other: Fix64): Fix64 = other + this
operator fun Short.minus(other: Fix64): Fix64 = other - this
operator fun Short.div(other: Fix64): Fix64 = other / this


operator fun Int.times(other: Fix64): Fix64 = other * this
operator fun Int.plus(other: Fix64): Fix64 = other + this
operator fun Int.minus(other: Fix64): Fix64 = other - this
operator fun Int.div(other: Fix64): Fix64 = other / this

operator fun Long.times(other: Fix64): Fix64 = other * this
operator fun Long.plus(other: Fix64): Fix64 = other + this
operator fun Long.minus(other: Fix64): Fix64 = other - this
operator fun Long.div(other: Fix64): Fix64 = other / this


operator fun Float.times(other: Fix64): Fix64 = other * this
operator fun Float.plus(other: Fix64): Fix64 = other + this
operator fun Float.minus(other: Fix64): Fix64 = other - this
operator fun Float.div(other: Fix64): Fix64 = other / this

operator fun Double.times(other: Fix64): Fix64 = other * this
operator fun Double.plus(other: Fix64): Fix64 = other + this
operator fun Double.minus(other: Fix64): Fix64 = other - this
operator fun Double.div(other: Fix64): Fix64 = other / this

private val SIN_TABLE = intArrayOf(
    0, 71, 142, 214, 285, 357, 428, 499, 570, 641,
    711, 781, 851, 921, 990, 1060, 1128, 1197, 1265, 1333,
    1400, 1468, 1534, 1600, 1665, 1730, 1795, 1859, 1922, 1985,
    2048, 2109, 2170, 2230, 2290, 2349, 2407, 2464, 2521, 2577,
    2632, 2686, 2740, 2793, 2845, 2896, 2946, 2995, 3043, 3091,
    3137, 3183, 3227, 3271, 3313, 3355, 3395, 3434, 3473, 3510,
    3547, 3582, 3616, 3649, 3681, 3712, 3741, 3770, 3797, 3823,
    3849, 3872, 3895, 3917, 3937, 3956, 3974, 3991, 4006, 4020,
    4033, 4045, 4056, 4065, 4073, 4080, 4086, 4090, 4093, 4095,
    4096
)

//fun main() {
//    val zero = Fix64.zero
//    val one = Fix64.one
//    val pi = Fix64.pi
//    val frame = Fix64.fromRaw(273)
//    val pi2 = Long.MAX_VALUE
//    println("zero::${zero}, one::${one}, pi::${pi}, frame::${frame}")
//    println("pi2::${pi2}, pi2::${Fix64.fromRaw(pi2)}, pi::${Fix64.fromRaw(pi2).toLong()}")
//}