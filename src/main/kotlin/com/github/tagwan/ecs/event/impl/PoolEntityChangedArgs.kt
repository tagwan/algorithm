package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.Pool
import com.github.tagwan.ecs.entity.Entity
import com.github.tagwan.ecs.event.EventArgs

/**
 * PoolEntityChangedArgs
 *
 * @data 2022/9/20 20:16
 */
class PoolEntityChangedArgs(
    val pool: Pool,
    val entity: Entity
) : EventArgs