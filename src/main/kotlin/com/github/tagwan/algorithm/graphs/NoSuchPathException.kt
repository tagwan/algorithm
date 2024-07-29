package com.github.tagwan.algorithm.graphs

class NoSuchPathException(s: String?) : Exception(s) {
    constructor() : this(null)
}