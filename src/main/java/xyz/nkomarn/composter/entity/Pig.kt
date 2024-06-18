package xyz.nkomarn.composter.entity

import kyta.composter.entity.data.DataDescriptor
import kyta.composter.entity.data.DataType
import xyz.nkomarn.composter.world.World

class Pig(world: World) : Entity(world) {
    init {
//         synchronizedData.register(DATA_SADDLED)
    }

    override fun tick(currentTick: Long) {
        super.tick(currentTick)

        val nearbyPlayer = world.server.playerManager.players.firstOrNull()
            ?: return

        if (nearbyPlayer.getBlockPos().distanceSqrt(getBlockPos()) > 30) {
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
        val DATA_SADDLED = DataDescriptor(16, DataType.BYTE, false)
    }
}