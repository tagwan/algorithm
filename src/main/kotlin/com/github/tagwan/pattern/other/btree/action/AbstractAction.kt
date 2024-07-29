package com.github.tagwan.pattern.other.btree.action

import com.github.tagwan.pattern.other.btree.core.AbstractBehavior
import com.github.tagwan.pattern.other.btree.core.IBehaviour

/**
 * 动作节点基类
 *
 * @data 2022/5/17 16:50
 */
abstract class AbstractAction : AbstractBehavior(), IAction {

    override fun addChild(child: IBehaviour) {
        throw IllegalArgumentException("node::${child.javaClass.simpleName} 不支持添加到节点 ${this.javaClass.simpleName}上")
    }

}