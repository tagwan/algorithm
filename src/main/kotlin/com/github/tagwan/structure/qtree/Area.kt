package com.github.tagwan.structure.qtree

import java.util.ArrayList


class Area<T : IQuadNode>(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val depth: Int,  // 深度
    val xCode: Long,   // x轴编码
    val yCode: Long    // y轴编码
) {

    //格子内的物体
    val objs = ArrayList<T>()

    //子区域 固定4个
    var areas: List<Area<T>> = emptyList()
        internal set

    /**
     * 位置是否包含在该区域内
     */
    fun containsPos(x: Int, y: Int): Boolean {
        return x >= this.x && x < this.x + width && y >= this.y && y < this.y + height
    }

    /**
     * 搜索区域
     */
    fun searchArea(x: Int, y: Int): Area<T>? {
        if (areas.isNotEmpty()) {
            for (i in 0..3) {
                if (areas[i].containsPos(x, y)) {
                    return areas[i].searchArea(x, y)
                }
            }
        }
        if (containsPos(x, y)) {
            return this
        }
        return null
    }
}