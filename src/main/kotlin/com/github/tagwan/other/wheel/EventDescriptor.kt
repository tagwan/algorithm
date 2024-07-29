package com.github.tagwan.other.wheel

import com.github.tagwan.other.timewheel.TimeWheel


class EventDescriptor(
    val event: ScheduledEvent,  // 等待定时的任务对象
    val msLater: Long           // 多少毫秒后执行
) {

    /**
     * 记录这个事件被加入公共队列的时间
     * 用于 [TimeWheel.addEvent]中的补偿机制
     */
    val addedTime: Long = System.currentTimeMillis()
}