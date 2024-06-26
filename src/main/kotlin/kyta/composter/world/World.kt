package kyta.composter.world

import kotlinx.coroutines.runBlocking
import kyta.composter.server.Tickable
import kyta.composter.world.entity.ItemEntity
import kyta.composter.item.Item
import kyta.composter.item.ItemStack
import kyta.composter.math.AABB
import kyta.composter.math.Vec3d
import kyta.composter.math.intersects
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundSetTimePacket
import kyta.composter.protocol.packet.play.ClientboundUpdateBlockPacket
import kyta.composter.server.MinecraftServer
import kyta.composter.world.block.AIR
import kyta.composter.world.block.BlockState
import kyta.composter.world.block.defaultState
import kyta.composter.world.chunk.ChunkController
import kyta.composter.world.dimension.DimensionType
import kyta.composter.world.entity.Entity
import kyta.composter.world.entity.Player
import kyta.composter.world.entity.boundingBox
import xyz.nkomarn.composter.world.ChunkIO
import xyz.nkomarn.composter.world.generator.WorldGenerator

class World(
    val server: MinecraftServer,
    val properties: Properties,
) : Tickable {
    private var time: Long = 0
    val chunks = ChunkController(this)

    private val _entities = mutableMapOf<Int, Entity>()
    val entities: Collection<Entity>
        get() = _entities.values

    init {
        createSpawnChunks()
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

    fun getEntity(id: Int): Entity? {
        return _entities[id]
    }

    fun addEntity(entity: Entity) {
        _entities[entity.id] = entity
    }

    fun broadcast(origin: BlockPos, packet: Packet) {
        for (entity in _entities.values) {
            if (entity is Player) {
                entity.connection.sendPacket(packet);
            }
        }
    }

    private fun createSpawnChunks() {
        runBlocking {
            for (x in -7..7) {
                for (z in -7..7) {
                    chunks.getChunk(ChunkPos(x, z))
                }
            }
        }

        /*
         * find the lowest suitable spawn block.
         */
        var pos = BlockPos(0, MAX_WORLD_HEIGHT, 0)
        while (pos.y > 0) {
            val block = getBlock(pos).block
            if (block != AIR) break

            pos = pos.down(1)
        }

        properties.spawn = pos.up(1)
        server.logger.info(
            "the default world spawn is now ({}, {}, {})",
            properties.spawn.x,
            properties.spawn.y,
            properties.spawn.z
        )
    }

    override fun tick(currentTick: Long) {
        time++

        _entities.values.removeIf(Entity::isRemoved)
        _entities.values.forEach { it.tick(currentTick) }

        /* send time updates to clients */
        if (currentTick % 20 == 0L) {
            val packet = ClientboundSetTimePacket(time)
            server.playerList.onlinePlayers()
                .filter { it.world === this }
                .forEach { it.connection.sendPacket(packet) }
        }

        chunks.tick(currentTick)
    }

    data class Properties(
        val type: DimensionType,
        val seed: Long,
        val io: ChunkIO,
        val generator: WorldGenerator,
        var spawn: BlockPos,
    )

    companion object {
        private const val MAX_WORLD_HEIGHT = 127
    }
}

fun World.getCollidingEntities(box: AABB): Sequence<Entity> {
    return entities.asSequence()
        .filterNot(Entity::isRemoved)
        .filter { box.intersects(it.boundingBox) }
}

fun World.breakBlock(pos: BlockPos) {
    val state = getBlock(pos)
    val entity = ItemEntity(this).apply {
        this.pos = Vec3d(pos).add(0.5, 0.0, 0.5)
        itemStack = ItemStack(Item(state.block.networkId), metadataValue = state.metadataValue)
    }

    setBlock(pos, AIR.defaultState)
    addEntity(entity)
}