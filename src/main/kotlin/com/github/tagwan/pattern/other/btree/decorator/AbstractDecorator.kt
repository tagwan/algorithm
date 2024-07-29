package com.github.tagwan.pattern.other.btree.decorator

import com.github.tagwan.pattern.other.btree.core.AbstractBehavior
import com.github.tagwan.pattern.other.btree.core.IBehaviour


/**
 * 装饰器
 *
 * @data 2022/5/17 17:14
 */
abstract class AbstractDecorator(
    override var child : IBehaviour
): AbstractBehavior(), IDecorator {

    override fun addChild(child: IBehaviour) {
        this.child = child
    }
}