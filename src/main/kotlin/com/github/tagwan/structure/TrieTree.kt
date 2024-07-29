package com.github.tagwan.structure


import java.util.*

/**
 * 字典树实现
 */
class TrieTree {

    private val root: TrieNode = TrieNode()

    /**
     * 添加一个word
     *
     * @param word 需要添加的词
     */
    fun addWord(word: String) {
        var deep = 0
        var currNode: TrieNode? = root
        while (deep < word.length) {
            /*
             * 判断当前node的child，如果为空直接添加，不为空，查找是否含有，不含有则添加并设为currNode，含有则找到并设置为currNode
             */
            val c = word[deep]
            if (currNode!!.child.contains(TrieNode(c))) {
                currNode = currNode.getNode(c)
            } else {
                val node = TrieNode(c)
                node.preNode = currNode
                node.deep = deep + 1
                currNode.child.add(node)
                currNode = node
            }
            if (deep == word.length - 1) {
                currNode!!.isEnd = true
            }
            deep++
        }
    }

    /**
     * word在map中是否存在
     *
     * @param word 需要查找的word
     * @return 是否存在
     */
    fun hasWord(word: String): Boolean {
        var deep = 0
        var currNode: TrieNode? = root
        while (deep < word.length) {
            val c = word[deep]
            currNode = if (currNode!!.child.contains(TrieNode(c))) {
                currNode.getNode(c)
            } else {
                return false
            }
            if (deep == word.length - 1) {
                return currNode!!.isEnd
            }
            deep++
        }
        return false
    }

    /**
     * 移除word，几种情况：
     * 1、word在list中不存在，直接返回失败
     * 2、word最后一个char 没有child，则删掉此节点并朝 root 查找没有child && isEnd=false 的节点都删掉
     * 3、word最后一个char 有child，则把isEnd置为false
     *
     * @param word 需要移除的word
     * @return 是否移除成功
     */
    fun removeWord(word: String?): Boolean {
        if (word == null || word.trim { it <= ' ' } == "") {
            return false
        }
        if (hasWord(word)) {
            return false
        }
        var deep = 0
        var currNode: TrieNode? = root
        while (deep < word.length) {
            val c = word[deep]
            currNode = if (currNode!!.child.contains(TrieNode(c))) {
                currNode.getNode(c)
            } else {
                return false
            }
            if (deep == word.length - 1) {
                if (currNode!!.child.size > 0) {
                    //3、word最后一个char 有child，则把isEnd置为false
                    currNode.isEnd = false
                    return true
                } else {
                    //2、word最后一个char 没有child，则删掉此节点并朝 root 查找没有child && isEnd=false 的节点都删掉
                    val parent = currNode.preNode
                    while (parent != null) {
                        currNode = if (parent.child.size === 0 && !parent.isEnd) {
                            parent.removeChild(currNode)
                            parent
                        } else {
                            return true
                        }
                    }
                }
            }
            deep++
        }
        return false
    }

    /**
     * 前序遍历所有节点
     */
    fun traverseTree() {
        visitNode(root, "")
    }

    private fun visitNode(node: TrieNode, result: String) {
        val re = result + node.content
        for (n in node.child) {
            visitNode(n, re)
        }
    }
}




class TrieNode(
    var content: Char = 0.toChar()
) {
    var preNode: TrieNode? = null
    var isEnd = false
    var deep = 0 // 做hash使用，防止一个单词里面有多个char的时候hash是一样的，可能导致删除出错
    var child: LinkedList<TrieNode> = LinkedList()


    override fun toString(): String {
        return """
                {End=$isEnd, d=$deep, c=$content, c=$child}
                """.trimIndent()
    }

    override fun hashCode(): Int {
        return content.toInt() + deep
    }

    override fun equals(obj: Any?): Boolean {
        return obj is TrieNode && obj.content == content
    }

    /**
     * child中删掉某个Node
     *
     * @param node 需要删掉的node
     */
    fun removeChild(node: TrieNode?) {
        for (aChild in child) {
            if (aChild.content == node!!.content) {
                child.remove(aChild)
                break
            }
        }
    }

    /**
     * child中是否有此Node
     *
     * @param character 保存的char
     * @return 存在返回不存在返回Null
     */
    fun getNode(character: Char): TrieNode? {
        for (aChild in child) {
            if (aChild.content == character) {
                return aChild
            }
        }
        return null
    }
}