package com.github.tagwan.structure.binarytree

/**
 * TreeNode
 *
 * @data 2022/5/18 14:30
 */
class TreeNode(var value: Int) {
    var left: TreeNode ?= null
    var right: TreeNode ?= null

    // 前序遍历
    fun frontShow() {
        print("$value ")
        left?.frontShow()
        right?.frontShow()
    }

    // 中序遍历
    fun midShow() {
        left?.midShow()
        print("$value ")
        right?.midShow()
    }

    // 后序遍历
    fun afterShow() {
        left?.afterShow()
        right?.afterShow()
        print("$value ")
    }


    fun frontSearch(element: Int): TreeNode? {
        var target: TreeNode? = null
        if (element == this.value) {
            return this
        } else {
            if (left != null) {
                target = left!!.frontSearch(element)
            }
            if (target != null) {
                return target
            }
            if (right != null) {
                target = right!!.frontSearch(element)
            }
            return target
        }
    }


    fun delete(i: Int) {
        // 判断左子树
        if (this.left?.value == i) {
            this.left = null
        }
        // 判断右子树
        if (this.right?.value == i) {
            this.right = null
        }
        // 递归检查并删除左子树
        if (left != null) {
            left?.delete(i)
        }
        // 递归检查并删除右子树
        if (right != null) {
            right?.delete(i)
        }
    }
}