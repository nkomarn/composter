package kyta.composter.world.entity

enum class EntityType(val networkId: Int) {
    PIG(90),
    DROPPED_ITEM(140), // todo; temporary id, not sure that items are sent the same way as entities
    PLAYER(141) // todo; temporary id, not sure that items are sent the same way as entities,
}