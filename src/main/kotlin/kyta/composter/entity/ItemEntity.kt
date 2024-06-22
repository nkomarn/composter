package kyta.composter.entity

import kyta.composter.item.ItemStack
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAddDroppedItemPacket
import kyta.composter.protocol.packet.play.ClientboundCollectDroppedItemPacket
import kyta.composter.world.World
import xyz.nkomarn.composter.entity.Entity
import xyz.nkomarn.composter.entity.Player

class ItemEntity(
    world: World,
    val itemStack: ItemStack,
) : Entity(world, EntityType.DROPPED_ITEM) {
    override val dimensions = 0.25 to 0.25

    override fun createAddEntityPacket(): Packet {
        return ClientboundAddDroppedItemPacket(this)
    }

    override fun tick(currentTick: Long) {
        super.tick(currentTick)

        val collectingPlayer = getCollidingEntities()
            .filterIsInstance<Player>()
            .firstOrNull()
            ?: return

        collectingPlayer.inventory.addItem(itemStack) // todo; handle the remainder
        collectingPlayer.entityTracker.broadcastIncludingSelf(ClientboundCollectDroppedItemPacket(id, collectingPlayer.id))
        markRemoved()
    }
}