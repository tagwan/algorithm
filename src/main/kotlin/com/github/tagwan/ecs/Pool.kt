package com.github.tagwan.ecs

import com.github.tagwan.ecs.entity.Entity
import com.github.tagwan.ecs.event.Event
import com.github.tagwan.ecs.event.impl.*
import com.github.tagwan.ecs.exception.EntityIsNotDestroyedException
import com.github.tagwan.ecs.exception.PoolDoesNotContainEntityException
import com.github.tagwan.ecs.matcher.IMatcher
import com.github.tagwan.ecs.system.IMultiReactiveSystem
import com.github.tagwan.ecs.system.IReactiveSystem
import com.github.tagwan.ecs.system.ISystem
import com.github.tagwan.ecs.system.ReactiveSystem
import java.util.*

/**
 *
 * A pool manages the lifecycle of entities and groups.
 * You can create and destroy entities and get groups of entities.
 * The preferred way is to use the generated methods from the code generator to create a Pool, e.g. var pool = Pools.pool;
 *
 * @param   totalComponents number of components
 * @param   startCreationIndex  entity index counter
 * @param   componentName  component index to name function
 */
class Pool(
    val totalComponents: Int,
    startCreationIndex: Int = 0,
    componentName: ((p: Int) -> String) = { it.toString() }
) {

    val startCreationIndex = startCreationIndex
    val componentName = componentName
    val count: Int get() = _entities.size
    val reusableEntitiesCount: Int get() = _reusableEntities.size
    val retainedEntitiesCount: Int get() = _retainedEntities.size

    val onEntityCreated = Event<PoolEntityChangedArgs>()
    val onEntityWillBeDestroyed = Event<PoolEntityChangedArgs>()
    val onEntityDestroyed = Event<PoolEntityChangedArgs>()
    val onGroupCreated = Event<PoolGroupChangedArgs>()
    private var _creationIndex: Int = startCreationIndex
    private val _entities: HashSet<Entity> = hashSetOf()
    private val _groups: HashMap<IMatcher, Group> = hashMapOf()
    private val _groupsForIndex: Array<MutableList<Group>?> = Array(totalComponents) { i -> null }
    private val _reusableEntities: MutableList<Entity> = mutableListOf()
    private val _retainedEntities: HashSet<Entity> = hashSetOf()
    private val _entitiesCache: MutableList<Entity> = mutableListOf()
    private lateinit var onEntityReleasedCache: (e: EntityReleasedArgs) -> Unit

    /**
     * EventHandler onEntityReleased
     */
    val onEntityReleased = { e: EntityReleasedArgs ->
        if (e.entity.isEnabled)
            throw EntityIsNotDestroyedException("Cannot release entity.")

        e.entity.onEntityReleased -= onEntityReleasedCache
        _retainedEntities.remove(e.entity)
        val ignore = _reusableEntities.add(e.entity)

    }

    /**
     * EventHandler updateGroupsComponentAddedOrRemoved
     */
    val updateGroupsComponentAddedOrRemoved = { e: EntityChangedArgs ->
        if (_groupsForIndex[e.index] != null) {
            for (i in 0.._groupsForIndex[e.index]!!.size - 1) {
                val group = _groupsForIndex[e.index]!![i]
                if (e.component != null)
                    group.handleEntity(e.entity, e.index, e.component)
            }
        }
    }

    /**
     * EventHandler updateGroupsComponentReplaced
     */
    val updateGroupsComponentReplaced = { e: ComponentReplacedArgs ->

        if (_groupsForIndex[e.index] != null) {
            for (i in 0 until _groupsForIndex[e.index]!!.size) {
                val group = _groupsForIndex[e.index]!![i]
                group.updateEntity(e.entity, e.index, e.previous, e.replacement)
            }
        }
    }


    init {
        onEntityReleasedCache = onEntityReleased
        _instance = this
    }

    /**
     *
     * Creates a new entity or gets a reusable entity from the internal ObjectPool for entities.
     */
    fun createEntity(name: String): Entity {
        val entity = if (_reusableEntities.size > 0) _reusableEntities.removeAt(_reusableEntities.size - 1) else Entity(
            totalComponents
        )
        entity.initialize(name, _creationIndex++)
//        entity.isEnabled = true
//        entity.name = name
//        entity._creationIndex = _creationIndex++
        entity.retain()
        entity.onComponentAdded += updateGroupsComponentAddedOrRemoved
        entity.onComponentRemoved += updateGroupsComponentAddedOrRemoved
        entity.onComponentReplaced += updateGroupsComponentReplaced
        entity.onEntityReleased += onEntityReleased
        _entities.add(entity)
        _entitiesCache.clear()
        onEntityCreated(PoolEntityChangedArgs(this, entity))
        return entity
    }

    /**
     *
     * Destroys the entity, removes all its components and pushes it back to the internal ObjectPool for entities.
     */
    fun destroyEntity(entity: Entity?) {
        if (entity == null) return
        if (entity !in _entities) {
            throw PoolDoesNotContainEntityException(entity, "Could not destroy entity!")
        }
        _entities.remove(entity)
        _entitiesCache.clear()
        onEntityWillBeDestroyed(PoolEntityChangedArgs(this, entity))
        entity.destroy()
        onEntityDestroyed(PoolEntityChangedArgs(this, entity))

        if (entity.refCount == 1) {
            entity.onEntityReleased -= onEntityReleased
            _reusableEntities.add(entity)
        } else {
            _retainedEntities.add(entity)
        }
    }


    /**
     *
     * Destroys all entities in the pool.
     */
    fun destroyAllEntities() {
        val entities = getEntities()
        for (i in 0..entities.size)
            destroyEntity(entities[i])
    }

    /**
     *
     * Determines whether the pool has the specified entity.
     */
    fun hasEntity(entity: Entity): Boolean {
        return entity in _entities
    }

    private fun getEntities(): MutableList<Entity> {
        if (_entitiesCache.size == 0) {
            for (entity in _entities)
                _entitiesCache.add(entity)
        }
        return _entitiesCache
    }

    fun getEntities(matcher: IMatcher): Array<Entity>? {
        return getGroup(matcher).entities
//        return getGroup(matcher)!!.getEntities()
    }

    fun createSystem(system: ISystem): ISystem {
        setPool(system, this)
        if (system is IReactiveSystem) {
            return ReactiveSystem(this, system)
        }
        if (system is IMultiReactiveSystem) {
            return ReactiveSystem(this, system)
        }
        return system
    }

    /**
     *
     * Returns a group for the specified matcher.
     * Calling pool.GetGroup(matcher) with the same matcher will always return the same instance of the group.
     */
    fun getGroup(matcher: IMatcher): Group {
        var group = _groups[matcher]
        if (group != null) {
            return group
        } else {
            val group = Group(matcher)
            val entities = getEntities()
            for (i in 0..entities.size - 1) {
                group.handleEntitySilently(entities[i])
            }
            _groups[matcher] = group
            for (index in matcher.indices) {
                if (_groupsForIndex[index] == null) {
                    _groupsForIndex[index] = mutableListOf()
                }
                _groupsForIndex[index]!!.add(group)
            }
            onGroupCreated(PoolGroupChangedArgs(this, group))
            return group
        }
    }

    companion object static {
        private var _instance: Pool? = null
        val instance: Pool? get() = _instance
        fun setPool(system: ISystem, pool: Pool) {
            if (system is ISetPool) {
                system.setPool(pool)
            }
        }

    }

}