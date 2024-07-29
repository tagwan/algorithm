package com.github.tagwan.ecs.exception

import com.github.tagwan.ecs.matcher.IMatcher


/**
 * SingleEntityException
 *
 * @data 2022/9/20 20:00
 */
class SingleEntityException(matcher: IMatcher)
    : Exception("Multiple entities exist matching $matcher")