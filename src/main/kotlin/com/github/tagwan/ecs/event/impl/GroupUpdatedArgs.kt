package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.Group
import com.github.tagwan.ecs.component.IComponent
import com.github.tagwan.ecs.entity.Entity
import com.github.tagwan.ecs.event.EventArgs

/**
 * GroupUpdatedArgs
 *
 * @data 2022/9/20 20:15
 */
class GroupUpdatedArgs(
    val group: Group, val entity: Entity, val index: Int, val prevComponent: IComponent,
    val newComponent: IComponent?
) : EventArgs