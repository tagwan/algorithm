package com.github.tagwan.other.pool

import java.util.*


abstract class ObjectPool<T> {

    private var objects: Vector<PooledObject<T>>? = null // 存放对象池中对象的向量(PooledObject类型)

    /**
     * 创建一个对象池
     *
     */
    @Synchronized
    fun createPool() {

        // 确保对象池没有创建。如果创建了，保存对象的向量 objects 不会为空
        if (objects != null) {
            return  // 如果己经创建，则返回
        }
        // 创建保存对象的向量 , 初始时有 0 个元素
        objects = Vector<PooledObject<T>>()
        for (i in 0 until numObjects) {
            objects?.addElement(create())
        }
    }

    abstract fun create(): PooledObject<T>// 重新再试，直到获得可用的对象，如果

    @Synchronized
    open fun getObject(): T? {
        // 确保对象池己被创建
        if (objects == null) {
            return null // 对象池还没创建，则返回 null
        }
        var t: T? = getFreeObject() // 获得一个可用的对象
        // 如果目前没有可以使用的对象，即所有的对象都在使用中
        while (t == null) {
            wait(250)
            t = getFreeObject() // 重新再试，直到获得可用的对象，如果返回的为 null，则表明创建一批对象后也不可获得可用对象
        }
        // 返回获得的可用的对象
        return t
    }

    /**
     * 本函数从对象池对象 objects 中返回一个可用的的对象，如果 当前没有可用的对象，则创建几个对象，并放入对象池中。
     * 如果创建后，所有的对象都在使用中，则返回 null
     */
    private fun getFreeObject(): T? {
        // 从对象池中获得一个可用的对象
        var obj = findFreeObject()
        if (obj == null) {
            createObjects(10) // 如果目前对象池中没有可用的对象，创建一些对象
            // 重新从池中查找是否有可用对象
            obj = findFreeObject()
            // 如果创建对象后仍获得不到可用的对象，则返回 null
            if (obj == null) {
                return null
            }
        }
        return obj
    }

    open fun createObjects(increment: Int) {
        for (i in 0 until increment) {
            if ( objects!!.size > maxObjects) {
                return
            }
            objects!!.addElement(create())
        }
    }

    /**
     * 查找对象池中所有的对象，查找一个可用的对象， 如果没有可用的对象，返回 null
     */
    private fun findFreeObject(): T? {
        var obj: T? = null
        var pObj: PooledObject<T>? = null
        // 获得对象池向量中所有的对象
        val enumerate: Enumeration<PooledObject<T>> = objects!!.elements()
        // 遍历所有的对象，看是否有可用的对象
        while (enumerate.hasMoreElements()) {
            pObj = enumerate.nextElement() as PooledObject<T>

            // 如果此对象不忙，则获得它的对象并把它设为忙
            if (!pObj.busy) {
                obj = pObj.objection
                pObj.busy = true
            }
        }

        // 返回找到到的可用对象
        return obj
    }

    /**
     * 此函数返回一个对象到对象池中，并把此对象置为空闲。 所有使用对象池获得的对象均应在不使用此对象时返回它。
     */
    fun returnObject(obj: T?) {

        // 确保对象池存在，如果对象没有创建（不存在），直接返回
        if (objects == null || obj == null) {
            return
        }
        var pObj: PooledObject<T>? = null
        val enumerate: Enumeration<PooledObject<T>> = objects!!.elements()

        // 遍历对象池中的所有对象，找到这个要返回的对象对象
        while (enumerate.hasMoreElements()) {
            pObj = enumerate.nextElement() as PooledObject<T>

            // 先找到对象池中的要返回的对象对象
            if (obj === pObj.objection) {

                // 找到了 , 设置此对象为空闲状态
                pObj.busy = false
                break
            }
        }
    }

    /**
     * 关闭对象池中所有的对象，并清空对象池。
     */
    @Synchronized
    fun closeObjectPool() {
        // 确保对象池存在，如果不存在，返回
        if (objects == null) {
            return
        }
        var pObj: PooledObject<T>? = null
        val enumerate: Enumeration<PooledObject<T>> = objects!!.elements()
        while (enumerate.hasMoreElements()) {
            pObj = enumerate.nextElement() as PooledObject<T>
            // 如果忙，等 0.5 秒
            if (pObj.busy) {
                wait(500) // 等
            }
            // 从对象池向量中删除它
            objects!!.removeElement(pObj)
        }
        // 置对象池为空
        objects = null
    }

    /**
     * 使程序等待给定的毫秒数
     */
    private fun wait(mSeconds: Int) {
        try {
            Thread.sleep(mSeconds.toLong())
        } catch (e: InterruptedException) {
        }
    }

    companion object {
        var numObjects = 10 // 对象池的大小
        var maxObjects = 50 // 对象池最大的大小
    }
}