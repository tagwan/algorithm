package com.github.tagwan.pattern.other.btree.composite

import com.github.tagwan.pattern.other.btree.core.AbstractBehavior
import com.github.tagwan.pattern.other.btree.core.IBehaviour

/**
 * 组合结点
 *
 * @data 2022/5/17 16:48
 */
abstract class AbstractComposite : AbstractBehavior(), IComposite {

    override var children: ArrayList<IBehaviour> = ArrayList()

    override fun addChild(child: IBehaviour) {
        children.add(child)
    }
}
