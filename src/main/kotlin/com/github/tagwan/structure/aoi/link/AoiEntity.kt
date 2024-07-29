package com.github.tagwan.structure.aoi.link

import java.util.stream.Collectors

/**
 * Aoi entity
 *
 * <p>
 *     游戏地图上有一些npc和玩家在移动，每一个这样移动的对象我们叫做AOIEntity，
 *     每一个AOIEntity可以挂多个不同半径的AOI，每一个这种半径的AOI单元我们叫做AOINode，
 *     如此，AOIEntity拥有多个AOINode，然后每一个场景管理者AOIManager管理着多个这样的AOIEntity对象。
 * @property key
 * @constructor Create empty Aoi entity
 */
class AoiEntity(
    val key: Long
) {
    var x: AoiNode? = null
    var y: AoiNode? = null
    val viewEntity: HashSet<Long> = HashSet()
    val viewEntityBak: HashSet<Long> = HashSet()

    fun dispose() {
        viewEntity.clear()
        viewEntityBak.clear()
    }

    /**
     * 并集
     *
     * @return
     */
    fun move(): Set<Long> {
        val list = HashSet<Long>(viewEntity)
        list.addAll(viewEntityBak)
        return list
    }

    /**
     * 差集
     *
     * @return
     */
    fun leave(): Set<Long> {
        return viewEntityBak.stream().filter { !viewEntity.contains(it) }.collect(Collectors.toSet())
    }
}