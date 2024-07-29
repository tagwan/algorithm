package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.Group
import com.github.tagwan.ecs.Pool
import com.github.tagwan.ecs.event.EventArgs

/**
 * PoolGroupChangedArgs
 *
 * @data 2022/9/20 20:16
 */
class PoolGroupChangedArgs(
    val pool: Pool,
    val group: Group
) : EventArgs