package com.github.tagwan.other.time

import com.github.tagwan.pattern.other.PartialFunction


/**
 * 时间轮
 * 10毫秒精度，100
 *
 * @data 2022/5/19 11:18
 */
class TimeWheel {

    // 定长链表数组
    private val near: Array<LinkList?> = arrayOfNulls(first_level_tick.toInt())
    private val wheel: Array<Array<LinkList?>> = Array(level_num.toInt()) { arrayOfNulls(high_level_tick.toInt()) }

    // 时间轮自定义的时间,索引
    var time: Long = System.currentTimeMillis()
    var actualTime: Long = time


    private val dealNear = object : PartialFunction<Long, LinkList?>() {
        override fun applyIfDefined(dtime: Long): LinkList? {
            val index = dtime and (first_level_tick * 10 - 1)
            return near[index]
        }

        override fun test(dtime: Long): Boolean {
            return dtime < first_level_tick * 10
        }
    }

    private val dealWheel0 = object : PartialFunction<Long, LinkList?>() {
        override fun applyIfDefined(dtime: Long): LinkList? {
            val index = dtime and (first_level_tick * 10 * high_level_tick - 1)
            return wheel[0, index]
        }

        override fun test(dtime: Long): Boolean {
            return dtime < first_level_tick * 10 * high_level_tick * high_level_tick
        }
    }

    private val dealWheel1 = object : PartialFunction<Long, LinkList?>() {
        override fun applyIfDefined(dtime: Long): LinkList? {
            val index = dtime and (first_level_tick * 10 * high_level_tick * high_level_tick - 1)
            return wheel[1, index]
        }

        override fun test(dtime: Long): Boolean {
            return dtime < first_level_tick * 10 * high_level_tick * high_level_tick * high_level_tick
        }
    }

    private val dealWheel2 = object : PartialFunction<Long, LinkList?>() {
        override fun applyIfDefined(dtime: Long): LinkList? {
            val index = dtime and (first_level_tick * 10 * high_level_tick * high_level_tick * high_level_tick - 1)
            return wheel[2, index]
        }

        override fun test(dtime: Long): Boolean {
            return dtime < first_level_tick * 10 * high_level_tick * high_level_tick * high_level_tick * high_level_tick
        }
    }

    private val dealWheel3 = object : PartialFunction<Long, LinkList?>() {
        override fun applyIfDefined(dtime: Long): LinkList? {
            val index =
                dtime and (first_level_tick * 10 * high_level_tick * high_level_tick * high_level_tick * high_level_tick - 1)
            return wheel[3, index]
        }

        override fun test(dtime: Long): Boolean {
            return dtime < first_level_tick * 10 * high_level_tick * high_level_tick * high_level_tick * high_level_tick * high_level_tick
        }
    }

    private val dealWheel4 = object : PartialFunction<Long, LinkList?>() {
        override fun applyIfDefined(dtime: Long): LinkList? {
            val index =
                dtime and (first_level_tick * 10 * high_level_tick - 1)
            return wheel[4, index]
        }

        override fun test(dtime: Long): Boolean {
            return dtime < first_level_tick * 10 * high_level_tick * high_level_tick * high_level_tick * high_level_tick * high_level_tick * high_level_tick
        }
    }

    private val dealPass = object : PartialFunction<Long, LinkList?>() {
        override fun applyIfDefined(x: Long): LinkList? {
            return null
        }

        override fun test(t: Long): Boolean {
            return true
        }

    }

    private val dealChain =
        dealNear orElse dealWheel0 orElse dealWheel1 orElse dealWheel2 orElse dealWheel3 orElse dealWheel4 orElse dealPass


    fun executeTimer() {
        val index = time and (first_level_tick - 1L)
        val linkList = this.near[index.toInt()]
        while (linkList?.head?.next != null) {
            linkList.head.next?.process()
        }
    }

    /**
     * 注意节点的expire时间用来确认事件应该放在数组的哪个位置
     * delay 用来判断在哪层时间轮
     *
     * @param node
     */
    fun addNode(node: TimeNode) {
        val delay = node.expire - time
        val link = dealChain.apply(delay)

        // link node
        link?.tail?.next = node
        link?.tail = node
        node.next = null
    }


    fun addjustList() {
        val divisor = time shr 6
        val remainer = time and first_level_tick
        while (remainer == 0L) {
            // pass
        }
    }

    fun adjustList(level: Int, index: Int) {

    }
}

const val first_level_tick = 64L
const val high_level_tick = 32L     //
const val level_num = 4L            // 层数

operator fun Array<LinkList?>.get(index: Long): LinkList? {
    return this[index.toInt()]
}

operator fun Array<Array<LinkList?>>.get(index1: Int, index2: Long): LinkList? {
    return this[index1][index2]
}