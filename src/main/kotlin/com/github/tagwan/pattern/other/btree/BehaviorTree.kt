package com.github.tagwan.pattern.other.btree

import com.github.tagwan.pattern.other.btree.core.IBehaviour

/**
 * 行为树
 *
 * @data 2022/5/17 16:56
 */
class BehaviorTree(private var root: IBehaviour?) {

    fun loop() {
        root?.tick()
    }

    fun haveRoot(): Boolean = root != null

    infix fun root(inNode: IBehaviour) {
        root = inNode
    }

    fun release() {
        root?.release()
    }
}