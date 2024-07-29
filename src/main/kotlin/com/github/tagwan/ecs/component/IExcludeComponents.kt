package com.github.tagwan.ecs.component

import com.github.tagwan.ecs.matcher.IMatcher


/**
 * 排除组件
 *
 * @data 2022/9/20 20:07
 */
interface IExcludeComponents {
    val excludeComponents: IMatcher
}