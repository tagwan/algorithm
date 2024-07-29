package com.github.tagwan.structure

import kotlin.experimental.inv

/**
 * Bitmap
 *
 * todo 动态扩容
 * @data 2022/5/24 22:34
 */
class Bitmap(
    val length: Int,
    val bitmap: ByteArray = ByteArray(length ushr 3)
) {

    init {
        require(length >= 0) { "length < 0: $length" }
    }

    operator fun get(number: Int): Boolean {
        // 获取位置
        val site = number ushr 3 //等价于 site=number/8

        // 获取该字节
        val temp = bitmap[site]

        // 获取该字节的第几个
        val i = number and 7 //等价于 i=number%8
        val comp: Byte = 1

        // 将这个字节数右移(7-i)位（也就是把要查找的位移动到最右侧），然后与 0000 0001相与
        return (temp.toInt() ushr (7 - i) and 1) != 0
    }

    private operator fun set(number: Int, bool: Boolean) {
        // 获取位置
        val site = number ushr 3 //等价于 site=number/8
        // 获取该字节
        val temp = bitmap[site]

        // 获取该字节的第几个
        val i = number and 7 //等价于 i=number%8

        // 将0000 0001 左移(7-i)
        var comp = (1 shl (7 - i)).toByte()
        if (bool) { // 设置为1
            bitmap[site] = (comp.toInt() or temp.toInt()).toByte() //取或(0.. 1 0..)
        } else { // 设置为0
            comp = comp.inv() //取反
            bitmap[site] = (comp.toInt() and temp.toInt()).toByte() //相与(1.. 0 1..)
        }
    }

    fun add(index: Int) {
        set(index, true)
    }

    fun delete(index: Int) {
        set(index, false)
    }
}

fun main(args: Array<String>) {
    val bitmap = Bitmap(100000)
    bitmap.add(100)
    println(bitmap[100])
    bitmap.delete(100)
    println(bitmap[100])
}