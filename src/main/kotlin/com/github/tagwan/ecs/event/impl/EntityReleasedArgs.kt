package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.entity.Entity
import com.github.tagwan.ecs.event.EventArgs

class EntityReleasedArgs(
    val entity: Entity
) : EventArgs