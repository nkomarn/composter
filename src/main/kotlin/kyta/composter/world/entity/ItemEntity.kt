package kyta.composter.world.entity

import kyta.composter.item.ItemStack
import kyta.composter.item.isEmpty
import kyta.composter.math.grow
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAddDroppedItemPacket
import kyta.composter.world.World
import kyta.composter.world.getCollidingEntities

class ItemEntity : Entity(EntityType.DROPPED_ITEM) {
    override val dimensions = 0.25 to 0.25
    var itemStack = ItemStack.EMPTY
        set(value) {
            field = value

            if (value.isEmpty) {
                markRemoved()
            }
        }

    var pickUpDelay = 10

    override fun createAddEntityPacket(): Packet {
        return ClientboundAddDroppedItemPacket(this)
    }

    fun canBePickedUp(): Boolean {
        return pickUpDelay == 0
    }

    override fun tick(currentTick: Long, world: World) {
        super.tick(currentTick, world)

        if (pickUpDelay > 0) {
            pickUpDelay--
        }

        /**
         * Dropped items that have not been picked up in too long
         * are removed from the world.
         */
        if (ticksAlive >= DESPAWN_TICKS) {
            return markRemoved()
        }

//        mergeWithNearbyItems()
    }

    /**
     * TODO; this is not actually a behavior in b1.7.3.
     * It appears item merging was not implemented until
     * later in the game's development cycle.
     *
     * Should this still be a feature? Maybe a configurable one?
     */
    private fun mergeWithNearbyItems() {
        val nearby = world.getCollidingEntities(boundingBox.grow(ITEM_MERGE_RADIUS))
            .filterIsInstance<ItemEntity>()

        /**
         * Decide which way to merge based on the lower tick count:
         * - from this item entity into another
         * - from another item entity into this one
         */
        for (entity in nearby) {
            if (entity.ticksAlive < ticksAlive) {

            }

            val lowerTickCount = if (entity.ticksAlive < ticksAlive) entity else this
            TODO()
        }
    }

    companion object {
        const val TICKS_PICK_UP_DELAY = 10
        const val TICKS_PLAYER_PICK_UP_DELAY = 40
        const val DESPAWN_TICKS = 6000
        const val ITEM_MERGE_RADIUS = 0.75
    }
}