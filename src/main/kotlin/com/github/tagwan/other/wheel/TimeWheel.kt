package com.github.tagwan.other.wheel

import java.time.LocalDateTime
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * 时间轮核心逻辑
 *
 *
 * 这个时间轮目前支持的是大量的,频繁的定时,
 * 任务描述形式是 "在x时间后要执行某个任务"
 * 而不是 "在某个时间执行某个任务"
 *
 *
 *
 *
 * 这里并未使用分层时间轮，而是复用同一个时间轮
 * 在每个槽上有轮数来标识，现在是走到的第几圈
 * 支持的最小单位是秒
 */
class TimeWheel(
    val slotNum: Int = DEFAULT_SLOT_NUM,               // 一圈的槽数
    val milliSecondsPerSlot: Int = DEFAULT_TIME_PER_SLOT    // 一个槽所代表的时间,单位是ms
) {
    private val tickTimer: TickTimer

    // 现在走到的指针
    @Volatile
    private var point = 0

    // 轮数, 每走过一圈, 轮数自增
    @Volatile
    private var round: Long = 0

    // 执行回调逻辑的线程池
    private val executor: Executor
    private val slotList: ArrayList<Slot<ScheduledEvent>>

    // 任务开始时间
    private var startTime: LocalDateTime? = null

    // 任务开始计数
    private var startCnt: Long

    // 任务收集队列
    @Volatile
    private var collectQueue: ConcurrentLinkedQueue<EventDescriptor>

    // 单次任务收集的最大值, 这里可以取 单个slot时间(ms)的20倍左右
    private val SINGLE_ROUND_COLLECTION_MAXIMUM: Int

    init {
        tickTimer = BlockingQueueTimer(milliSecondsPerSlot.toLong())
        startCnt = 0
        slotList = ArrayList()
        for (i in 0 until slotNum) {
            slotList.add(Slot.emptySlot(i))
        }
        executor = Executors.newFixedThreadPool(20)
        collectQueue = ConcurrentLinkedQueue()
        // 取 单个slot时间(ms)的50倍
        SINGLE_ROUND_COLLECTION_MAXIMUM = this.milliSecondsPerSlot * 20
    }

    private fun tick() {
        // 计时一次
        // logger.debug("once");
        tickTimer.once()
        // logger.warn("timing ")
        val nowSlot: Slot<*> = slotList[point]
        val tarRound = round
        val nowPoint = point
        executor.execute { nowSlot.pollEvent(tarRound) }
        point++
        if (point >= slotNum) {
            point %= slotNum
            // long都溢出了, 这程序得跑到人类灭亡把...
            round++
        }
    }

    private fun collect() {
        // 由于任务被加进公共队列到真正被取出来加入时间轮，这中间有误差
        // 在这里做一个补偿机制
        val baseMillis = System.currentTimeMillis()
//        for (i in 0 until SINGLE_ROUND_COLLECTION_MAXIMUM) {
//            val descriptor = collectQueue.poll() ?: break
//            addEvent0(descriptor.getEvent(), descriptor.msLater, descriptor.addedTime, baseMillis)
//        }
    }

    fun start() {
        startCnt++
        startTime = LocalDateTime.now()

        // 计时线程
        Thread {
            while (true) {
                // 收集和计时交替进行
                tick()
                collect()
            }
        }.start()
    }

    /**
     * 在millisLater毫秒之后进行任务
     *
     * @param event
     * @param millisLater
     */
    private fun addEvent0(event: ScheduledEvent, millisLater: Long, missionStartMillis: Long, baseMillis: Long) {
        val deltaSlotIndex = (millisLater - (baseMillis - missionStartMillis)) / milliSecondsPerSlot
        var nextIndex = point + deltaSlotIndex.toInt()
        var tarRound = round
        if (nextIndex >= slotNum) {
            nextIndex -= slotNum
            tarRound++
        }
        val tarSlot = slotList[nextIndex]
        tarSlot.addEvent(tarRound, event)
    }

    /**
     * 对外暴露的添加时间方法
     * 把事件包装成 [EventDescriptor] 后, 加入队列, 等待消费
     *
     * @param event
     * @param millisLater
     */
    fun addEvent(event: ScheduledEvent?, millisLater: Long) {
        val eventDescriptor = EventDescriptor(event!!, millisLater)
        collectQueue.add(eventDescriptor)
    }

    /**
     * 槽位的类定义
     *
     * 不使用分层策略, 而是复用这一层.
     * 每个槽会维护一个 Map<round></round>, List<Event>> 的数据结构
     */
    class Slot<Event : ScheduledEvent>(
        @field:Volatile private var nowRound: Int,  // 现在这一槽位所处于的轮数
        @field:Volatile private var eventMap: HashMap<Long, ArrayList<Event>>,
        private val index: Int   // 这个slot所在的下标
    ) {

        fun addEvent(tarRound: Long, event: Event) {

            // 更新任务
            var eventList = eventMap.getOrDefault(tarRound, null)
            if (eventList == null) {
                eventList = ArrayList()
                eventMap[tarRound] = eventList
            }
            eventList.add(event)
            event.startTimingCallback()
        }

        // 循环指定round的任务, 进行回调
        fun pollEvent(tarRound: Long) {
            val eventList = eventMap.getOrDefault(tarRound, null)
                ?: return
            for (event in eventList) {
                event.timeoutCallback()
            }

            // remove the element, help gc
            eventMap.remove(tarRound)
        }

        companion object {
            fun emptySlot(index: Int): Slot<ScheduledEvent> {
                return Slot(0, HashMap(), index)
            }
        }
    }
}

private const val DEFAULT_SLOT_NUM = 200

/**
 * 这个建议值:
 * "在x时间后要执行的任务" 这种描述形式, 那么取这些任务中 x的最小值的 1/20
 */
private const val DEFAULT_TIME_PER_SLOT = 50