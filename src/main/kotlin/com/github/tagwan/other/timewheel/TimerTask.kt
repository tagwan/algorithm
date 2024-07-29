package com.github.tagwan.other.timewheel

/**
 * Timer task
 *
 * @property desc
 * @property delayMs 延时时间
 * @constructor Create empty Timer task
 */
class TimerTask(
    val desc: String,
    val delayMs: Long
) : Runnable {

    // 任务所在的entry
    private var timerTaskEntry: TimerTaskEntry? = null

    /**
     * 如果这个timetask已经被一个已存在的TimerTaskEntry持有,先移除一个
     *
     * @param entry
     */
    @Synchronized
    fun setTimerTaskEntry(entry: TimerTaskEntry) {
        val task = timerTaskEntry
        if (task != null && task != entry) {
            task.remove()
        }
        timerTaskEntry = entry
    }

    fun getTimerTaskEntry(): TimerTaskEntry? {
        return timerTaskEntry
    }

    override fun run() {
        // log.info("============={}任务执行", desc)
    }
}