package com.github.tagwan.ecs.exception

import com.github.tagwan.ecs.entity.Entity

/**
 * PoolDoesNotContainEntityException
 *
 * @data 2022/9/20 20:00
 */
class PoolDoesNotContainEntityException(entity: Entity, message:String)
    : Exception("$message\nPool does not contain entity $entity")