package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import kyta.composter.server.world.entity.data.DataType
import kyta.composter.server.world.entity.data.SynchronizedEntityData

class ClientboundSetEntityDataPacket(val id: Int, val data: SynchronizedEntityData) : Packet {
    companion object : PacketSerializer<ClientboundSetEntityDataPacket> {
        private const val TERMINATOR = Byte.MAX_VALUE

        override fun serialize(packet: ClientboundSetEntityDataPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)

            for ((descriptor, value) in packet.data.values) {
                buffer.writeByte(descriptor.type.serialId shl 5 or (descriptor.networkId and 31) and 0xFF)

                try {
                    descriptor.type.write(buffer, value)
                } catch (x: Throwable) {
                    x.printStackTrace()
                }
            }

            /* end metadata */
            buffer.writeByte(TERMINATOR)
        }
    }
}

private fun DataType.write(output: WriteBuffer, value: Any) {
    when (this) {
        DataType.BYTE -> output.writeByte(value as Number)
        DataType.BOOLEAN -> output.writeBoolean(value as Boolean)
        DataType.SHORT -> output.writeShort(value as Number)
        else -> throw UnsupportedOperationException()
    }
}