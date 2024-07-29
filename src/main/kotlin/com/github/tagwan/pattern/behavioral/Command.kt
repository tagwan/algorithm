package com.github.tagwan.pattern.behavioral

interface ReceiveCommand {
    fun execute()
}

class ResourceCostCommand : ReceiveCommand {
    override fun execute() = println("扣除资源")
}

class RewardAddCommand : ReceiveCommand {
    override fun execute() = println("添加奖励")
}

class CacheUpdateCommand : ReceiveCommand {
    override fun execute() = println("更新缓存")
}

class RedPointCommand : ReceiveCommand {
    override fun execute() = println("推送红点")
}

class CommandProcessor {

    private val queue = ArrayList<ReceiveCommand>()

    fun addToQueue(orderCommand: ReceiveCommand): CommandProcessor =
        apply {
            queue.add(orderCommand)
        }

    fun processCommands(): CommandProcessor =
        apply {
            queue.forEach { it.execute() }
            queue.clear()
        }
}

/**
 * 模拟一次领奖过程
 * <p>
 *     前置检测也可以写一个
 *
 */
fun main() {
    CommandProcessor()
        .addToQueue(ResourceCostCommand())
        .addToQueue(RewardAddCommand())
        .addToQueue(CacheUpdateCommand())
        .addToQueue(RedPointCommand())
        .processCommands()
}