package com.github.tagwan.other.timewheel

import java.util.concurrent.DelayQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SystemTimer : Timer {

    // 底层时间轮
    private var timeWheel: TimeWheel?

    // 一个Timer只有一个延时队列
    private val delayQueue: DelayQueue<TimerTaskList> = DelayQueue()

    /**
     * 过期任务执行线程
     */
    private val workerThreadPool: ExecutorService

    /**
     * 轮询delayQueue获取过期任务线程
     */
    private val bossThreadPool: ExecutorService

    init {
        timeWheel = TimeWheel(1, 20, System.currentTimeMillis(), delayQueue)
        workerThreadPool = Executors.newFixedThreadPool(100)
        bossThreadPool = Executors.newFixedThreadPool(1)

        // 20ms推动一次时间轮运转
        bossThreadPool.submit {
            while (true) {
                advanceClock(20)
            }
        }
    }

    fun addTimerTaskEntry(entry: TimerTaskEntry) {
        val wheel = timeWheel
            ?: return

        if (!wheel.add(entry)) {
            //已经过期了
            val timerTask = entry.timerTask
            //log.info("=====任务:{} 已到期,准备执行============", timerTask.getDesc())
            workerThreadPool.submit(timerTask)
        }
    }

    override fun add(timerTask: TimerTask) {
        //log.info("=======添加任务开始====task:{}", timerTask.getDesc())
        val entry = TimerTaskEntry(timerTask, timerTask.delayMs + System.currentTimeMillis())
        timerTask.setTimerTaskEntry(entry)
        addTimerTaskEntry(entry)
    }

    /**
     * 推动指针运转获取过期任务
     *
     * @param timeout 时间间隔
     * @return
     */
    @Synchronized
    override fun advanceClock(timeout: Long) {
        val wheel = timeWheel
            ?: return

        try {
            val bucket = delayQueue.poll(timeout, TimeUnit.MILLISECONDS)
            if (bucket != null) {
                //推进时间
                wheel.advanceLock(bucket.getExpiration())
                //执行过期任务(包含降级)
                bucket.clear(::addTimerTaskEntry)
            }
        } catch (e: InterruptedException) {
            //log.error("advanceClock error")
        }
    }

    override fun size(): Int {
        //todo
        return 0
    }

    override fun shutdown() {
        bossThreadPool.shutdown()
        workerThreadPool.shutdown()
        timeWheel = null
    }
}