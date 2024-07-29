package com.github.tagwan.algorithm.graphs.directed.unweighted

import com.github.tagwan.algorithm.graphs.Graph
import com.github.tagwan.structure.Queue

/**
 * DUGraph
 *
 * @data 2022/5/18 14:54
 */

class DUGraph(public override val V: Int): Graph {
    override var E: Int = 0
    private val adj: Array<Queue<Int>> = Array(V) { Queue<Int>() }
    private val indegree: IntArray = IntArray(V)

    public fun addEdge(from: Int, to: Int) {
        adj[from].add(to)
        indegree[to]++
        E++
    }

    public override fun adjacentVertices(from: Int): Collection<Int> {
        return adj[from]
    }

    public fun outdegree(from: Int): Int {
        return adj[from].size
    }

    public fun indegree(v: Int): Int {
        return indegree[v]
    }
}