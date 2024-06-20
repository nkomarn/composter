package kyta.composter.network.pipeline

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kyta.composter.network.Connection
import kyta.composter.network.NetworkController
import kyta.composter.protocol.ServerboundPacket
import org.slf4j.Logger

class InboundHandler(
    private val connection: Connection,
    private val logger: Logger,
) : SimpleChannelInboundHandler<ServerboundPacket>() {
    override fun channelRead0(context: ChannelHandlerContext, packet: ServerboundPacket) {
        connection.launch {
            packet.handle(connection.packetHandler)
        }
    }

    override fun channelActive(context: ChannelHandlerContext) {
        logger.info("New session -> {}", context.channel().remoteAddress())
    }

    override fun channelInactive(context: ChannelHandlerContext) {
        connection.handleDisconnection()
        logger.info("Session closed <- {}", context.channel().remoteAddress())
    }

    override fun exceptionCaught(context: ChannelHandlerContext, cause: Throwable) {
        logger.error("an error occurred in ${connection.player.username}'s connection", cause)
    }
}