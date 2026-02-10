package kyta.composter.world.entity

import kyta.composter.server.Tickable
import kyta.composter.world.World

class EntityStorage(private val world: World): Tickable, Iterable<Entity> {
    private val entities = mutableMapOf<Int, Entity>()

    operator fun get(entityId: Int): Entity? {
        return entities[entityId]
    }

    fun add(entity: Entity) {
        entity.world = world
        entities[entity.id] = entity
    }

    fun remove(entity: Entity) {
        entities.remove(entity.id, entity)
    }

    override fun iterator(): Iterator<Entity> {
        return entities.values.iterator()
    }

    override fun tick(currentTick: Long) {
        entities.values.removeIf(Entity::isRemoved)
        entities.values.forEach { it.tick(currentTick, world) }
    }
}