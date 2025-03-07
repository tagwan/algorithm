package com.github.tagwan.ecs.entity

import com.github.tagwan.ecs.component.IComponent
import com.github.tagwan.ecs.event.Event
import com.github.tagwan.ecs.event.impl.ComponentReplacedArgs
import com.github.tagwan.ecs.event.impl.EntityChangedArgs
import com.github.tagwan.ecs.event.impl.EntityReleasedArgs
import com.github.tagwan.ecs.exception.EntityAlreadyHasComponentException
import com.github.tagwan.ecs.exception.EntityDoesNotHaveComponentException
import com.github.tagwan.ecs.exception.EntityIsNotEnabledException
import java.util.*

/**
 * entity是一个存储数据的容器，用以表现程序中存在的对象
 * <p>
 *     Use pool.CreateEntity() to create a new entity and pool.DestroyEntity() to destroy it.
 */
class Entity(
    val totalComponents: Int
) {


    val creationIndex: Int get() = _creationIndex
    val refCount: Int get() = _refCount
    val name: String get() = _name
    val isEnabled: Boolean get() = _isEnabled

    val onEntityReleased = Event<EntityReleasedArgs>()
    val onComponentAdded = Event<EntityChangedArgs>()
    val onComponentRemoved = Event<EntityChangedArgs>()
    val onComponentReplaced = Event<ComponentReplacedArgs>()

    private var _name = ""
    private var _refCount = 0
    private var _isEnabled = false
    private var _creationIndex = 0
    private var toStringCache = ""
    private val components: Array<IComponent?> = Array(totalComponents) { i -> null }
    private val componentsCache: MutableList<IComponent> = ArrayList(listOf())

    fun initialize(name: String, creationIndex: Int) {
        _name = name
        _creationIndex = creationIndex
        _isEnabled = true
    }

    /**
     *  Adds a component at a certain index. You can only have one component at an index.
     *  Each component type must have its own constant index.
     *  The preferred way is to use the generated methods from the code generator.
     *
     *  @param[index]   the components ordinal index
     *  @param[component]   the component object to add
     *  @return this entity
     */
    fun addComponent(index: Int, component: IComponent): Entity {
        if (!isEnabled)
            throw EntityIsNotEnabledException("Cannot add component!")
        if (hasComponent(index)) {
            val errorMsg = "Cannot add component at index $index to ${toString()}"
            throw EntityAlreadyHasComponentException(errorMsg, index)
        }
        components[index] = component
        componentsCache.clear()
        toStringCache = ""
        onComponentAdded(EntityChangedArgs(this, index, component))
        return this
    }

    /**
     *  Removes a component at a certain index. You can only remove a component at an index if it exists.
     *  The preferred way is to use the generated methods from the code generator.
     *
     *  @param[index]   the components ordinal index
     *  @return this entity
     */
    fun removeComponent(index: Int): Entity {
        if (!isEnabled)
            throw EntityIsNotEnabledException("Entity is disabled, cannot remove component")
        if (!hasComponent(index)) {
            val errorMsg = "Cannot remove component at index $index from ${toString()}"
            throw EntityDoesNotHaveComponentException(errorMsg, index)
        }
        _replaceComponent(index, null)
        return this
    }

    /**
     *  Replaces an existing component at a certain index or adds it if it doesn't exist yet.
     *  The preferred way is to use the generated methods from the code generator.
     *
     *  @param[index]   the components ordinal index
     *  @param[component]   the replacement component object
     *  @return this entity
     */
    fun replaceComponent(index: Int, component: IComponent?): Entity {
        if (!isEnabled)
            throw EntityIsNotEnabledException("Entity is disabled, cannot replace at index $index, ${toString()}")

        if (hasComponent(index)) {
            _replaceComponent(index, component)
        } else if (component != null) {
            addComponent(index, component)
        }
        return this
    }

    /**
     *  Returns a component at a certain index. You can only get a component at an index if it exists.
     *  The preferred way is to use the generated methods from the code generator.
     *
     *  @param[index]   the components ordinal index
     *  @return the compoment
     */
    fun getComponent(index: Int): IComponent? {
        if (!hasComponent(index)) {
            val errorMsg = "Cannot get component at index $index from ${toString()}"
            throw EntityDoesNotHaveComponentException(errorMsg, index)
        }
        return components[index]
    }

    /**
     *  Returns all added components.
     *
     *  @return an array of components
     */
    fun getComponents(): MutableList<IComponent> {
        if (componentsCache.size == 0) {
            componentsCache.addAll(components.filterNotNull().toMutableList())
        }
        return componentsCache//.toTypedArray()
    }

    /**
     *  Determines whether this entity has a component at the specified index.
     *
     *  @param[index]   the components ordinal index
     *  @return true if the component is available
     */
    fun hasComponent(index: Int): Boolean {
        return components[index] != null
    }

    /**
     *  Determines whether this entity has components at all the specified indices.
     *
     *  @param[indices]   an arry of components ordinal index's
     *  @return true if all components in indices are available
     */
    fun hasComponents(indices: IntArray): Boolean {
        for (index in indices)
            if (components[index] == null) return false
        return true
    }

    /**
     *  Determines whether this entity has a component at any of the specified indices.
     *
     *  @param[indices]   an arry of components ordinal index's
     *  @return true if any components in indices are available
     */
    fun hasAnyComponent(indices: IntArray): Boolean {
        for (index in indices)
            if (components[index] != null) return true
        return false
    }

    /**
     *
     *  Removes all components.
     */
    fun removeAllComponents() {
        for (index in 0..totalComponents - 1)
            if (components[index] != null)
                _replaceComponent(index, null)
    }

    fun retain(): Entity {
        _refCount += 1
        return this
    }

    fun release() {
        _refCount -= 1
        if (_refCount == 0) {
            onEntityReleased(EntityReleasedArgs(this))
        } else if (_refCount < 0)
            throw Exception("Entity is already released ${toString()}")
    }

    /**
     *  Returns a cached string to describe the entity with the following format:
     *  Entity_{name|creationIndex}(*{retainCount})({list of components})
     */
    override fun toString(): String {
        if (toStringCache == "") {
            val sb = StringBuilder()
            sb.append("Entity_")
            sb.append(if (name != "") name else creationIndex.toString())
            sb.append("($_creationIndex)(")

            for (i in 0..components.size - 1) {
                //val name = Pool.instance!!.componentName(i)
                if (components[i] != null) {
                    //sb.append(Pool.instance!!.componentName(i))
                    sb.append(components[i].toString())
                    sb.append(",")
                }
            }
            sb.append(")")
            toStringCache = sb.toString().replace(",)", ")")
        }
        return toStringCache
    }

    fun destroy() {
        removeAllComponents()
        onComponentAdded.clear()
        onComponentRemoved.clear()
        onComponentReplaced.clear()
        componentsCache.clear()
        _name = ""
        _isEnabled = false
    }


    private fun _replaceComponent(index: Int, replacement: IComponent?): Entity {
        val previousComponent = components[index]
        if (previousComponent != null) {
            if (previousComponent.equals(replacement)) {
                onComponentReplaced(ComponentReplacedArgs(this, index, previousComponent, replacement))
            } else {
                components[index] = replacement
                componentsCache.clear()
                toStringCache = ""
                if (replacement == null)
                    onComponentRemoved(EntityChangedArgs(this, index, previousComponent))
                else
                    onComponentReplaced(ComponentReplacedArgs(this, index, previousComponent, replacement))
            }
        }

        return this
    }
}