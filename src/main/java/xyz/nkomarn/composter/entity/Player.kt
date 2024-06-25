package xyz.nkomarn.composter.entity

import kyta.composter.container.BasicContainer
import kyta.composter.container.menu.Menu
import kyta.composter.container.menu.PlayerInventoryMenu
import kyta.composter.entity.EntityType
import kyta.composter.item.ItemStack
import kyta.composter.network.Connection
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAddPlayerPacket
import kyta.composter.protocol.packet.play.ClientboundChatMessagePacket
import kyta.composter.protocol.packet.play.ClientboundChunkDataPacket
import kyta.composter.protocol.packet.play.ClientboundChunkOperationPacket
import kyta.composter.protocol.packet.play.ClientboundSetContainerContentPacket
import kyta.composter.protocol.packet.play.GenericPlayerActionPacket
import kyta.composter.world.ChunkPos
import kyta.composter.world.World
import net.kyori.adventure.text.Component
import xyz.nkomarn.composter.entity.tracker.EntityTracker
import java.util.function.Consumer

class Player(
    world: World,
    val connection: Connection,
    val username: String,
) : Entity(world, EntityType.PLAYER) {
    private val visibleChunks = mutableSetOf<ChunkPos>()
    override val dimensions = 0.6 to 1.8
    val inventory = BasicContainer(36)
    val armorContainer = BasicContainer(4)
    var selectedHotbarSlot = 0
    var cursorItem: ItemStack = ItemStack.EMPTY

    var inventoryMenu = PlayerInventoryMenu(this, inventory, armorContainer)
    var currentMenu: Menu? = null

    val entityTracker = EntityTracker(this)
    val stance = 67.240000009536743
    var isCrouching = false
    var isOnGround = false
    var lastDigStartTime = 0L

    var menuCounter = 0

    fun swingArm() {
        entityTracker.broadcast(GenericPlayerActionPacket(id, GenericPlayerActionPacket.Action.SWING_ARM))
    }

    override fun createAddEntityPacket(): Packet {
        return ClientboundAddPlayerPacket(this)
    }

    fun sendMessage(message: Component) {
        connection.sendPacket(ClientboundChatMessagePacket(message))
    }

    override fun tick(currentTick: Long) {
        super.tick(currentTick)
        entityTracker.tick(currentTick)
        updateVisibleChunks()

        /* send menu updates */
        currentMenu?.tick(currentTick)
            ?: inventoryMenu.tick(currentTick)
    }

    private fun updateVisibleChunks() {
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
