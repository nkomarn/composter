package kyta.composter.protocol.packet.play

import kyta.composter.protocol.asRotation
import kyta.composter.world.entity.EntityType
import kyta.composter.world.entity.ItemEntity
import kyta.composter.item.ItemStack
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import kyta.composter.world.entity.Entity
import kyta.composter.world.entity.Player
import kyta.composter.world.entity.getHotbarItem

interface AddEntityPacket : Packet {
    val id: Int
    val pos: Vec3d
    val yaw: Float
    val pitch: Float
}

data class ClientboundAddEntityPacket(
    override val id: Int,
    val entityType: EntityType,
    override val pos: Vec3d,
    override val yaw: Float,
    override val pitch: Float,
) : AddEntityPacket {
    constructor(entity: Entity) : this(entity.id, entity.type, entity.pos, entity.yaw, entity.pitch)

    companion object : PacketSerializer<ClientboundAddEntityPacket> {
        override fun serialize(packet: ClientboundAddEntityPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.entityType.networkId)
            buffer.writeAbsoluteInt(packet.pos.x)
            buffer.writeAbsoluteInt(packet.pos.y)
            buffer.writeAbsoluteInt(packet.pos.z)
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
            buffer.writeByte(0x7F) // todo; placeholder for entity metadata
        }
    }
}

data class ClientboundAddDroppedItemPacket(
    override val id: Int,
    val item: Int,
    val count: Int,
    val metadata: Int,
    override val pos: Vec3d,
    override val yaw: Float,
    override val pitch: Float,
    val roll: Float,
) : AddEntityPacket {
    constructor(entity: ItemEntity) : this(
        entity.id,
        entity.itemStack.item.networkId,
        entity.itemStack.count,
        entity.itemStack.metadataValue,
        entity.pos,
        entity.yaw,
        entity.pitch,
        0F, // todo
    )

    companion object : PacketSerializer<ClientboundAddDroppedItemPacket> {
        override fun serialize(packet: ClientboundAddDroppedItemPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeShort(packet.item)
            buffer.writeByte(packet.count)
            buffer.writeShort(packet.metadata)
            buffer.writeAbsoluteInt(packet.pos.x)
            buffer.writeAbsoluteInt(packet.pos.y)
            buffer.writeAbsoluteInt(packet.pos.z)
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
            buffer.writeByte(packet.roll.asRotation())
        }
    }
}

data class ClientboundAddPlayerPacket(
    override val id: Int,
    val username: String,
    override val pos: Vec3d,
    override val yaw: Float,
    override val pitch: Float,
    val heldItem: ItemStack,
) : AddEntityPacket {
    constructor(player: Player) : this(
        player.id,
        player.username,
        player.pos,
        player.yaw,
        player.pitch,
        player.getHotbarItem(player.selectedHotbarSlot),
    )

    companion object : PacketSerializer<ClientboundAddPlayerPacket> {
        override fun serialize(packet: ClientboundAddPlayerPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeString(packet.username)
            buffer.writeAbsoluteInt(packet.pos.x)
            buffer.writeAbsoluteInt(packet.pos.y)
            buffer.writeAbsoluteInt(packet.pos.z)
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
            buffer.writeShort(packet.heldItem.item.networkId)
        }
    }
}