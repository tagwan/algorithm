package com.github.tagwan.ecs.exception

/**
 * EntityIsNotDestroyedException
 *
 * @data 2022/9/20 19:58
 */
class EntityIsNotDestroyedException(message:String)
    : Exception("$message\nEntity is not destroyed yet!")