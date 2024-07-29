package com.github.tagwan.structure.binarytree

/**
 * BinaryTree
 *
 * @data 2022/5/18 14:26
 */
class BinaryTree() {
    var root: TreeNode ?= null

    // 前序遍历
    fun frontShow() {
        root?.frontShow()
    }

    // 中序遍历
    fun midShow() {
        root?.midShow()
    }

    // 后序遍历
    fun afterShow() {
        root?.afterShow()
    }

    // 前序查找
    fun frontSearch(element: Int): TreeNode? {
        return root?.frontSearch(element)
    }

    // 删除子树
    fun delete(i: Int) {
        if (root?.value == i) {

        }else {
            root?.delete(i)
        }
    }
}