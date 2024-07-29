package com.github.tagwan.pattern.behavioral

// Chain of Responsibility

// 抽奖责任链基类
interface LuckDrawChain {
    fun execute(request: String): String?
}

// 单次
class OnceLuckDraw(val token: String?, var next: LuckDrawChain? = null): LuckDrawChain {

    override fun execute(request: String): String? {
        token ?: throw IllegalStateException("Token should be not null")

        // 单次抽奖达成什么条件发送什么奖励?

        return next?.execute(request)
    }
}

// 五次
class FiveLuckDraw(val token: String?, var next: LuckDrawChain? = null): LuckDrawChain {

    override fun execute(request: String): String? {
        token ?: throw IllegalStateException("Token should be not null")

        // 五次抽奖达成什么条件发送什么奖励?

        return next?.execute(request)
    }
}


// 十次
class TenLuckDraw(val token: String?, var next: LuckDrawChain? = null): LuckDrawChain {

    override fun execute(request: String): String? {
        token ?: throw IllegalStateException("Token should be not null")

        // 十连抽达成什么条件发送什么奖励?

        return next?.execute(request)
    }
}

/**
 * Chain of Responsibility
 * 责任链模式
 *
 */
fun main() {
    val token = "token"
    val request = ""
    // create chain elements, construct chain
    val dealChain = OnceLuckDraw(token, FiveLuckDraw(token, TenLuckDraw(token)))
    // execute chain
    dealChain.execute(request)
}