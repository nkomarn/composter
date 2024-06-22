package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import kyta.composter.world.ChunkPos

data class ClientboundChunkOperationPacket(val pos: ChunkPos, val mode: Mode) : Packet {
    enum class Mode {
        LOAD,
        UNLOAD,
    }

    companion object : PacketSerializer<ClientboundChunkOperationPacket> {
        override fun serialize(packet: ClientboundChunkOperationPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.pos.x)
            buffer.writeInt(packet.pos.z)
            buffer.writeBoolean(packet.mode == Mode.LOAD)
        }
    }
}
