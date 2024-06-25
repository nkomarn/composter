package xyz.nkomarn.composter.entity

import java.util.function.Consumer
import kyta.composter.container.BasicContainer
import kyta.composter.container.menu.MenuSynchronizer
import kyta.composter.entity.EntityType
import kyta.composter.entity.ItemEntity
import kyta.composter.item.Item
import kyta.composter.item.ItemStack
import kyta.composter.network.Connection
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAddPlayerPacket
import kyta.composter.protocol.packet.play.ClientboundChatMessagePacket
import kyta.composter.protocol.packet.play.ClientboundChunkDataPacket
import kyta.composter.protocol.packet.play.ClientboundChunkOperationPacket
import kyta.composter.protocol.packet.play.GenericPlayerActionPacket
import kyta.composter.world.ChunkPos
import kyta.composter.world.World
import net.kyori.adventure.text.Component
import xyz.nkomarn.composter.entity.tracker.EntityTracker

class Player(
    world: World,
    val connection: Connection,
    val username: String,
) : Entity(world, EntityType.PLAYER) {
    override val dimensions = 0.6 to 1.8
    val stance = 67.240000009536743
    var isCrouching = false
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
            inventory.setItem(x, ItemStack(Item(x + 1)))
        }
    }

    override fun createAddEntityPacket(): Packet {
        return ClientboundAddPlayerPacket(this)
    }

    override fun tick(currentTick: Long) {
        super.tick(currentTick)
        entityTracker.tick(currentTick)
        menuSynchronizer.tick(currentTick)

        updateVisibleChunks()
    }

    fun updateVisibleChunks() {
        val currentBlock = getBlockPos()
        val (currentChunkX, currentChunkZ) = ChunkPos(currentBlock)

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

    private companion object {
        const val VIEW_DISTANCE = 16
    }
}

fun Player.sendMessage(message: Component) {
    connection.sendPacket(ClientboundChatMessagePacket(message))
}

fun Player.getHotbarItem(index: Int): ItemStack {
    return inventory.getItem(index.coerceIn(0..8)) // todo; test this, might have to be in reverse

}

fun Player.drop(stack: ItemStack) {
    ItemEntity(world, stack).let { item ->
        item.pos = pos
        world.addEntity(item)
    }
}

fun Player.swingArm() {
    entityTracker.broadcast(GenericPlayerActionPacket(id, GenericPlayerActionPacket.Action.SWING_ARM))
}
