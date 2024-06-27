package kyta.composter.network

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.util.AttributeKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kyta.composter.protocol.ConnectionState
import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.packet.GenericDisconnectPacket
import kyta.composter.server.MinecraftServer
import kyta.composter.world.entity.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

data class Connection(
    private val server: MinecraftServer,
    private val channel: Channel,
    var state: ConnectionState,
) : CoroutineScope {
    override val coroutineContext = Dispatchers.Unconfined
    lateinit var packetHandler: PacketHandler
    lateinit var player: Player

    fun sendPacket(packet: Packet): ChannelFuture {
        return channel.writeAndFlush(packet)
    }

    @Deprecated("Replaced by modern component API.")
    fun disconnect(message: String) {
        disconnect(Component.text(message))
    }

    fun disconnect(message: Component) {
        // todo - net refactor
        val string = LegacyComponentSerializer.legacySection().serialize(message)
        channel.writeAndFlush(GenericDisconnectPacket(string)).addListener(ChannelFutureListener.CLOSE)
    }

    fun close() {
        channel.close()
    }

    fun handleDisconnection() {
        if (this::player.isInitialized) {
            server.playerList.playerDisconnected(player)
        }
    }

    companion object {
        val KEY_PLAYER_CONNECTION: AttributeKey<Connection> = AttributeKey.valueOf("player_connection")
    }
}
