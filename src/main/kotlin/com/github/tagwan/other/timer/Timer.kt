package com.github.tagwan.other.timer

class Timer {
    /**
     * timer Id
     */
    var timerId: String? = null

    /**
     * 应用名
     */
    var app: String? = null

    /**
     * 超时时间，绝对时间值，单位毫秒，如在2017-11-17 18:00:00超时
     */
    var timeout: Long = 0

    /**
     * 重要等级
     */
    var level = LEVEL_NORMAL

    /**
     * timer类型
     */
    var timerType = 0

    /**
     * timer状态
     */
    var timerState = 0

    /**
     * 任务元数据
     */
    private var taskMeta: HashMap<String, String> = HashMap()
    val isExpired: Boolean
        get() = System.currentTimeMillis() > timeout

    fun getTaskMeta(): Map<String, String> {
        return taskMeta
    }

    fun setTaskMeta(taskMeta: HashMap<String, String>) {
        this.taskMeta = taskMeta
    }

    fun addTaskMeta(key: String, value: String) {
        taskMeta[key] = value
    }

    override fun toString(): String {
        val sb = StringBuilder("Timer{")
        sb.append("timerId='").append(timerId).append('\'')
        sb.append(", app='").append(app).append('\'')
        sb.append(", handle=").append(timeout)
        sb.append(", level=").append(level)
        sb.append(", timerType=").append(timerType)
        sb.append(", timerState=").append(timerState)
        sb.append(", taskMeta=").append(taskMeta)
        sb.append('}')
        return sb.toString()
    }

    companion object {
        const val STATE_INIT = 0
        const val STATE_RUNNING = 1
        const val STATE_FINISH = 2
        const val STATE_CANCEL = 3
        const val LEVEL_NORMAL = 0
        const val LEVEL_IMPORTANT = 1
    }
}