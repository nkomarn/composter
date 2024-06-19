package xyz.nkomarn.composter.network.protocol.packet.play

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import xyz.nkomarn.composter.entity.Entity
import xyz.nkomarn.composter.network.protocol.Packet
import kotlin.properties.Delegates

class ClientboundEntityLookPacket : Packet<ClientboundEntityLookPacket> {
    private var entityId by Delegates.notNull<Int>()
    private var yaw by Delegates.notNull<Int>()
    private var pitch by Delegates.notNull<Int>()

    constructor()

    constructor(entity: Entity) {
        entityId = entity.id
        yaw = (entity.yaw * 256F / 360F).toInt()
        pitch = (entity.pitch * 256F / 360F).toInt()
    }

    override fun getId(): Int {
        return 0x20
    }

    override fun encode(): ByteBuf =
        Unpooled.buffer()
            .writeInt(entityId)
            .writeByte(yaw)
            .writeByte(pitch)
}