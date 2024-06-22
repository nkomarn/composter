package kyta.composter.network

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import kyta.composter.network.pipeline.InboundConnectionInitializer
import org.slf4j.LoggerFactory
import kyta.composter.server.MinecraftServer
import kotlin.reflect.KClass

class NetworkController(private val server: MinecraftServer) {
    private val logger = LoggerFactory.getLogger("net")

    @Throws(InterruptedException::class)
    fun bind(port: Int) {
        val future = ServerBootstrap()
            .group(createGroup())
            .channel(channelClass().java)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(InboundConnectionInitializer(server, logger))
            .bind(port)
            .sync()

        if (future.isSuccess) {
            logger.info("bound to port {} and accepting connections.", port)
        }

        future.channel().closeFuture().sync()
    }

    private fun createGroup(): EventLoopGroup {
        return if (Epoll.isAvailable()) EpollEventLoopGroup() else NioEventLoopGroup()
    }

    private fun channelClass(): KClass<out ServerChannel> {
        return if (Epoll.isAvailable()) EpollServerSocketChannel::class else NioServerSocketChannel::class
    }
}