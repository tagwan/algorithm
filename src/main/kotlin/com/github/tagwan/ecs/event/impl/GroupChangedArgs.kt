package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.Group
import com.github.tagwan.ecs.component.IComponent
import com.github.tagwan.ecs.entity.Entity
import com.github.tagwan.ecs.event.EventArgs

/**
 * GroupChangedArgs
 *
 * @data 2022/9/20 20:14
 */
class GroupChangedArgs(
    val group: Group,
    val entity: Entity,
    val index: Int,
    val newComponent: IComponent?
) : EventArgs
