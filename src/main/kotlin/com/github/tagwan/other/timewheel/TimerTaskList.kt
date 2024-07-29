package com.github.tagwan.other.timewheel

import java.util.concurrent.Delayed

import java.util.concurrent.TimeUnit

import java.util.concurrent.atomic.AtomicLong
import java.util.function.Consumer
import kotlin.math.max


class TimerTaskList : Delayed {
    /**
     * TimerTaskList 环形链表使用一个虚拟根节点root
     */
    private val root = TimerTaskEntry(null, -1)

    init {
        root.next = root
        root.prev = root
    }

    /**
     * bucket的过期时间
     */
    private val expiration = AtomicLong(-1L)
    fun getExpiration(): Long {
        return expiration.get()
    }

    /**
     * 设置bucket的过期时间,设置成功返回true
     *
     * @param expirationMs
     * @return
     */
    fun setExpiration(expirationMs: Long): Boolean {
        return expiration.getAndSet(expirationMs) != expirationMs
    }

    fun addTask(entry: TimerTaskEntry): Boolean {
        var done = false
        while (!done) {
            //如果TimerTaskEntry已经在别的list中就先移除,同步代码块外面移除,避免死锁,一直到成功为止
            entry.remove()
            synchronized(this) {
                if (entry.timedTaskList == null) {
                    //加到链表的末尾
                    entry.timedTaskList = this
                    val tail = root.prev
                    entry.prev = tail
                    entry.next = root
                    tail!!.next = entry
                    root.prev = entry
                    done = true
                }
            }
        }
        return true
    }

    /**
     * 从 TimedTaskList 移除指定的 timerTaskEntry
     *
     * @param entry
     */
    fun remove(entry: TimerTaskEntry?) {
        synchronized(this) {
            if (entry!!.timedTaskList == this) {
                entry.next!!.prev = entry.prev
                entry.prev!!.next = entry.next
                entry.next = null
                entry.prev = null
                entry.timedTaskList = null
            }
        }
    }

    /**
     * 移除所有
     */
    @Synchronized
    fun clear(entry: Consumer<TimerTaskEntry>) {
        var head = root.next
        while (head != root) {
            remove(head)
            if (head != null)
                entry.accept(head)
            head = root.next
        }
        expiration.set(-1L)
    }

    override fun getDelay(unit: TimeUnit): Long {
        return max(0, unit.convert(expiration.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS))
    }

    override fun compareTo(o: Delayed): Int {
        return if (o is TimerTaskList) {
            expiration.get().compareTo(o.expiration.get())
        } else 0
    }
}