package com.github.tagwan.ecs.system

import java.util.*
/**
 *
 * Systems provide a convenient way to group systems. You can add IInitializeSystem, IExecuteSystem, ReactiveSystem and other nested Systems instances.
 * All systems will be initialized and executed based on the order you added them.
 */
class Systems : IInitializeSystem, IExecuteSystem {

    private var _initializeSystems:MutableList<IInitializeSystem> = ArrayList(listOf())
    private var _executeSystems:MutableList<IExecuteSystem> = ArrayList(listOf())
    private var executeSystems:Array<IExecuteSystem> = arrayOf()

    /**
     *
     * Adds the system instance to the systems list.
     */
    fun add(system: ISystem): Systems {
        val reactiveSystem = if (system is ReactiveSystem) system else null

        val initializeSystem = if (reactiveSystem != null) {
            if (reactiveSystem.subsystem is IInitializeSystem)
                reactiveSystem.subsystem as IInitializeSystem
            else null
        } else {
            if (system is IInitializeSystem) system else null
        }

        if (initializeSystem != null)
            _initializeSystems.add(initializeSystem)

        val executeSystem = if (system is IExecuteSystem) system else null
        if (executeSystem != null) {
            _executeSystems.add(executeSystem)
        }
        return this
    }

    /**
     *
     * Calls Initialize() on all IInitializeSystem in the order you added them.
     */
    override fun initialize() {
        for (system in _initializeSystems) system.initialize()

        /** compact the execute system array for performance */
        executeSystems = _executeSystems.toTypedArray()
    }

    /**
     *
     * Calls Execute() on all IExecuteSystem, ReactiveSystem and other nested Systems instances in the order you added them.
     */
    override fun execute() {
        for (system in executeSystems) system.execute()
    }

    /**
     *
     * Clears all ReactiveSystems in the systems list.
     */
    fun clearReactiveSystems() {
        for (system in _executeSystems) {
            if (system is ReactiveSystem) system.clear()
            if (system is Systems) system.clearReactiveSystems()
        }

    }

}