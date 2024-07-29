package com.github.tagwan.pattern.other.btree.decorator.impl

import com.github.tagwan.pattern.other.btree.EStatus
import com.github.tagwan.pattern.other.btree.core.IBehaviour
import com.github.tagwan.pattern.other.btree.decorator.AbstractDecorator
import com.github.tagwan.pattern.other.btree.decorator.IDecorator

/**
 * 重复节点
 *
 * @data 2022/5/17 17:15
 */
class Repeat(
    child: IBehaviour,
    private val limited: Int = 3,
) : AbstractDecorator(child), IDecorator {

    @Volatile
    private var count = 0

    override fun update(): EStatus {
        while (true) {
            child.tick()
            when (child.status) {
                EStatus.Running ->
                    return EStatus.Success
                EStatus.Failure ->
                    return EStatus.Failure
                else -> {}
            }
            if (++count > limited)
                return EStatus.Success
            child.reset()
        }
    }

    override fun onInitialize() {
        count = 0
    }
}