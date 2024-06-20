package kyta.composter.protocol

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kyta.composter.asAbsoluteInt
import kyta.composter.protocol.packet.GenericDisconnectPacket
import kyta.composter.protocol.packet.GenericKeepAlivePacket
import kyta.composter.protocol.packet.handshaking.ClientboundHandshakePacket
import kyta.composter.protocol.packet.handshaking.ServerboundHandshakePacket
import kyta.composter.protocol.packet.login.ClientboundLoginPacket
import kyta.composter.protocol.packet.login.ServerboundLoginPacket
import kyta.composter.protocol.packet.play.ClientboundAddEntityPacket
import kyta.composter.protocol.packet.play.ClientboundAddPlayerPacket
import kyta.composter.protocol.packet.play.ClientboundChatMessagePacket
import kyta.composter.protocol.packet.play.ClientboundChunkDataPacket
import kyta.composter.protocol.packet.play.ClientboundChunkOperationPacket
import kyta.composter.protocol.packet.play.ClientboundRemoveEntityPacket
import kyta.composter.protocol.packet.play.ClientboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.ClientboundSetSpawnPacket
import kyta.composter.protocol.packet.play.ClientboundSetTimePacket
import kyta.composter.protocol.packet.play.FlyingStatusPacket
import kyta.composter.protocol.packet.play.PositionPacket
import kyta.composter.protocol.packet.play.RotationPacket
import kyta.composter.protocol.packet.play.ServerboundChatMessagePacket
import kyta.composter.protocol.packet.play.ServerboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.ServerboundSetPlayerFlyingStatusPacket
import kyta.composter.protocol.packet.play.ServerboundSetPlayerPositionPacket
import kyta.composter.protocol.packet.play.ServerboundSetPlayerRotationPacket
import kyta.composter.world.BlockPos
import kotlin.reflect.KClass

interface Packet
interface PacketHandler {
    suspend fun handleHandshake(packet: ServerboundHandshakePacket)
    suspend fun handleKeepAlive(packet: GenericKeepAlivePacket)
    suspend fun handleLogin(packet: ServerboundLoginPacket)

    suspend fun handleChatMessage(packet: ServerboundChatMessagePacket)

    suspend fun handlePlayerFlyingStatus(packet: FlyingStatusPacket)
    suspend fun handlePlayerPosition(packet: PositionPacket)
    suspend fun handlePlayerRotation(packet: RotationPacket)
    suspend fun handleAbsolutePlayerPosition(packet: ServerboundSetAbsolutePlayerPositionPacket)

    suspend fun handleDisconnect(packet: GenericDisconnectPacket)
}

interface ServerboundPacket : Packet {
    suspend fun handle(handler: PacketHandler)
}

object Protocol {
    init {
        val allStates = ConnectionState.entries.toTypedArray()

        registerPacket(0, GenericKeepAlivePacket::class, GenericKeepAlivePacket, FlowDirection.CLIENTBOUND, *allStates)
        registerPacket(0, GenericKeepAlivePacket::class, GenericKeepAlivePacket, FlowDirection.SERVERBOUND, *allStates)

        registerPacket(2, ClientboundHandshakePacket::class, ClientboundHandshakePacket, FlowDirection.CLIENTBOUND, ConnectionState.HANDSHAKING)
        registerPacket(2, ServerboundHandshakePacket::class, ServerboundHandshakePacket, FlowDirection.SERVERBOUND, ConnectionState.HANDSHAKING)

        registerPacket(1, ClientboundLoginPacket::class, ClientboundLoginPacket, FlowDirection.CLIENTBOUND, ConnectionState.LOGIN)
        registerPacket(1, ServerboundLoginPacket::class, ServerboundLoginPacket, FlowDirection.SERVERBOUND, ConnectionState.LOGIN)

        registerPacket(3, ClientboundChatMessagePacket::class, ClientboundChatMessagePacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(3, ServerboundChatMessagePacket::class, ServerboundChatMessagePacket, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        registerPacket(4, ClientboundSetTimePacket::class, ClientboundSetTimePacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(6, ClientboundSetSpawnPacket::class, ClientboundSetSpawnPacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(10, ServerboundSetPlayerFlyingStatusPacket::class, ServerboundSetPlayerFlyingStatusPacket, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        registerPacket(11, ServerboundSetPlayerPositionPacket::class, ServerboundSetPlayerPositionPacket, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        registerPacket(12, ServerboundSetPlayerRotationPacket::class, ServerboundSetPlayerRotationPacket, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        registerPacket(13, ClientboundSetAbsolutePlayerPositionPacket::class, ClientboundSetAbsolutePlayerPositionPacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(13, ServerboundSetAbsolutePlayerPositionPacket::class, ServerboundSetAbsolutePlayerPositionPacket, FlowDirection.SERVERBOUND, ConnectionState.PLAY)
        registerPacket(20, ClientboundAddPlayerPacket::class, ClientboundAddPlayerPacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(24, ClientboundAddEntityPacket::class, ClientboundAddEntityPacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(29, ClientboundRemoveEntityPacket::class, ClientboundRemoveEntityPacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(50, ClientboundChunkOperationPacket::class, ClientboundChunkOperationPacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)
        registerPacket(51, ClientboundChunkDataPacket::class, ClientboundChunkDataPacket, FlowDirection.CLIENTBOUND, ConnectionState.PLAY)

        registerPacket(255, GenericDisconnectPacket::class, GenericDisconnectPacket, FlowDirection.CLIENTBOUND, *allStates)
        registerPacket(255, GenericDisconnectPacket::class, GenericDisconnectPacket, FlowDirection.SERVERBOUND, *allStates)
    }

    fun bootstrap() {
    }

    private fun registerPacket(
        id: Int,
        type: KClass<out Packet>,
        serializer: PacketSerializer<out Packet>,
        direction: FlowDirection,
        vararg states: ConnectionState,
    ) {
        states.forEach {
            val definitions = it.definitions[direction]!!
            definitions.packetIds[type] = id
            definitions.serializers[id] = serializer
        }
    }
}

enum class FlowDirection {
    CLIENTBOUND,
    SERVERBOUND,
}

enum class ConnectionState {
    HANDSHAKING,
    LOGIN,
    PLAY;

    internal val definitions = mapOf(
        FlowDirection.CLIENTBOUND to PacketDefinitions(this),
        FlowDirection.SERVERBOUND to PacketDefinitions(this),
    )
}

internal class PacketDefinitions(
    private val state: ConnectionState,
    internal val packetIds: MutableMap<KClass<out Packet>, Int> = mutableMapOf(),
    internal val serializers: Array<PacketSerializer<out Packet>?> = arrayOfNulls(256),
) {
    fun getId(type: KClass<out Packet>): Int {
        return packetIds[type] ?: error("${type.simpleName} does not have an assigned id for state ${state.name}.")
    }

    fun getSerializer(id: Int) : PacketSerializer<out Packet>? {
        return serializers[id]
    }
}

interface PacketSerializer<out T : Packet> {
    fun serialize(packet: @UnsafeVariance T, buffer: WriteBuffer) {
    }

    fun deserialize(buffer: ReadBuffer): T {
        throw UnsupportedOperationException()
    }
}

interface ReadBuffer {
    fun readByte(): Byte
    fun readShort(): Short
    fun readInt(): Int
    fun readLong(): Long
    fun readFloat(): Float
    fun readDouble(): Double
    fun readBoolean(): Boolean
    fun readString(): String
    fun readBlockPos(): BlockPos
}

interface WriteBuffer {
    fun writeByte(value: Number)
    fun writeBytes(value: ByteArray, start: Int, end: Int)
    fun writeShort(value: Number)
    fun writeInt(value: Number)
    fun writeAbsoluteInt(value: Number)
    fun writeLong(value: Number)
    fun writeFloat(value: Number)
    fun writeDouble(value: Number)
    fun writeBoolean(value: Boolean)
    fun writeString(value: String)
    fun writeBlockPos(value: BlockPos)
}


@JvmInline
value class MinecraftPacketBuffer(internal val buf: ByteBuf) : ReadBuffer, WriteBuffer {
    override fun readByte(): Byte {
        return buf.readByte()
    }

    override fun readShort(): Short {
        return buf.readShort()
    }

    override fun readInt(): Int {
        return buf.readInt()
    }

    override fun readLong(): Long {
        return buf.readLong()
    }

    override fun readFloat(): Float {
        return buf.readFloat()
    }

    override fun readDouble(): Double {
        return buf.readDouble()
    }

    override fun readBoolean(): Boolean {
        return buf.readBoolean()
    }

    override fun readString(): String {
        val length = buf.readUnsignedShort()
        val characters = CharArray(length)

        for (i in 0 until length) {
            characters[i] = buf.readChar()
        }

        return String(characters)
    }

    override fun readBlockPos(): BlockPos {
        TODO("Not yet implemented")
    }

    override fun writeByte(value: Number) {
        buf.writeByte(value.toInt())
    }

    override fun writeBytes(value: ByteArray, start: Int, end: Int) {
        buf.writeBytes(value, start, end)
    }

    override fun writeShort(value: Number) {
        buf.writeShort(value.toInt())
    }

    override fun writeInt(value: Number) {
        buf.writeInt(value.toInt())
    }

    override fun writeAbsoluteInt(value: Number) {
        buf.writeInt(value.toDouble().asAbsoluteInt())
    }

    override fun writeLong(value: Number) {
        buf.writeLong(value.toLong())
    }

    override fun writeFloat(value: Number) {
        buf.writeFloat(value.toFloat())
    }

    override fun writeDouble(value: Number) {
        buf.writeDouble(value.toDouble())
    }

    override fun writeBoolean(value: Boolean) {
        buf.writeBoolean(value)
    }

    override fun writeString(value: String) {
        val characters = value.toCharArray()
        buf.writeShort(characters.size)

        for (character in characters) {
            buf.writeChar(character.code)
        }
    }

    override fun writeBlockPos(value: BlockPos) {
        buf.writeInt(value.x)
        buf.writeByte(value.y)
        buf.writeInt(value.z)
    }

    companion object {
        fun create(packetId: Int) = MinecraftPacketBuffer(Unpooled.buffer(1).writeByte(packetId))
    }
}