package com.github.tagwan.pattern.other.btree.condition

import com.github.tagwan.pattern.other.btree.core.AbstractBehavior


/**
 * 条件
 *
 * @data 2022/5/17 16:49
 */
abstract class AbstractCondition : AbstractBehavior(), ICondition {

    override var negation: Boolean = false

    protected fun getRandom(): Int {
        val random = Math.random() * 100
        //    int i = random.intValue();
        return random.toInt()
    }
}
