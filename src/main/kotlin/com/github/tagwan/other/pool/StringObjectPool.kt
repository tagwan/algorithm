package com.github.tagwan.other.pool

class StringObjectPool : ObjectPool<String>() {
    override fun create(): PooledObject<String> {
        return PooledObject("")
    }
}

fun main(args: Array<String>) {
    val objPool = StringObjectPool()

    objPool.createPool()
    val obj = objPool.getObject()
    objPool.returnObject(obj)
    objPool.closeObjectPool()
}