package xyz.nkomarn.composter.network.protocol.packet.play

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kyta.composter.math.Vec3d
import xyz.nkomarn.composter.entity.Entity
import xyz.nkomarn.composter.network.protocol.Packet
import xyz.nkomarn.composter.util.ByteBufUtil.toAbsoluteInteger
import kotlin.properties.Delegates

class ClientboundMoveEntityPacket : Packet<ClientboundMoveEntityPacket> {
    private var entityId by Delegates.notNull<Int>()
    private var deltaX by Delegates.notNull<Int>()
    private var deltaY by Delegates.notNull<Int>()
    private var deltaZ by Delegates.notNull<Int>()

    constructor()

    constructor(entity: Entity, deltaX: Int, deltaY: Int, deltaZ: Int) {
        entityId = entity.id
        this.deltaX = deltaX
        this.deltaY = deltaY
        this.deltaZ = deltaZ
    }

    override fun getId(): Int {
        return 0x1F
    }

    override fun encode(): ByteBuf =
        Unpooled.buffer()
            .writeInt(entityId)
            .writeByte(deltaX)
            .writeByte(deltaY)
            .writeByte(deltaZ)
}