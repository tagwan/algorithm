package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.component.IComponent
import com.github.tagwan.ecs.entity.Entity
import com.github.tagwan.ecs.event.EventArgs

/**
 * ComponentReplacedArgs
 *
 * @data 2022/9/20 20:14
 */
class ComponentReplacedArgs(
    val entity: Entity,
    val index: Int,
    val previous: IComponent,
    val replacement: IComponent?
) : EventArgs {
}