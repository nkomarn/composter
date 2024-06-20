package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import kyta.composter.world.BlockPos

data class ClientboundSetSpawnPacket(val pos: BlockPos) : Packet {
    companion object : PacketSerializer<ClientboundSetSpawnPacket> {
        override fun serialize(packet: ClientboundSetSpawnPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.pos.x)
            buffer.writeInt(packet.pos.y) // must be an integer y-value
            buffer.writeInt(packet.pos.z)
        }
    }
}