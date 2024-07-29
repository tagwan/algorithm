package com.github.tagwan.ecs.exception

/**
 * EntityIsNotEnabledException
 *
 * @data 2022/9/20 19:59
 */
class EntityIsNotEnabledException(message:String)
    : Exception("$message\nEntity is not enabled")