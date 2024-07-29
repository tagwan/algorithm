package com.github.tagwan.other.timewheel

import java.util.concurrent.DelayQueue

/**
 * Time wheel
 *
 * @property tickMs 一个槽的时间间隔(时间轮最小刻度)
 * @property wheelSize 时间轮大小(槽的个数)
 * @property currentTime
 * @property delayQueue 一个timer只有一个delayqueue
 *
 */
class TimeWheel(
    val tickMs: Long,
    val wheelSize: Int,
    var currentTime: Long,
    val delayQueue: DelayQueue<TimerTaskList>
) {
    /**
     * 一轮的时间跨度
     */
    private val interval: Long = tickMs * wheelSize

    /**
     * 槽
     */
    private val buckets: Array<TimerTaskList?> = arrayOfNulls(wheelSize)

    /**
     * 上层时间轮
     */
    @Volatile
    private var overflowWheel: TimeWheel? = null

    init {
        currentTime -= currentTime % tickMs
        for (i in 0 until wheelSize) {
            buckets[i] = TimerTaskList()
        }
    }

    fun add(entry: TimerTaskEntry): Boolean {
        val expiration: Long = entry.expireMs
        if (expiration < tickMs + currentTime) {
            // 到期了
            return false
        } else if (expiration < currentTime + interval) {
            // 扔进当前时间轮的某个槽里,只有时间大于某个槽,才会放进去
            val virtualId = expiration / tickMs
            val index = (virtualId % wheelSize).toInt()
            val bucket = buckets[index]
                ?: return false
            bucket.addTask(entry)
            //设置bucket 过期时间
            if (bucket.setExpiration(virtualId * tickMs)) {
                //设好过期时间的bucket需要入队
                delayQueue.offer(bucket)
                return true
            }
        } else {
            //当前轮不能满足,需要扔到上一轮
            val timeWheel = getOverflowWheel()
            return timeWheel?.add(entry) ?: false
        }
        return false
    }

    private fun getOverflowWheel(): TimeWheel? {
        if (overflowWheel == null) {
            synchronized(this) {
                if (overflowWheel == null) {
                    overflowWheel = TimeWheel(interval, wheelSize, currentTime, delayQueue)
                }
            }
        }
        return overflowWheel
    }

    /**
     * 推进指针
     *
     * @param timestamp
     */
    fun advanceLock(timestamp: Long) {
        if (timestamp > currentTime + tickMs) {
            currentTime = timestamp - timestamp % tickMs
            if (overflowWheel != null) {
                getOverflowWheel()?.advanceLock(timestamp)
            }
        }
    }
}