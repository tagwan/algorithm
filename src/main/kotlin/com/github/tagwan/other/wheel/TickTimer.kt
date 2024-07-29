package com.github.tagwan.other.wheel

/**
 * tickTimer接口
 * 定义时间轮上的一格时间的模拟
 *
 * @data 2022/5/19 10:05
 */
interface TickTimer {
    /**
     * 进行一次计时
     */
    fun once()

    /**
     * 获取一次计时的间隔
     */
    fun getInterval(): Long
}