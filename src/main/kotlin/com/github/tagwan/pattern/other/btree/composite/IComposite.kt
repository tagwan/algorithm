package com.github.tagwan.pattern.other.btree.composite

import com.github.tagwan.pattern.other.btree.core.IBehaviour

/**
 * 组合结点
 */
interface IComposite : IBehaviour {
    var children: ArrayList<IBehaviour>
}
