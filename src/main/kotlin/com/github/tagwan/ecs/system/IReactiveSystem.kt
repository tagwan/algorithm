package com.github.tagwan.ecs.system

import com.github.tagwan.ecs.event.impl.TriggerOnEvent


interface IReactiveSystem : IReactiveExecuteSystem {
    val trigger: TriggerOnEvent
}