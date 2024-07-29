package com.github.tagwan.pattern.other.btree.composite.impl

import com.github.tagwan.pattern.other.btree.EStatus
import com.github.tagwan.pattern.other.btree.composite.AbstractComposite
import com.github.tagwan.pattern.other.btree.composite.IComposite


/**
 * 顺序节点
 * <p>
 *     顺序器：依次执行所有节点直到其中一个失败或者全部成功位置
 *
 * @data 2022/5/17 17:12
 */
class Sequence: AbstractComposite(), IComposite {

    override fun update(): EStatus {
        val iterator = this.children.iterator()
        if (iterator.hasNext()) {
            while (true) {
                val currChild = iterator.next()
                val s: EStatus = currChild.tick()

                //如果执行成功了就继续执行，否则返回
                if (s !== EStatus.Success)
                    return s

                if (!iterator.hasNext())
                    return EStatus.Success
            }
        }

        // 循环意外终止
        return EStatus.Invalid
    }
}