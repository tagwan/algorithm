package com.github.tagwan.other.wheel

import com.github.tagwan.other.timewheel.TimeWheel


/**
 * 超时接口, 所有使用[TimeWheel]的事件都需要实现这个接口
 */
interface ScheduledEvent {
    fun startTimingCallback() {}
    fun timeoutCallback()
}