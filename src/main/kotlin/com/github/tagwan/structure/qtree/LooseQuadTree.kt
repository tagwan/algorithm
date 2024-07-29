package com.github.tagwan.structure.qtree

import com.github.tagwan.math.Vector2
import com.github.tagwan.math.spiral
import java.util.*
import kotlin.collections.HashMap


/**
 *
 *----------------------------
 *  00,00  01,00  10,00  11,00
 * ----------------------------
 *  00,01  01,01  10,01  11,01
 * ----------------------------
 *  00,10  01,10  10,10  11,10
 * ----------------------------
 *  00,11  01,11  10,11  11,11
 *----------------------------
 */

/**
 * 松散四叉树
 * 提前初始化区域，xy用二进制进行编码
 */
class LooseQuadTree<T : IQuadNode>(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val maxDepth: Int
) {

    /**
     * 根节点区域
     */
    val area: Area<T>

    /**
     * x编码-y编码-区域
     */
    val areaMap = HashMap<Long, HashMap<Long, Area<T>>>()

    init {
        area = Area(x, y, width, height, 1, 0, 0)
        createArea(area)
    }

    /**
     * 递归建立区域
     */
    private fun createArea(area: Area<T>) {
        if (area.depth >= maxDepth) {
            return
        }

        if (area.width < 2 || area.height < 2) {
            return
        }

        val nowDepth = area.depth + 1

        //分四个区域
        val leftWidth = (area.width + 1) / 2
        val rightWidth = area.width - leftWidth
        val topHeight = (area.height + 1) / 2
        val bottomHeight = area.height - topHeight

        val topLeftArea = Area<T>(
            area.x,
            area.y,
            leftWidth,
            topHeight,
            nowDepth,
            area.xCode shl 1,
            area.yCode shl 1
        )
        val topRightArea = Area<T>(
            area.x + leftWidth,
            area.y,
            rightWidth,
            topHeight,
            nowDepth,
            (area.xCode shl 1) + 1,
            area.yCode shl 1
        )
        val bottomLeftArea = Area<T>(
            area.x,
            area.y + topHeight,
            leftWidth,
            bottomHeight,
            nowDepth,
            area.xCode shl 1,
            (area.yCode shl 1) + 1
        )
        val bottomRightArea = Area<T>(
            area.x + leftWidth,
            area.y + topHeight,
            rightWidth,
            bottomHeight,
            nowDepth,
            (area.xCode shl 1) + 1,
            (area.yCode shl 1) + 1
        )
        area.areas = listOf(topLeftArea, topRightArea, bottomLeftArea, bottomRightArea)

        for (subArea in area.areas) {
            if (subArea.depth >= maxDepth || subArea.width < 2 || subArea.height < 2) {
                //保证所有子节点可以继续分割，此时的最小子节点才可以加入
                areaMap.getOrPut(topLeftArea.xCode) { hashMapOf() }[topLeftArea.yCode] = topLeftArea
                areaMap.getOrPut(topRightArea.xCode) { hashMapOf() }[topRightArea.yCode] = topRightArea
                areaMap.getOrPut(bottomLeftArea.xCode) { hashMapOf() }[bottomLeftArea.yCode] =
                    bottomLeftArea
                areaMap.getOrPut(bottomRightArea.xCode) { hashMapOf() }[bottomRightArea.yCode] =
                    bottomRightArea
                return
            }
        }

        createArea(topLeftArea)
        createArea(topRightArea)
        createArea(bottomLeftArea)
        createArea(bottomRightArea)
    }

    /**
     * 插入对象
     */
    fun insert(obj: T): Boolean {
        val findArea = area.searchArea(obj.findPosX(), obj.findPosY())
            ?: return false
        return findArea.objs.add(obj)
    }

    /**
     * 移除对象
     */
    fun remove(obj: T): Boolean {
        val findArea = area.searchArea(obj.findPosX(), obj.findPosY())
            ?: return false
        return findArea.objs.remove(obj)
    }

    /**
     * 搜索范围内的对象
     *
     * @param range 搜索范围，默认一格区域
     */
    fun search(x: Int, y: Int, range: Int = 1): List<T> {
        val searchArea = area.searchArea(x, y)
            ?: return emptyList()

        val allObjs = LinkedList<T>()

        for (xCode in searchArea.xCode - range + 1 until searchArea.xCode + range) {
            for (yCode in searchArea.yCode + 1 - range until searchArea.yCode + range) {
                val findArea = areaMap[xCode, yCode]
                if (findArea != null) {
                    allObjs.addAll(findArea.objs)
                }
            }
        }

        return allObjs
    }

    /**
     * 螺旋矩阵搜索
     */
    fun searchSpiral(x: Int, y: Int, checkOk: (obj: T) -> Boolean) {
        val searchArea = area.searchArea(x, y)
            ?: return

        for (obj in searchArea.objs) {
            if (checkOk(obj)) {
                return
            }
        }

        Vector2(searchArea.xCode, searchArea.yCode).spiral { xCode, yCode ->
            val findArea = areaMap[xCode, yCode]
                ?: return@spiral false

            for (obj in findArea.objs) {
                if (checkOk(obj)) {
                    return@spiral true
                }
            }

            return@spiral false
        }
    }

    private operator fun HashMap<Long, HashMap<Long, Area<T>>>.get(x: Long, y: Long): Area<T>? = this@get[x]?.get(y)
}