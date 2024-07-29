package com.github.tagwan.pattern.other.btree.core

import com.github.tagwan.pattern.other.btree.EStatus

/**
 * 所有节点的基类
 * <p>
 *     在Update方法被首次调用前，调用一次OnInitialize函数，负责初始化等操作,
 *     Update（）方法在行为树每次更新时调用且仅调用一次,
 *     当行为不再处于运行状态时，调用一次OnTerminate（），并根据返回状态不同执行不同的逻辑
 *
 * @data 2022/5/17 16:27
 */
interface IBehaviour {

    var status: EStatus

    /**
     * 设置调用顺序，onInitialize--update--onTerminate
     *
     * @return
     */
    fun tick(): EStatus


    /**
     * 当节点调用前
     *
     */
    fun onInitialize()

    /**
     * 节点操作的具体实现
     *
     * @return
     */
    fun update(): EStatus

    /**
     * 节点调用后执行
     *
     * @param Status
     */
    fun onTerminate(Status: EStatus)

    /**
     * 释放对象所占资源
     *
     */
    fun release()

    /**
     * 添加子节点
     *
     * @param child
     */
    fun addChild(child: IBehaviour)

    /**
     * 重置
     *
     */
    fun reset()

    /**
     * 废弃
     *
     */
    fun abort()
}
