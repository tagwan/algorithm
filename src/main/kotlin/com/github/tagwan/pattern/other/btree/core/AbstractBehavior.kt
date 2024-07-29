package com.github.tagwan.pattern.other.btree.core

import com.github.tagwan.pattern.other.btree.EStatus


/**
 * 所有节点基类
 *
 * @data 2022/5/17 16:20
 */
abstract class AbstractBehavior : IBehaviour {

    override var status: EStatus = EStatus.Invalid


    /**
     * 包装函数，防止打破调用契约
     * <p>
     *     update方法被首次调用前执行OnInitlize方法，每次行为树更新时调用一次update方法，
     *     当刚刚更新的行为不再运行时调用OnTerminate方法
     *
     * @return
     */
    override fun tick(): EStatus {
        if (this.status != EStatus.Running) {
            this.onInitialize()
        }
        this.status = this.update()
        if (this.status != EStatus.Running) {
            this.onTerminate(status)
        }
        return this.status
    }

    override fun release() {
        // pass
    }


    override fun onInitialize() {
        // pass
    }

    override fun onTerminate(Status: EStatus) {
        // pass
    }

    override fun reset() {
        this.status = EStatus.Invalid
    }

    override fun abort() {
        this.onTerminate(EStatus.Aborted)
        this.status = EStatus.Aborted
    }
}
