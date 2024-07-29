package com.github.tagwan.math

import com.github.tagwan.other.Fix64
import com.github.tagwan.other.toFix64


/**
 * unity三维向量 <br></br>
 * 基于unity，部分计算基于2D平面，x，z轴
 *
 * @author jdg
 */
data class Vector3(
    @Volatile
    var x: Fix64,
    @Volatile
    var y: Fix64,
    @Volatile
    var z: Fix64,
) : Vector<Vector3> {


    constructor() : this(Fix64.zero, Fix64.zero, Fix64.zero)

    constructor(degree: Fix64) : this(Fix64.zero, Fix64.zero, Fix64.zero) {
        var degree2 = degree
        degree2 = 90.toFix64() - degree
        // x = Fix64.cos(degreesToRadians * degree2) // TODO
        z = Fix64.sin(degreesToRadians * degree2)
    }

    constructor(x: Fix64, z: Fix64) : this(x, Fix64.zero, z)

    /**
     * 复制点
     *
     * @return
     */
    fun copy(): Vector3 {
        return Vector3(x, y, z)
    }

    /**
     * 平面距离平方
     *
     * @param p
     * @return
     */
    fun dst2(p: Vector3): Fix64 {
        return dst2(x, z, p.x, p.z)
    }

    /**
     * Returns the squared distance between this point and the given point
     *
     * @param x
     * The x-component of the other point
     * @param y
     * The y-component of the other point
     * @param z
     * The z-component of the other point
     * @return The squared distance
     */
    fun dst2(x: Fix64, y: Fix64, z: Fix64): Fix64 {
        val a = x - this.x
        val b = y - this.y
        val c = z - this.z
        return a * a + b * b + c * c
    }

    /**
     * 平面两坐标点距离
     *
     * @param p
     * @return
     */
    fun dst(p: Vector3): Fix64 {
        return dst(x, z, p.x, p.z)
    }

    /** @return the distance between this point and the given point
     */
    fun dst(x: Fix64, y: Fix64, z: Fix64): Fix64 {
        val a = x - this.x
        val b = y - this.y
        val c = z - this.z
        return Fix64.sqrt(a * a + b * b + (c * c))
    }

    /**
     * x轴加一个常量
     *
     * @param x
     */
    fun addX(x: Fix64) {
        this.x += x
    }

    fun addY(y: Fix64) {
        this.y += y
    }

    fun addZ(z: Fix64) {
        this.z += z
    }


    override fun add(vector: Vector3): Vector3 {
        return this.add(vector.x, vector.y, vector.z)
    }

    /**
     * Adds the given vector to this component
     *
     * @param x
     * The x-component of the other vector
     * @param y
     * The y-component of the other vector
     * @param z
     * The z-component of the other vector
     * @return This vector for chaining.
     */
    fun add(x: Fix64, y: Fix64, z: Fix64): Vector3 {
        return this.set(this.x + x, this.y + y, this.z + z)
    }

    /**
     * Sets the vector to the given components
     *
     * @param x
     * The x-component
     * @param y
     * The y-component
     * @param z
     * The z-component
     * @return this vector for chaining
     */
    operator fun set(x: Fix64, y: Fix64, z: Fix64): Vector3 {
        this.x = x
        this.y = y
        this.z = z
        return this
    }

    override fun set(vector: Vector3): Vector3 {
        return this.set(vector.x, vector.y, vector.z)
    }

    override fun scl(scalar: Fix64): Vector3 {
        return this.set(x * scalar, y * scalar, z * scalar)
    }

    override fun scl(other: Vector3): Vector3 {
        return this.set(x * other.x, y * other.y, z * other.z)
    }

    /**
     * Sets this vector to the cross product between it and the other vector. <br></br>
     * 叉乘更多的是判断某个平面的方向。从这个平面上选两个不共线的向量，叉乘的结果就是这个平面的法向量
     *
     * @param vector
     * The other vector
     * @return This vector for chaining
     */
    fun cross(vector: Vector3): Vector3 {
        return this.set(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x)
    }

    /**
     * 叉乘<br></br>
     * Sets this vector to the cross product between it and the other vector.
     *
     * @param x
     * The x-component of the other vector
     * @param y
     * The y-component of the other vector
     * @param z
     * The z-component of the other vector
     * @return This vector for chaining
     */
    fun cross(x: Fix64, y: Fix64, z: Fix64): Vector3 {
        return this.set(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x)
    }


    override fun len2(): Fix64 {
        return x * x + y * y + z * z
    }


    override fun mulAdd(vec: Vector3, scalar: Fix64): Vector3 {
        x += vec.x * scalar
        y += vec.y * scalar
        z += vec.z * scalar
        return this
    }

    override fun lerp(target: Vector3, alpha: Fix64): Vector3 {
        x += alpha * (target.x - x)
        y += alpha * (target.y - y)
        z += alpha * (target.z - z)
        return this
    }

    companion object {

         val TWO_PI = Fix64.pi * 2

        /** 度数转弧度换算单位  */
         val degreesToRadians = Fix64.pi / 180

        /** 零向量  */
        val ZERO = Vector3()

        /**
         * 平面距离平方
         *
         * @param x1
         * @param z1
         * @param x2
         * @param z2
         * @return
         */
        fun dst2(x1: Fix64, z1: Fix64, x2: Fix64, z2: Fix64): Fix64 {
            var x = x1
            var z = z1
            x -= x2
            z -= z2
            return x * x + z * z
        }

        /**
         * 两坐标距离
         *
         * @param vector1
         * @param vector2
         * @return
         */
        fun dst(vector1: Vector3, vector2: Vector3): Fix64 {
            return dst(vector1.x, vector1.z, vector2.x, vector2.z)
        }

        /**
         * * 平面两坐标点距离
         *
         * @param x1
         * @param z1
         * @param x2
         * @param z2
         * @return
         */
        fun dst(x1: Fix64, z1: Fix64, x2: Fix64, z2: Fix64): Fix64 {
            var x = x1
            var z = z1
            x -= x2
            z -= z2
            return Fix64.sqrt(x * x + z * z)
        }
    }

}