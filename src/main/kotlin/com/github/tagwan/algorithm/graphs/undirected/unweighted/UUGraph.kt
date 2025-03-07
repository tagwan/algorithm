package com.github.tagwan.algorithm.graphs.undirected.unweighted

import com.github.tagwan.algorithm.graphs.Graph
import com.github.tagwan.structure.Queue

class UUGraph(public override val V: Int): Graph {
    public override var E: Int = 0
    private val adj: Array<Queue<Int>> = Array(V) { Queue<Int>() }

    public fun addEdge(v: Int, w: Int) {
        adj[v].add(w)
        adj[w].add(v)
        E++
    }

    public override fun adjacentVertices(v: Int): Collection<Int> {
        return adj[v]
    }

    public fun degree(v: Int): Int {
        return adj[v].size
    }
}