package com.github.tagwan.pattern.other.btree.condition

import com.github.tagwan.pattern.other.btree.core.IBehaviour

/**
 * 条件基类
 *
 */
interface ICondition: IBehaviour {
    var negation: Boolean
}