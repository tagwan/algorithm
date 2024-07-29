package com.github.tagwan.other.timewheel

interface Timer {
    /**
     * 添加一个新任务
     *
     * @param timerTask
     */
    fun add(timerTask: TimerTask)

    /**
     * 推动指针
     *
     * @param timeout
     */
    fun advanceClock(timeout: Long)

    /**
     * 等待执行的任务
     *
     * @return
     */
    fun size(): Int

    /**
     * 关闭服务,剩下的无法被执行
     */
    fun shutdown()
}