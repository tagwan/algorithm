package com.github.tagwan.other.wheel

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit


/**
 * 使用一个空阻塞队列来模拟定时器
 * 允许支持的最小单位是100ms
 */
class BlockingQueueTimer(interval: Long) : TickTimer {
    // 单位是ms
    private val interval: Long

    // 使用阻塞队列来模拟
    private val blockingQueue: BlockingQueue<*>

    init {
        if (interval < MINIMUM_INTERVAL) {
            throw MinimumIntervalException(MINIMUM_INTERVAL, interval)
        }
        this.interval = interval
        blockingQueue = LinkedBlockingQueue<Any?>()
    }

    override fun once() {
        try {
            // 阻塞取空队列队首，依次来模拟一次计时
            blockingQueue.poll(interval, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun getInterval(): Long {
        return interval
    }

    companion object {
        // 最小单位, 默认为100ms
        private const val MINIMUM_INTERVAL: Long = 20
    }
}