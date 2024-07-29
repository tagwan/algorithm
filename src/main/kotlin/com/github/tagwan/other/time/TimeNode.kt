package com.github.tagwan.other.time

import java.util.stream.Collectors

/**
 * 任务节点
 *
 * @data 2022/5/19 11:12
 */
class TimeNode(
    var next: TimeNode?,
    var expire: Long
) {

    fun process() {
        // pass
    }
}

data class PlayerVo(
    val user: String,
    val power: Long
)
fun main() {
    val playerList = listOf<PlayerVo>(
        PlayerVo("jdg001", 999),
        PlayerVo("jdg001", 888),
        PlayerVo("jdg002", 888),
    )
    playerList.stream().collect(
        Collectors.toMap(
        { it?.user ?: "" }, // key
        { it } // value
    ) { o1, o2 ->
        if ((o1?.power ?: 0) >= (o2?.power ?: 0)) { // merge
            o1
        } else {
            o2
        }
    }).forEach { t, u ->
        println(u)
    }
}