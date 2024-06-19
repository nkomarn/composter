package xyz.nkomarn.composter.network.protocol.packet.play

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import xyz.nkomarn.composter.entity.Entity
import xyz.nkomarn.composter.network.protocol.Packet
import kotlin.properties.Delegates

class ClientboundMoveLookEntityPacket : Packet<ClientboundMoveLookEntityPacket> {
    private var entityId by Delegates.notNull<Int>()
    private var deltaX by Delegates.notNull<Int>()
    private var deltaY by Delegates.notNull<Int>()
    private var deltaZ by Delegates.notNull<Int>()
    private var yaw by Delegates.notNull<Int>()
    private var pitch by Delegates.notNull<Int>()

    constructor()

    constructor(entity: Entity, deltaX: Int, deltaY: Int, deltaZ: Int) {
        entityId = entity.id
        this.deltaX = deltaX
        this.deltaY = deltaY
        this.deltaZ = deltaZ
        yaw = (entity.yaw * 256F / 360F).toInt()
        pitch = (entity.pitch * 256F / 360F).toInt()
    }

    override fun getId(): Int {
        return 0x21
    }

    override fun encode(): ByteBuf =
        Unpooled.buffer()
            .writeInt(entityId)
            .writeByte(deltaX)
            .writeByte(deltaY)
            .writeByte(deltaZ)
            .writeByte(yaw)
            .writeByte(pitch)
}