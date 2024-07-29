package com.github.tagwan.ecs.exception

import com.github.tagwan.ecs.matcher.IMatcher


/**
 * MatcherException
 *
 * @data 2022/9/20 19:59
 */
class MatcherException(matcher: IMatcher)
    : Exception("matcher.indices.length must be 1 but was ${matcher.indices.size}")