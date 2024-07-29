package com.github.tagwan.juc

/**
 * SynchronizedReentrant
 *
 * <p>
 *     可重入锁：
 *     当线程获取某个锁后，还可以继续获取它，可以递归调用，而不会发生死锁；【ReentrantLock和synchronized】
 * <p>
 *     不可重入锁：
 *     与可重入相反，获取锁后不能重复获取，否则会死锁（自己锁自己）。 【StampedLock】
 *
 * @data 2022/5/26 13:32
 */
class SynchronizedReentrant : Runnable {
    private val obj = Any()

    /**
     * 方法1，调用方法2
     */
    fun method1() {
        synchronized(obj) {
            println(Thread.currentThread().name + " method1()")
            method2()
        }
    }

    /**
     * 方法2，打印前获取 obj 锁
     * 如果同一线程，锁不可重入的话，method2 需要等待 method1 释放 obj 锁
     */
    fun method2() {
        synchronized(obj) { println(Thread.currentThread().name + " method2()") }
    }

    override fun run() {
        //线程启动 执行方法1
        method1()
    }
}