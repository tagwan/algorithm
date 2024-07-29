package com.github.tagwan.algorithm.graphs

/**
 * Graph
 *
 * @data 2022/5/18 14:51
 */
interface Graph {
    public val V: Int
    public var E: Int
    public fun adjacentVertices(from: Int): Collection<Int>

    public fun vertices(): IntRange = 0 until V
}