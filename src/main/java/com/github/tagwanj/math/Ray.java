package com.github.tagwanj.math;

import java.io.Serializable;

/**
 * 射线<br>
 * Encapsulates a ray having a starting position and a unit length direction.
 * 
 * @author badlogicgames@gmail.com
 */
public class Ray implements Serializable {
	private static final long serialVersionUID = -620692054835390878L;
	public final com.github.tagwanj.math.Vector3 origin = new com.github.tagwanj.math.Vector3(); // 起点
	public final com.github.tagwanj.math.Vector3 direction = new com.github.tagwanj.math.Vector3(); // 方向

	public Ray() {
	}

	/**
	 * Constructor, sets the starting position of the ray and the direction.
	 * 
	 * @param origin
	 *            The starting position
	 * @param direction
	 *            The direction
	 */
	public Ray(com.github.tagwanj.math.Vector3 origin, com.github.tagwanj.math.Vector3 direction) {
		this.origin.set(origin);
		this.direction.set(direction).nor();
	}

	/** @return a copy of this ray. */
	public Ray cpy() {
		return new Ray(this.origin, this.direction);
	}

	/**
	 * Returns the endpoint given the distance. This is calculated as startpoint +
	 * distance * direction.
	 * 
	 * @param out
	 *            The vector to set to the result
	 * @param distance
	 *            The distance from the end point to the start point.
	 * @return The out param
	 */
	public com.github.tagwanj.math.Vector3 getEndPoint(final com.github.tagwanj.math.Vector3 out, final float distance) {
		return out.set(direction).scl(distance).add(origin);
	}

	static com.github.tagwanj.math.Vector3 tmp = new com.github.tagwanj.math.Vector3();

	/**
	 * 射线坐标系改变<br>
	 * Multiplies the ray by the given matrix. Use this to transform a ray into
	 * another coordinate system.
	 * 
	 * @param matrix
	 *            The matrix
	 * @return This ray for chaining.
	 */
	public Ray mul(Matrix4 matrix) {
		tmp.set(origin).add(direction);
		tmp.mul(matrix);
		origin.mul(matrix);
		direction.set(tmp.sub(origin));
		return this;
	}

	/** {@inheritDoc} */
	public String toString() {
		return "ray [" + origin + ":" + direction + "]";
	}

	/**
	 * Sets the starting position and the direction of this ray.
	 * 
	 * @param origin
	 *            The starting position
	 * @param direction
	 *            The direction
	 * @return this ray for chaining
	 */
	public Ray set(com.github.tagwanj.math.Vector3 origin, Vector3 direction) {
		this.origin.set(origin);
		this.direction.set(direction);
		return this;
	}

	/**
	 * Sets this ray from the given starting position and direction.
	 * 
	 * @param x
	 *            The x-component of the starting position
	 * @param y
	 *            The y-component of the starting position
	 * @param z
	 *            The z-component of the starting position
	 * @param dx
	 *            The x-component of the direction
	 * @param dy
	 *            The y-component of the direction
	 * @param dz
	 *            The z-component of the direction
	 * @return this ray for chaining
	 */
	public Ray set(float x, float y, float z, float dx, float dy, float dz) {
		this.origin.set(x, y, z);
		this.direction.set(dx, dy, dz);
		return this;
	}

	/**
	 * Sets the starting position and direction from the given ray
	 * 
	 * @param ray
	 *            The ray
	 * @return This ray for chaining
	 */
	public Ray set(Ray ray) {
		this.origin.set(ray.origin);
		this.direction.set(ray.direction);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;
		Ray r = (Ray) o;
		return this.direction.equals(r.direction) && this.origin.equals(r.origin);
	}

	@Override
	public int hashCode() {
		final int prime = 73;
		int result = 1;
		result = prime * result + this.direction.hashCode();
		result = prime * result + this.origin.hashCode();
		return result;
	}
}
