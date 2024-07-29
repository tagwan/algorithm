//package com.github.tagwan.other.timer.impl
//
//import com.github.tagwan.other.timer.Timer
//import com.github.tagwan.other.timer.WheelTimer
//import java.util.concurrent.TimeUnit
//
//
///**
// * 可恢复的时间轮实现
// *
// * @data 2022/5/19 9:59
// */
//class RecoverableWheelTimer(dbPath: String, tickDuration: Long, unit: TimeUnit?, callback: Callback) :
//    WheelTimer {
//    private val wheelTimer: WheelTimer
//    private val dbPath: String
//    private var storeForRunning: TimerStore? = null
//    private var storeForFinished: TimerStore? = null
//
//    @Volatile
//    private var state = State.NONE
//    private val timeoutCallback: TimeoutCallback
//    private val finishedCallback: Callback
//    private val masterWorker: ScheduledExecutorService =
//        Executors.newSingleThreadScheduledExecutor(DefaultThreadFactory("masterWorker"))
//
//    private enum class State {
//        NONE, INIT, RUNNING, CLOSED
//    }
//
//    init {
//        require(!StringUtils.isBlank(dbPath)) { "dbPath should not be blank." }
//        this.dbPath = dbPath
//        timeoutCallback = TimeoutCallback()
//        finishedCallback = callback
//        wheelTimer = DefaultWheelTimer(tickDuration, unit, timeoutCallback)
//    }
//
//    fun init() {
//        storeForRunning = DefaultTimerStore(DefaultKVStore(LevelDBOp(toPath(dbPath, RUNNING_DB_DIR))))
//        storeForFinished = DefaultTimerStore(DefaultKVStore(LevelDBOp(toPath(dbPath, FINISH_DB_DIR))))
//        state = State.INIT
//    }
//
//    private fun toPath(parent: String, child: String): String {
//        var parent = parent
//        if (!StringUtils.endsWith(parent, "/") && !StringUtils.endsWith(parent, "\\")) {
//            parent += "/"
//        }
//        return parent + child
//    }
//
//    fun start() {
//        check(state == State.INIT) { "Not in INIT state. please init first." }
//        try {
//            storeForRunning.open()
//            storeForFinished.open()
//        } catch (e: Exception) {
//            throw RuntimeException("open failed.", e)
//        }
//        wheelTimer.start()
//        //恢复timer
//        recover()
//
//        //开始后台finish相关的线程
//        startWorkerBackground()
//
//        //添加关闭钩子
//        addShutdownHook()
//        state = State.RUNNING
//    }
//
//    /**
//     * 关闭钩子
//     */
//    private fun addShutdownHook() {
//        Runtime.getRuntime().addShutdownHook(object : Thread() {
//            override fun run() {
//                shutdown()
//            }
//        })
//    }
//
//    /**
//     * 开启后台工作线程
//     */
//    private fun startWorkerBackground() {
//        masterWorker.scheduleWithFixedDelay(Runnable {
//            storeForFinished.iteratorOps(object : Callback() {
//                fun handle(timer: Timer): Boolean {
//                    //回调通知类服务
//                    finishedCallback.handle(timer)
//                    //移除通知回调完成的timer
//                    storeForFinished.delete(timer.getTimerId())
//                    return true
//                }
//            })
//        }, 0, 10, TimeUnit.MILLISECONDS)
//    }
//
//    private fun recover() {
//        logger.info("start recovering from local database files...")
//        //恢复步骤
//        //1. 从storeForFinish中迭代查询，并将查询到的timer从storeForRunning中删除
//        storeForFinished.iteratorOps(object : Callback() {
//            fun handle(timer: Timer): Boolean {
//                storeForRunning.delete(timer.getTimerId())
//                return true
//            }
//        })
//
//        //2. 遍历storeForRunning，将其中符合要求的timer重新加入时间轮，不符合要求的根据情况改变状态并投入storeForFinish中
//        storeForRunning.iteratorOps(object : Callback() {
//            fun handle(timer: Timer): Boolean {
//                if (timer.getTimerState() === Timer.STATE_CANCEL) {
//                    transferToFinish(timer)
//                    logger.info("timer canceled:$timer")
//                }
//                if (timer.getTimerState() === Timer.STATE_RUNNING) {
//                    wheelTimer.newTimeout(timer)
//                    logger.info("timer recovered:$timer")
//                }
//                if (timer.getTimerState() === Timer.STATE_FINISH) {
//                    timeoutCallback.handle(timer)
//                    logger.info("timer finished:$timer")
//                }
//                return true
//            }
//        })
//    }
//
//    private fun transferToFinish(timer: Timer) {
//        logger.debug("timer finished:{}", timer)
//        storeForFinished.save(timer)
//        storeForRunning.delete(timer.getTimerId())
//    }
//
//    fun shutdown() {
//        logger.info("begin to shutdown...")
//        masterWorker.shutdown()
//        wheelTimer.shutdown()
//        try {
//            storeForRunning.close()
//            storeForRunning.close()
//        } catch (e: Exception) {
//            throw RuntimeException("close failed.", e)
//        }
//        state = State.CLOSED
//        logger.info("shutdown finished.")
//    }
//
//    fun newTimeout(timer: Timer) {
//        check(state == State.RUNNING) { "Not in RUNNING state." }
//
//        //存储timer
//        timer.setTimerState(Timer.STATE_RUNNING)
//        if (Timer.LEVEL_IMPORTANT === timer.getLevel()) {
//            storeForRunning.saveSafety(timer)
//        } else {
//            storeForRunning.save(timer)
//        }
//        wheelTimer.newTimeout(timer)
//    }
//
//    fun isRunning(timerId: String?): Boolean {
//        check(state == State.RUNNING) { "Not in RUNNING state." }
//        return storeForRunning.query(timerId) != null
//    }
//
//    fun length(): Int {
//        return wheelTimer.length()
//    }
//
//    fun cancel(timerId: String?) {
//        if (state != State.RUNNING) {
//            return
//        }
//        wheelTimer.cancel(timerId)
//        val timer: Timer = storeForRunning.query(timerId)
//        timer.setTimerState(Timer.STATE_CANCEL)
//        transferToFinish(timer)
//        logger.info("timer canceled:{}", timer)
//    }
//
//    private inner class TimeoutCallback : WheelTimer.Callback {
//        fun handle(timer: Timer) {
//            timer.setTimerState(Timer.STATE_FINISH)
//            //容错处理
//            //先改变RUNNING存储中timer的状态，再插入FINISH存储timer，最后删除RUNNING存储中的timer
//            storeForRunning.save(timer)
//            transferToFinish(timer)
//        }
//    }
//
//    companion object {
//        const val RUNNING_DB_DIR = "/running"
//        const val FINISH_DB_DIR = "/finished"
//    }
//}