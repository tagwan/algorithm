package com.github.tagwan.algorithm.sort

fun <T> Array<T>.exch(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}