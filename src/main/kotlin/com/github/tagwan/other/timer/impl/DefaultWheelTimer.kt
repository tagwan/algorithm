//package com.github.tagwan.other.timer.impl
//
//import java.util.concurrent.TimeUnit
////import io.netty.util.HashedWheelTimer
////import io.netty.util.Timeout
////import io.netty.util.concurrent.DefaultThreadFactory
//
//import java.util.concurrent.ThreadFactory
//
//
///**
// * 默认时间轮
// *
// * @data 2022/5/19 10:01
// */
//class DefaultWheelTimer(tickDuration: Long, unit: TimeUnit?, callback: Callback) : WheelTimer {
//    private val hashedWheelTimer: HashedWheelTimer
//    private val timerTaskMap: MutableMap<String, Timeout> = Maps.newConcurrentMap()
//    private val callback: Callback
//
//    init {
//        hashedWheelTimer = HashedWheelTimer(threadFactory, tickDuration, unit)
//        this.callback = callback
//    }
//
//    fun init() {
//        //do nothing for current implement
//    }
//
//    fun start() {
//        hashedWheelTimer.start()
//    }
//
//    fun shutdown() {
//        hashedWheelTimer.stop()
//    }
//
//    fun newTimeout(timer: Timer) {
//        val duration: Long = timer.getTimeout() - System.currentTimeMillis()
//        val timeout: Timeout = hashedWheelTimer.newTimeout(object : TimerTask() {
//            @Throws(Exception::class)
//            fun run(timeout: Timeout?) {
//                timerTaskMap.remove(timer.getTimerId())
//                callback.handle(timer)
//            }
//        }, duration, TimeUnit.MILLISECONDS)
//        timerTaskMap[timer.getTimerId()] = timeout
//    }
//
//    fun isRunning(timerId: String): Boolean {
//        val tt: Timeout = timerTaskMap[timerId] ?: return false
//        return !tt.isCancelled()
//    }
//
//    fun length(): Int {
//        return timerTaskMap.size
//    }
//
//    fun cancel(timerId: String) {
//        val tt: Timeout? = timerTaskMap[timerId]
//        if (tt != null) {
//            tt.cancel()
//        }
//        timerTaskMap.remove(timerId)
//    }
//
//    companion object {
//        private val threadFactory: ThreadFactory = DefaultThreadFactory("timeWheel")
//    }
//}