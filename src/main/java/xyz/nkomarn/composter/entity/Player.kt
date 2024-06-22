package xyz.nkomarn.composter.entity

import kyta.composter.network.Connection
import kyta.composter.protocol.packet.play.ClientboundChunkDataPacket
import kyta.composter.protocol.packet.play.ClientboundChunkOperationPacket
import kyta.composter.protocol.packet.play.GenericPlayerActionPacket
import kyta.composter.world.ChunkPos
import kyta.composter.world.World
import xyz.nkomarn.composter.entity.tracker.EntityTracker
import java.util.function.Consumer

class Player(
    world: World,
    val connection: Connection,
    val username: String,
) : Entity(world) {
    private val visibleChunks = mutableSetOf<ChunkPos>()
    val entityTracker = EntityTracker(this)
    val stance = 67.240000009536743
    var isCrouching = false
    var isOnGround = false
    var lastDigStartTime = 0L

    fun swingArm() {
        entityTracker.broadcast(GenericPlayerActionPacket(id, GenericPlayerActionPacket.Action.SWING_ARM))
    }

    override fun tick(currentTick: Long) {
        super.tick(currentTick)
        entityTracker.tick(currentTick)
        updateVisibleChunks()
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
