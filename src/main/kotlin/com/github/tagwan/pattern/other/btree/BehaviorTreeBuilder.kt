package com.github.tagwan.pattern.other.btree

import com.github.tagwan.pattern.other.btree.core.IBehaviour
import java.util.*


/**
 * 行为树构建器
 *
 * @data 2022/5/17 16:58
 */
class BehaviorTreeBuilder {

    private val behaviourStack = Stack<IBehaviour>()
    private var treeRoot: IBehaviour? = null

    /**
     * 添加节点
     * <P>
     *     如果没有根节点设置新节点为根节点，
     *     否则设置新节点为堆栈顶部节点的子节点
     *
     * @param behaviour
     * @return
     */
    fun addBehaviour(behaviour: IBehaviour): BehaviorTreeBuilder {
        if (treeRoot == null) {
            treeRoot = behaviour
        } else {
            behaviourStack.peek().addChild(behaviour)
        }

        // 将新节点压入堆栈
        behaviourStack.push(behaviour)
        return this
    }

    fun back(): BehaviorTreeBuilder {
        behaviourStack.pop()
        return this
    }

    fun end(): BehaviorTree {
        while (behaviourStack.isNotEmpty()) {
            behaviourStack.pop()
        }
        return BehaviorTree(treeRoot)
    }
}