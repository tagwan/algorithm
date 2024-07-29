package com.github.tagwan.ecs.matcher

/**
 * ICompoundMatcher
 *
 * @data 2022/9/20 20:20
 */
interface ICompoundMatcher : IMatcher {
    val allOfIndices:IntArray
    val anyOfIndices:IntArray
    val noneOfIndices:IntArray
}