package com.github.tagwan.math

import java.util.*

fun <K> HashMap<K, Int>.plusValue(k: K, v: Int) {
    this[k] = this.getOrDefault(k, 0) + v
}

fun <K> HashMap<K, Long>.plusValue(k: K, v: Long) {
    this[k] = this.getOrDefault(k, 0) + v
}

fun <K> HashMap<K, Int>.minusValue(k: K, v: Int) {
    this[k] = this.getOrDefault(k, 0) - v
}

fun <K> HashMap<K, Long>.minusValue(k: K, v: Long) {
    this[k] = this.getOrDefault(k, 0) - v
}

fun <K, V> TreeMap<K, V>.lastValue(): V? {
    val entry = this.lastEntry() ?: return null
    return entry.value
}

fun <T> List<T>.tail(): List<T> = this.drop(1)

fun <T> List<T>.destructured(): Pair<T, List<T>> = first() to tail()