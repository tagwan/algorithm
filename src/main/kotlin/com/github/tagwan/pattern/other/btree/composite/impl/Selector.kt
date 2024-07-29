package com.github.tagwan.pattern.other.btree.composite.impl

import com.github.tagwan.pattern.other.btree.EStatus
import com.github.tagwan.pattern.other.btree.composite.AbstractComposite
import com.github.tagwan.pattern.other.btree.composite.IComposite


/**
 * 选择节点
 * <p>
 *     选择器:依次执行每个子节点直到其中一个执行成功或者返回Running状态
 *
 * @data 2022/5/17 17:08
 */
class Selector : AbstractComposite(), IComposite {

    override fun update(): EStatus {
        val iterator = this.children.iterator()
        if (iterator.hasNext()) {
            while (true) {
                val currChild = iterator.next()
                val s: EStatus = currChild.tick()

                // 如果执行成功了就继续执行，否则返回
                if (s !== EStatus.Failure)
                    return s

                if (!iterator.hasNext())
                    return EStatus.Failure
            }
        }

        // 循环意外终止
        return EStatus.Invalid
    }
}