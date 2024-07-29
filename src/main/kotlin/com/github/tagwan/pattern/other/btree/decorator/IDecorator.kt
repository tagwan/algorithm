package com.github.tagwan.pattern.other.btree.decorator

import com.github.tagwan.pattern.other.btree.core.IBehaviour

/**
 * 装饰结点
 */
interface IDecorator : IBehaviour {
    // 装饰器只会有一个子节点
    var child: IBehaviour
}