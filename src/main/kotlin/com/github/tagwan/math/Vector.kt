package com.github.tagwan.math

import com.github.tagwan.other.Fix64


/**
 * 向量接口
 *
 * @author jdg
 */
interface Vector<T : Vector<T>> {
    /**
     * Adds the given vector to this vector
     *
     * @param v
     * The vector
     * @return This vector for chaining
     */
    fun add(v: T): T

    /**
     * Scales this vector by a scalar
     *
     * @param scalar
     * The scalar
     * @return This vector for chaining
     */
    fun scl(scalar: Fix64): T

    /**
     * Scales this vector by another vector
     *
     * @return This vector for chaining
     */
    fun scl(v: T): T

    /**
     * Sets this vector from the given vector
     *
     * @param v
     * The vector
     * @return This vector for chaining
     */
    fun set(v: T): T

    /**
     * This method is faster than [Vector.len] because it avoids calculating
     * a square root. It is useful for comparisons, but not for getting exact
     * lengths, as the return value is the square of the actual length.
     *
     * @return The squared euclidean length
     */
    fun len2(): Fix64

    /**
     * First scale a supplied vector, then add it to this vector.
     *
     * @param v
     * addition vector
     * @param scalar
     * for scaling the addition vector
     */
    fun mulAdd(v: T, scalar: Fix64): T

    /**
     * 坐标是否相等
     *
     * @param precision
     * 精度
     * @return
     */
    fun equal(vector3: Vector3?, precision: Fix64): Boolean {
        return false
    }

    /** Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
     * in this vector.
     * @param target The target vector
     * @param alpha The interpolation coefficient
     * @return This vector for chaining.
     */
    fun lerp(target: T, alpha: Fix64): T
}