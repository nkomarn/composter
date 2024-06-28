package kyta.composter.server.world.entity.tracker

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import kyta.composter.server.Tickable
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundRemoveEntityPacket
import kyta.composter.protocol.packet.play.ClientboundSetEntityDataPacket
import org.slf4j.LoggerFactory
import kyta.composter.world.entity.Entity
import kyta.composter.world.entity.Player

class EntityTracker(private val player: Player) : Tickable {
    private val logger = LoggerFactory.getLogger("tracker")
    private val trackedEntities = Int2ObjectOpenHashMap<TrackedEntity>()

    fun broadcast(packet: Packet) {
        // todo; this should probably work in reverse
        // where players that track THIS player receive packets
        trackedEntities.values
            .map { it.entity }
            .filterIsInstance<Player>().forEach {
            it.connection.sendPacket(packet)
        }
    }

    fun broadcastIncludingSelf(packet: Packet) {
        broadcast(packet)
        player.connection.sendPacket(packet)
    }

    override fun tick(currentTick: Long) {
        /* track any newly added entities */
        for (entity in player.world.entities) {
            if (entity.id != player.id && !trackedEntities.containsKey(entity.id)) {
                trackNewEntity(entity)
            }
        }

        /* un-track old entities and tick all tracked entities */
        val iterator = trackedEntities.values.iterator()
        while (iterator.hasNext()) {
            val trackedEntity = iterator.next()
            if (trackedEntity.entity.isRemoved) {
                untrackEntity(trackedEntity.entity)
                iterator.remove()
                continue
            }

            trackedEntity.tick(currentTick)
        }
    }

    private fun trackNewEntity(entity: Entity) {
        player.connection.sendPacket(entity.createAddEntityPacket())
        player.connection.sendPacket(ClientboundSetEntityDataPacket(entity.id, entity.synchronizedData))

        val trackedEntity = TrackedEntity(entity) { player.connection.sendPacket(it) }
        trackedEntities.put(entity.id, trackedEntity)
        logger.info("{} began tracking {} (entity #{}).", player.username, entity.javaClass.getSimpleName(), entity.id)
    }

    private fun untrackEntity(entity: Entity) {
        player.connection.sendPacket(ClientboundRemoveEntityPacket(entity))
        logger.info("{} untracked {} (entity #{}).", player.username, entity.javaClass.getSimpleName(), entity.id)
    }
}
