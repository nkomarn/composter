package kyta.composter.network.pipeline

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import kyta.composter.network.Connection
import kyta.composter.protocol.ConnectionState
import kyta.composter.protocol.packet.VanillaPacketHandler
import kyta.composter.server.MinecraftServer
import org.slf4j.Logger

class InboundConnectionInitializer(
    private val server: MinecraftServer,
    private val logger: Logger,
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(channel: SocketChannel) {
        val connection = Connection(server, channel, ConnectionState.HANDSHAKING)
        connection.packetHandler = VanillaPacketHandler(server, connection) // todo - net refactor

        channel.pipeline()
            .addLast(PacketDecoder(connection, logger))
            .addLast(PacketEncoder(connection, logger))
            .addLast(InboundHandler(connection, logger))

        channel.attr(Connection.KEY_PLAYER_CONNECTION).set(connection)
    }
}