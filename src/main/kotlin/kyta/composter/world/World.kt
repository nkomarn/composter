package kyta.composter.world

import kyta.composter.item.Item
import kyta.composter.item.ItemStack
import kyta.composter.math.AABB
import kyta.composter.math.Vec3d
import kyta.composter.math.intersects
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundSetTimePacket
import kyta.composter.protocol.packet.play.ClientboundUpdateBlockPacket
import kyta.composter.server.MinecraftServer
import kyta.composter.server.Tickable
import kyta.composter.world.block.AIR
import kyta.composter.world.block.BlockState
import kyta.composter.world.block.defaultState
import kyta.composter.world.chunk.ChunkController
import kyta.composter.world.dimension.DimensionType
import kyta.composter.world.entity.Entity
import kyta.composter.world.entity.EntityStorage
import kyta.composter.world.entity.ItemEntity
import kyta.composter.world.entity.Pig
import kyta.composter.world.entity.Player
import kyta.composter.world.entity.boundingBox
import kyta.composter.world.entity.pos
import xyz.nkomarn.composter.world.ChunkIO
import xyz.nkomarn.composter.world.generator.WorldGenerator

class World(
    val server: MinecraftServer,
    val properties: Properties,
) : Tickable {
    private var time: Long = 0
    val chunks = ChunkController(this)
    val entities = EntityStorage(this)

    init {
        Pig().let {
            it.pos = Vec3d(properties.spawn)
            entities.add(it)
        }
    }

    /**
     * requires the chunk to be currently loaded.
     */
    fun getBlock(pos: BlockPos): BlockState {
        val chunkPos = ChunkPos(pos)
        val chunk = chunks.getLoadedChunk(chunkPos)
            ?: return AIR.defaultState

        return chunk.getBlock(pos)
    }

    fun setBlock(pos: BlockPos, state: BlockState) {
        val chunkPos = ChunkPos(pos)
        val chunk = chunks.getLoadedChunk(chunkPos)

        if (chunk != null) {
            chunk.setBlock(pos, state)
            broadcast(pos, ClientboundUpdateBlockPacket(pos, state))
        }
    }

    fun broadcast(origin: BlockPos, packet: Packet) {
        for (entity in entities) {
            if (entity is Player) {
                entity.connection.sendPacket(packet)
            }
        }
    }

    override fun tick(currentTick: Long) {
        time++

        chunks.tick(currentTick)
        entities.tick(currentTick)

        /* send time updates to clients */
        if (currentTick % 20 == 0L) {
            val packet = ClientboundSetTimePacket(time)
            server.playerList
                .filter { it.world === this }
                .forEach { it.connection.sendPacket(packet) }
        }
    }

    data class Properties(
        val dimensionType: DimensionType,
        val seed: Long,
        val io: ChunkIO,
        val generator: WorldGenerator,
        var spawn: BlockPos,
    )

    companion object {
        const val MAX_WORLD_HEIGHT = 127
    }
}

fun World.getCollidingEntities(box: AABB): Sequence<Entity> {
    return entities.asSequence()
        .filterNot(Entity::isRemoved)
        .filter { box.intersects(it.boundingBox) }
}

fun World.breakBlock(pos: BlockPos) {
    val state = getBlock(pos)
    val entity = ItemEntity().apply {
        this.pos = Vec3d(pos).add(0.5, 0.0, 0.5)
        itemStack = ItemStack(Item(state.block.networkId), metadataValue = state.metadataValue)
    }

    setBlock(pos, AIR.defaultState)
    entities.add(entity)
}