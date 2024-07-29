package com.github.tagwan.ecs.exception

/**
 * EntityDoesNotHaveComponentException
 *
 * @data 2022/9/20 19:58
 */
class EntityDoesNotHaveComponentException(message:String, index:Int)
    : Exception("$message\nEntity does not have a component at index ($index)")