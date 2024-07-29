package com.github.tagwan.ecs.system

import com.github.tagwan.ecs.event.impl.TriggerOnEvent


/**
 * IMultiReactiveSystem
 *
 * @data 2022/9/20 19:56
 */
interface IMultiReactiveSystem : IReactiveExecuteSystem {
    val triggers:Array<TriggerOnEvent>
}