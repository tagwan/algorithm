package com.github.tagwan.pattern.other.btree.composite.impl

import com.github.tagwan.pattern.other.btree.EPolicy
import com.github.tagwan.pattern.other.btree.EStatus
import com.github.tagwan.pattern.other.btree.composite.AbstractComposite
import com.github.tagwan.pattern.other.btree.composite.IComposite


/**
 * 并行节点
 * <p>
 *     并行器：多个行为并行执行
 *
 * @data 2022/5/17 17:01
 */
class Parallel(
    private var successPolicy: EPolicy,
    private var failPolicy: EPolicy
) : AbstractComposite(), IComposite {

    override fun update(): EStatus {
        var successCount = 0
        var failureCount = 0
        val childrenSize: Int = this.children.size
        for (iBehaviour in this.children) {

            //如果行为已经终止则不再执行该行为
            if (!(iBehaviour.status == EStatus.Success || iBehaviour.status == EStatus.Failure)) {
                iBehaviour.tick()
            }

            if (iBehaviour.status == EStatus.Success) {
                ++successCount
                if (successPolicy == EPolicy.RequireOne) {
                    iBehaviour.reset()
                    return EStatus.Success
                }
            }
            if (iBehaviour.status == EStatus.Failure) {
                ++failureCount
                if (failPolicy == EPolicy.RequireOne) {
                    iBehaviour.reset()
                    return EStatus.Failure
                }
            }
        }

        if (failPolicy == EPolicy.RequireAll && failureCount == childrenSize) {
            for (iBehaviour in this.children) {
                iBehaviour.reset()
            }
            return EStatus.Failure
        }
        if (successPolicy == EPolicy.RequireAll && successCount == childrenSize) {
            for (iBehaviour in this.children) {
                iBehaviour.reset()
            }
            return EStatus.Success
        }

        return EStatus.Running
    }

    override fun onTerminate(Status: EStatus) {
        for (iBehaviour in this.children) {
            if (iBehaviour.status == EStatus.Running) {
                iBehaviour.abort()
            }
            iBehaviour.reset()
        }
    }
}