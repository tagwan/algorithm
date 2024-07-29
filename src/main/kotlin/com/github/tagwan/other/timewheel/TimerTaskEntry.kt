package com.github.tagwan.other.timewheel

class TimerTaskEntry(
    val timerTask: TimerTask?,
    val expireMs: Long
) : Comparable<TimerTaskEntry> {

    @Volatile
    var timedTaskList: TimerTaskList? = null
    var next: TimerTaskEntry? = null
    var prev: TimerTaskEntry? = null

    fun remove() {
        var currentList: TimerTaskList? = timedTaskList
        while (currentList != null) {
            currentList.remove(this)
            currentList = timedTaskList
        }
    }

    override operator fun compareTo(other: TimerTaskEntry): Int {
        return (expireMs - other.expireMs).toInt()
    }
}
