package com.github.tagwan.other.pool


data class PooledObject<T>(
    var objection: T,
    var busy: Boolean = false
)