package com.github.tagwan.ecs.exception

/**
 * EntityAlreadyHasComponentException
 *
 * @data 2022/9/20 19:58
 */
class EntityAlreadyHasComponentException(message:String, index:Int)
    : Exception("$message\nEntity already has a component at index ($index)")