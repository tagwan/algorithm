package com.github.kt

import kotlin.reflect.KProperty

fun main(args: Array<String>) {
    println(Main.show1)
    println(Main.show2)
    println(Main.show1)
}

enum class RedPoint {
    COMMON,
    TEST,
    ;
}

class RedPointDelegate(
    val type: RedPoint
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return type.name
    }
}

object Main {
    val show1: String by RedPointDelegate(RedPoint.COMMON)
    val show2: String by RedPointDelegate(RedPoint.TEST)
}

