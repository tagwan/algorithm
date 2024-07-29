package com.github.tagwan.ecs.event.impl

import com.github.tagwan.ecs.matcher.Matcher
import com.github.tagwan.ecs.event.GroupEventType

/**
 * TriggerOnEvent
 *
 * @data 2022/9/20 20:17
 */
class TriggerOnEvent(val trigger: Matcher, val eventType: GroupEventType)