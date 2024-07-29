package com.github.tagwan.structure

import java.util.*


/**
 * Trie树（前缀树、字典树）
 *
 * @author ronglexie
 * @version 2018/9/1
 */
class Trie {
    private var root: Node?
    var size: Int
        private set

    init {
        root = Node()
        size = 0
    }

    /**
     * 添加一个单词
     *
     * @param word
     * @return void
     * @author ronglexie
     * @version 2018/9/1
     */
    fun add(word: String) {
        var cur = root
        for (i in 0 until word.length) {
            val c = word[i]
            if (cur!!.next[c] == null) {
                cur.next[c] = Node()
            }
            cur = cur.next[c]
        }
        if (!cur!!.isWord) {
            cur.isWord = true
            size++
        }
    }

    /**
     * 递归添加一个单词
     *
     * @param word
     * @return void
     * @author ronglexie
     * @version 2018/9/1
     */
    fun addRecursion(word: String) {
        root = addRecursion(root, word)
    }

    /**
     * 在node节点添加一个单词
     *
     * @param node
     * @param word
     * @return Trie.Node
     * @author ronglexie
     * @version 2018/9/1
     */
    private fun addRecursion(node: Node?, word: String): Node? {
        if (node == null) {
            return null
        }
        if (word.length > 0) {
            if (node.next[word[0]] == null) {
                node.next[word[0]] = Node()
            }
            node.next[word[0]] = addRecursion(node.next[word[0]], word.substring(1))
            if (word.length == 1 && !node.next[word[0]]!!.isWord) {
                node.next[word[0]]!!.isWord = true
                size++
            }
        }
        return node
    }

    /**
     * 是否包含某个单词
     *
     * @param word
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    operator fun contains(word: String): Boolean {
        var cur = root
        for (i in 0 until word.length) {
            val c = word[i]
            if (cur!!.next[c] == null) {
                return false
            }
            cur = cur.next[c]
        }
        return cur!!.isWord
    }

    /**
     * 递归查询是否包含某个单词
     *
     * @param word
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    fun containsRecurison(word: String): Boolean {
        return containsRecurison(root, word)
    }

    /**
     * 递归查询某个节点中是否包含某个单词
     *
     * @param node
     * @param word
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    private fun containsRecurison(node: Node?, word: String): Boolean {
        if (node == null) {
            return false
        }
        if (word.length > 0) {
            if (node.next[word[0]] == null) {
                return false
            } else if (word.length == 1 && node.next[word[0]]!!.isWord) {
                return true
            }
            return containsRecurison(node.next[word[0]], word.substring(1))
        }
        return false
    }

    /**
     * 是否包含此前缀的单词
     *
     * @param prefix
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    private fun isPrefix(prefix: String): Boolean {
        var cur = root
        for (i in 0 until prefix.length) {
            val c = prefix[i]
            if (cur!!.next[c] == null) {
                return false
            }
            cur = cur.next[c]
        }
        return true
    }

    /**
     * 递归查询是否包含此前缀的单词
     *
     * @param word
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    fun isPrefixRecurison(word: String): Boolean {
        return isPrefixRecurison(root, word)
    }

    /**
     * 递归查询是否包含此前缀的单词
     *
     * @param node
     * @param word
     * @return boolean
     * @author ronglexie
     * @version 2018/9/1
     */
    private fun isPrefixRecurison(node: Node?, word: String): Boolean {
        if (node == null) {
            return false
        }
        if (word.length > 0) {
            if (node.next[word[0]] == null) {
                return false
            } else if (word.length == 1) {
                return true
            }
            return isPrefixRecurison(node.next[word[0]], word.substring(1))
        }
        return false
    }

    /**
     * 节点类
     *
     * @author ronglexie
     * @version 2018/9/1
     */
    private inner class Node @JvmOverloads constructor(var isWord: Boolean = false) {
        var next: TreeMap<Char, Node?>

        init {
            next = TreeMap()
        }
    }
}