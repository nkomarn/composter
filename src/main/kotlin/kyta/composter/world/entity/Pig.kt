package kyta.composter.world.entity

import kyta.composter.server.world.entity.data.DataDescriptor
import kyta.composter.server.world.entity.data.DataType
import kyta.composter.world.World

class Pig : Entity(EntityType.PIG) {
    override fun tick(currentTick: Long, world: World) {
        super.tick(currentTick, world)

        if (ticksAlive % 20 == 0L) {
            setSaddle(!hasSaddle())
        }

        val nearbyPlayer = world.server.playerList.firstOrNull()
            ?: return

        if (nearbyPlayer.blockPos.distanceSqRt(blockPos) > 30) {
            pitch = 0F
            return
        }

        pitch = Math.clamp((-nearbyPlayer.pitch) % 360, -27F, 5F)
        yaw = (nearbyPlayer.yaw - 180) % 360
    }

    fun hasSaddle(): Boolean {
        return synchronizedData.getValue(DATA_SADDLED)
    }

    fun setSaddle(saddled: Boolean) {
        synchronizedData.setValue(DATA_SADDLED, saddled)
    }

    companion object {
        val DATA_SADDLED = DataDescriptor(16, DataType.BOOLEAN, false)
    }
}
