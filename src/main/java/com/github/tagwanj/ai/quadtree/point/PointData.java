package com.github.tagwanj.ai.quadtree.point;

import com.github.tagwanj.ai.quadtree.Data;
import com.github.tagwanj.math.Vector3;

/**
 * 坐标点数据结构
 */
public class PointData<T> extends Data<T> {

	private com.github.tagwanj.math.Vector3 point;

	public PointData(com.github.tagwanj.math.Vector3 point, T value) {
		super(value);
		this.point = point;
	}

	public com.github.tagwanj.math.Vector3 getPoint() {
		return point;
	}

	public void setPoint(Vector3 point) {
		this.point = point;
	}

	@Override
	public int compareTo(Data<T> data) {
		PointData<T> o=(PointData<T>) data;
		if (this.point.x < o.point.x) {
			return -1;
		} else if (this.point.x > o.point.x) {
			return 1;
		} else {
			if (this.point.z < o.point.z) {
				return -1;
			} else if (this.point.z > o.point.z) {
				return 1;
			}
			return 0;
		}
	}

	@Override
	public String toString() {
		return "PointData [point=" + point + ", getValue()=" + getValue() + "]";
	}
	
}
