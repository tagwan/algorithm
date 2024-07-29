package com.github.tagwan.ecs.system

import com.github.tagwan.ecs.entity.Entity

/**
 * IReactiveExecuteSystem
 *
 * @data 2022/9/20 19:56
 */
interface IReactiveExecuteSystem : ISystem {
    fun execute(entities:Array<Entity>)
}