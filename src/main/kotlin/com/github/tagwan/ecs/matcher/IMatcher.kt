package com.github.tagwan.ecs.matcher

import com.github.tagwan.ecs.entity.Entity

/**
 * IMatcher
 *
 * @data 2022/9/20 20:20
 */
interface IMatcher {
    val id:Int
    val indices:IntArray
    fun matches(entity: Entity):Boolean
}