package com.github.tagwan.ecs.matcher


/**
 * IAllOfMatcher
 *
 * @data 2022/9/20 20:21
 */
interface IAllOfMatcher : ICompoundMatcher {
    fun anyOf(indices:Array<IMatcher>): IAnyOfMatcher
    fun anyOf(indices:IntArray): IAnyOfMatcher
    fun noneOf(indices:Array<IMatcher>): INoneOfMatcher
    fun noneOf(indices:IntArray): INoneOfMatcher
}