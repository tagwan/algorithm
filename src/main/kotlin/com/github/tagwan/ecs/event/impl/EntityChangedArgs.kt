package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.component.IComponent
import com.github.tagwan.ecs.entity.Entity
import com.github.tagwan.ecs.event.EventArgs

/**
 * EntityChangedArgs
 *
 * @data 2022/9/20 20:13
 */
class EntityChangedArgs(
    val entity: Entity,
    val index: Int,
    val component: IComponent?
) : EventArgs
