package com.github.tagwan.ecs.matcher

/**
 * IAnyOfMatcher
 *
 * @data 2022/9/20 20:21
 */
interface IAnyOfMatcher : ICompoundMatcher {
    fun noneOf(indices:Array<IMatcher>): INoneOfMatcher
    fun noneOf(indices:IntArray): INoneOfMatcher
}