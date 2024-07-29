package com.github.tagwan.other.timer

/**
 * 时间轮接口
 *
 * @data 2022/5/19 9:58
 */
interface WheelTimer {
    fun init()
    fun start()
    fun shutdown()
    fun newTimeout(timer: Timer?)
    fun isRunning(timerId: String?): Boolean
    fun length(): Int
    fun cancel(timerId: String?)

    interface Callback {
        fun handle(timer: Timer?)
    }
}
