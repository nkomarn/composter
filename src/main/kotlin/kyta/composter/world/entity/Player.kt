package kyta.composter.world.entity

import java.util.function.Consumer
import kyta.composter.container.BasicContainer
import kyta.composter.container.menu.MenuSynchronizer
import kyta.composter.item.Item
import kyta.composter.item.ItemStack
import kyta.composter.item.isEmpty
import kyta.composter.math.grow
import kyta.composter.network.Connection
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAddPlayerPacket
import kyta.composter.protocol.packet.play.ClientboundChatMessagePacket
import kyta.composter.protocol.packet.play.ClientboundChunkDataPacket
import kyta.composter.protocol.packet.play.ClientboundChunkOperationPacket
import kyta.composter.protocol.packet.play.ClientboundCollectDroppedItemPacket
import kyta.composter.protocol.packet.play.GenericPlayerActionPacket
import kyta.composter.server.world.entity.tracker.EntityTracker
import kyta.composter.world.ChunkPos
import kyta.composter.world.World
import kyta.composter.world.getCollidingEntities
import net.kyori.adventure.text.Component

class Player(
    world: World,
    val connection: Connection,
    val username: String,
) : Entity(world, EntityType.PLAYER) {
    override val dimensions = 0.6 to 1.8
    val stance = 67.240000009536743
    var isOnGround = false
    var lastDigStartTime = 0L

    private val visibleChunks = mutableSetOf<ChunkPos>()
    val inventory = BasicContainer(36)
    val armorContainer = BasicContainer(4)
    var cursorItem: ItemStack = ItemStack.EMPTY
    var selectedHotbarSlot = 0

    val entityTracker = EntityTracker(this)
    val menuSynchronizer = MenuSynchronizer(this)

    init {
        for (x in 0 until inventory.size) {
            inventory.setItem(x, ItemStack(Item(x + 1), count = 64))
        }
    }

    override fun createAddEntityPacket(): Packet {
        return ClientboundAddPlayerPacket(this)
    }

    override fun tick(currentTick: Long) {
        super.tick(currentTick)
        entityTracker.tick(currentTick)
        menuSynchronizer.tick(currentTick)

        collectDroppedItems()
        updateVisibleChunks()
    }

    fun updateVisibleChunks() {
        val (currentChunkX, currentChunkZ) = ChunkPos(blockPos)

        /*
         * send chunk packets for chunks that the player should
         * be able to see from their current position.
         */
        val pendingUnload = HashSet(visibleChunks)
        for (x in currentChunkX - VIEW_DISTANCE until currentChunkX + VIEW_DISTANCE) {
            for (z in currentChunkZ - VIEW_DISTANCE until currentChunkZ + VIEW_DISTANCE) {
                val pos = ChunkPos(x, z)

                /*
                 * if the chunk is currently visible, don't
                 * send another chunk data packet to the client.
                 */
                if (visibleChunks.contains(pos)) {
                    pendingUnload.remove(pos)
                    continue
                }

                /*
                 * if the chunk hasn't been loaded on the server
                 * side, don't send it to the client yet.
                 */
                val chunk = world.chunks.getLoadedChunk(pos) ?: continue
                connection.sendPacket(ClientboundChunkOperationPacket(pos, ClientboundChunkOperationPacket.Mode.LOAD))
                connection.sendPacket(ClientboundChunkDataPacket(pos, chunk))
                visibleChunks.add(pos)
            }
        }

        /*
         * unload chunks that are no longer visible.
         */
        pendingUnload.forEach(Consumer { pos: ChunkPos ->
            visibleChunks.remove(pos)
            connection.sendPacket(ClientboundChunkOperationPacket(pos, ClientboundChunkOperationPacket.Mode.UNLOAD))
        })
    }

    private fun collectDroppedItems() {
        val box = boundingBox.grow(1.0, 0.5, 1.0)
        val nearby = world.getCollidingEntities(box)
            .filterIsInstance<ItemEntity>()
            .filter { it.canBePickedUp() }

        for (entity in nearby) {
            entity.itemStack = inventory.insert(entity.itemStack)

            /**
             * Display the item pickup animation if the entire quantity
             * of the stored stack has been picked up.
             */
            if (entity.itemStack.isEmpty) {
                entityTracker.broadcastIncludingSelf(ClientboundCollectDroppedItemPacket(entity.id, id))
            }
        }
    }

    companion object {
        const val VIEW_DISTANCE = 16
        const val EYE_HEIGHT = 1.62000000476837
    }
}

var Player.heldItem: ItemStack
    get() = inventory.getItem(selectedHotbarSlot)
    set(value) = inventory.setItem(selectedHotbarSlot, value)

fun Player.sendMessage(message: Component) {
    connection.sendPacket(ClientboundChatMessagePacket(message))
}

fun Player.getHotbarItem(index: Int): ItemStack {
    return inventory.getItem(index.coerceIn(0..8)) // todo; test this, might have to be in reverse

}

fun Player.drop(stack: ItemStack) {
    val entity = ItemEntity(world).apply {
        pos = this@drop.pos
        itemStack = stack
        pickUpDelay = ItemEntity.TICKS_PLAYER_PICK_UP_DELAY
    }

    world.addEntity(entity)
}

fun Player.swingArm() {
    entityTracker.broadcast(GenericPlayerActionPacket(id, GenericPlayerActionPacket.Action.SWING_ARM))
}
