package com.github.tagwan.structure

/**
 * 并查集
 * <p>
 *     用于解决连通性问题
 *     并查集用于判断连个点所在的集合是否属于同一个集合，若属于同一个集合但还未合并则将两个集合进行合并。
 *     同一个集合的意思是这两个点是连通的，直接相连或者通过其它点连通。
 */
class UnionFind(n: Int) {
    // parent 数组存储每个元素所属的集合的代表元素（根节点）。初始时,每个元素都是自己的代表元素。
    private val parent = IntArray(n) { it }
    // rank 数组存储每个集合的"秩"(rank),用于在合并时保持树的平衡, 提高查找效率。
    private val rank = IntArray(n)

    init {
        for (i in 0 until n) {
            this.parent[i] = i
            this.rank[i] = 0
        }
    }

    /**
     * 查找元素 x 所属的集合（连通分量）
     * <p>
     *     在查找过程中,会将沿途的节点直接指向根节点,以减少后续查找的时间复杂度。
     *
     * @param x Int
     * @return Int
     */
    fun find(x: Int): Int {
        if (parent[x] != x) {
            parent[x] = this.find(parent[x])
        }
        return parent[x]
    }

    /**
     * 将元素 x 和 y 所属的两个集合合并
     * <p>
     *     它首先找到 x 和 y 各自的根节点,然后比较两个集合的"秩"。如果 x 的"秩"小于 y 的"秩",
     *     则将 x 的根节点设为 y 的根节点;反之则将 y 的根节点设为 x 的根节点。
     *     如果两个集合的"秩"相同,则将 y 的根节点设为 x 的根节点,并将 x 的"秩"加 1。
     *
     * @param x Int
     * @param y Int
     */
    fun union(x: Int, y: Int): Boolean {
        val xRoot = this.find(x)
        val yRoot = this.find(y)

        if (xRoot == yRoot) {
            return false
        }

        if (this.rank[xRoot] < this.rank[yRoot]) {
            this.parent[xRoot] = yRoot
        } else if (this.rank[xRoot] > this.rank[yRoot]) {
            this.parent[yRoot] = xRoot
        } else {
            this.parent[yRoot] = xRoot
            this.rank[xRoot]++
        }

        return true
    }

    //判断两个元素是否在一个集合中
    fun isConnected(a: Int, b: Int): Boolean {
        return this.find(a) == this.find(b)
    }
}