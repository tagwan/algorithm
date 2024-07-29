package com.github.tagwan.structure.aoi.link

import com.github.tagwan.math.Vector2
import com.github.tagwan.other.Fix64
import com.github.tagwan.other.toFix64
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

/**
 * Aoi zone
 * <p>
 *     使用跳跃表+十字链方式实现
 *     效率插入、移动、查找均到毫秒一下
 *
 * @author jdg
 */
class AoiZone {

    private var xLinks: AoiLinkedList
    private var yLinks: AoiLinkedList
    private val entityMap: HashMap<Long, AoiEntity> = HashMap()

    constructor() {
        xLinks = AoiLinkedList()
        yLinks = AoiLinkedList()
    }

    constructor(
        xLinksLimit: Fix64,
        yLinksLimit: Fix64
    ) {
        xLinks = AoiLinkedList(limit = xLinksLimit)
        yLinks = AoiLinkedList(limit = yLinksLimit)
    }

    constructor(
        xLinksLimit: Fix64,
        yLinksLimit: Fix64,
        maxLayer: Int
    ) {
        xLinks = AoiLinkedList(maxLayer, xLinksLimit)
        yLinks = AoiLinkedList(maxLayer, yLinksLimit)
    }

    public operator fun get(key: Long): AoiEntity? {
        return entityMap[key]
    }

    /**
     * Add a new AoiEntity
     *
     * @param key
     * @param x         X MinValue = -3.402823E+38f
     * @param y         Y MinValue = -3.402823E+38f
     * @param area
     * @param enter
     * @return
     */
    fun enter(key: Long, x: Fix64, y: Fix64, area: Vector2, enter: HashSet<Long>): AoiEntity {
        val entity = this.enter(key, x, y)
        this.refresh(key, area, enter)
        return entity
    }

    /**
     * 添加到AOI
     *
     * <p>
     *      根据新增对象的X,Y坐标,依次遍历X,Y轴坐标链表,这里有两个目的,
     *      一个是获得这个新增对象的坐标在X,Y轴坐标的位置, 另一方面获得该通知哪些结点.
     *      通知的范围,每个对象可以自己定制自己的通知范围。
     * <p>
     *      必须X,Y坐标同时都在通知范围内才可以进入通知集合，我们要按照从小到大进行插入。
     *
     * @param key
     * @param x
     * @param y
     * @return
     */
    fun enter(key: Long, x: Fix64, y: Fix64): AoiEntity {
        return Optional.ofNullable(this[key]).orElseGet {
            val entity = AoiEntity(key)
            entity.x = xLinks.add(x, entity)
            entity.y = yLinks.add(y, entity)
            entityMap[key] = entity
            entity
        }
    }

    /**
     *  Refresh the AoiEntity
     *
     * @param key
     * @param area
     * @param enter
     * @return
     */
    fun refresh(key: Long, area: Vector2, enter: HashSet<Long>): AoiEntity? {
        val entity = this.refresh(key, area)
        enter.clear()
        enter.addAll(entity?.viewEntity?: emptySet())
        return entity
    }

    fun refresh(key: Long, area: Vector2): AoiEntity? {
        val entity = this[key] ?: return null
        this.find(entity, area)
        return  entity
    }

    fun refresh(key: Long, x: Fix64, y: Fix64, area: Vector2, enter: HashSet<Long>): AoiEntity? {
        val entity = this.refresh(key, x, y, area)
        enter.clear()
        enter.addAll(entity?.viewEntity?: emptySet())
        return entity
    }

    fun refresh(key: Long, x: Fix64, y: Fix64, area: Vector2): AoiEntity? {
        val entity = entityMap[key]
            ?: return null

        var isFind = false
        if (Fix64.abs(entity.x?.value ?: Fix64.zero - x) > Fix64.zero) {
            isFind = true
            xLinks.move(entity.x, x)
        }

        if (Fix64.abs(entity.x?.value ?: Fix64.zero - y) > Fix64.zero) {
            isFind = true
            yLinks.move(entity.y, y)
        }

        if (isFind)
            this.find(entity, area)

        return entity
    }

    /**
     * Refresh including myself
     *
     * @param key
     * @param area
     * @param enter
     * @return
     */
    fun refreshIncludingMyself(key: Long, area: Vector2, enter: HashSet<Long>): AoiEntity? {
        val entity = this.refresh(key, area, enter)
        enter.add(key)
        return entity
    }

    fun refreshIncludingMyself(key: Long, x: Fix64, y: Fix64, area: Vector2, enter: HashSet<Long>): AoiEntity? {
        val entity = this.refresh(key, x, y, area, enter)
        enter.add(key)
        return entity
    }

    /**
     * Find
     *
     * @param node
     * @param area
     */
    fun find(node: AoiEntity, area: Vector2) {
        swapViewEntity(node.viewEntity, node.viewEntityBak)

        // region xLinks
        for (i in 0 until 2) {
            var cur = if (i == 0) node.x?.right else node.x?.left
            while (cur != null) {
                if (Fix64.abs(cur.value) - Fix64.abs(node.x?.value ?: Fix64.zero) > area.x.toFix64()) {
                    break
                }

                if (Fix64.abs(cur.entity?.y?.value ?: Fix64.zero) - Fix64.abs(node.y?.value
                        ?: Fix64.zero) <= area.y.toFix64()) {
                    val node1 = Vector2(node.x?.value?.toInt() ?: 0, node.y?.value?.toInt() ?: 0)
                    val node2 = Vector2(cur.entity?.x?.value?.toInt() ?: 0, cur.entity?.y?.value?.toInt() ?: 0)
                    if ((node1 distance node2) <= (area.x).toFix64()) {
                        node.viewEntity.add(cur.entity?.key ?: 0)
                    }

                    cur = if (i == 0) cur.right else cur.left
                }
            }
        }
        // endregion

        // region yLinks
        for (i in 0 until 2) {
            var cur = if (i == 0) node.y?.right else node.y?.left
            while (cur != null) {
                if (Fix64.abs(cur.value) - Fix64.abs(node.y?.value ?: Fix64.zero) > area.y.toFix64()) {
                    break
                }

                if (Fix64.abs(cur.entity?.x?.value ?: Fix64.zero) - Fix64.abs(node.x?.value
                        ?: Fix64.zero) <= area.x.toFix64()) {
                    val node1 = Vector2(node.x?.value?.toInt() ?: 0, node.y?.value?.toInt() ?: 0)
                    val node2 = Vector2(cur.entity?.x?.value?.toInt() ?: 0, cur.entity?.y?.value?.toInt() ?: 0)
                    if ((node1 distance node2) <= (area.y).toFix64()) {
                        node.viewEntity.add(cur.entity?.key ?: 0)
                    }

                    cur = if (i == 0) cur.right else cur.left
                }
            }
        }
        // endregion
    }

    fun exit(key: Long) {
        val entity = entityMap[key]
    }

    fun exit(node: AoiEntity) {
        xLinks.remove(node.x)
        yLinks.remove(node.y)
        entityMap.remove(node.key)
        node.dispose()
    }

    companion object {
        private fun swapViewEntity(viewEntity: HashSet<Long>, viewEntityBak: HashSet<Long>) {

            val tmp1 = HashSet(viewEntity)
            val tmp2 = HashSet(viewEntityBak)
            viewEntity.clear()
            viewEntityBak.clear()
            viewEntity.addAll(tmp2)
            viewEntityBak.addAll(tmp1)

        }

    }
}

fun main() {

    // 创建一个AOI区域、如果地图过大可以定义多个区域
    val zone = AoiZone()

    // AOI的显示区域、每个客户端都可以单独定义区域、这样可以更好的适用于不同的分辨率。
    val area = Vector2(3, 3)

    // 添加50个玩家。
    for (i in 0 until 50) {
        //println("添加玩家: key: $i, pos: ($i, $i)")
        zone.enter(i.toLong(), i.toFix64(), i.toFix64())
    }

    println("-------刷新key为3的信息----------")
    val enters = HashSet<Long>()
    zone.refresh(3, area, enters)

    println("---------------加入玩家范围的玩家列表--------------")
    for (aoiKey in enters) {
        val vo = zone[aoiKey] ?: continue
        println("x:${vo.x?.value}, y:${vo.y?.value}")
    }

    // 更新key为3的坐标。
    val entity = zone.refresh(3.toLong(), 20.toFix64(), 20.toFix64(), Vector2(3, 3), enters)
        ?: return

    println("---------------离开玩家范围的玩家列表--------------")
    for (aoiKey in entity.leave()) {
        val vo = zone[aoiKey] ?: continue
        println("x:${vo.x?.value}, y:${vo.y?.value}")
    }

    println("---------------离开玩家范围的玩家列表--------------")
    for (aoiKey in enters) {
        val vo = zone[aoiKey] ?: continue
        println("x:${vo.x?.value}, y:${vo.y?.value}")
    }

    // 离开当前AOI
    zone.exit(50)
}