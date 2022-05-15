package xyz.nkomarn.composter.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.network.pipeline.InboundInitializer;
import xyz.nkomarn.composter.server.MinecraftServer;

import static io.netty.channel.ChannelOption.TCP_NODELAY;

public class NetworkController {

    private static final Logger LOGGER = LoggerFactory.getLogger("Network");
    private final ServerBootstrap bootstrap;

    public NetworkController(MinecraftServer server) {
        this.bootstrap = new ServerBootstrap()
                .group(createGroup())
                .channel(channelClass())
                .childOption(TCP_NODELAY, true)
                .childHandler(new InboundInitializer(server));
    }

    public void bind(int port) throws InterruptedException {
        var future = bootstrap.bind(port).sync();

        if (future.isSuccess()) {
            LOGGER.info("Bound to port {} and accepting connections.", port);
        }

        future.channel().closeFuture().sync();
    }

    private EventLoopGroup createGroup() {
        return Epoll.isAvailable()
                ? new EpollEventLoopGroup()
                : new NioEventLoopGroup();
    }

    private Class<? extends ServerChannel> channelClass() {
        return Epoll.isAvailable()
                ? EpollServerSocketChannel.class
                : NioServerSocketChannel.class;
    }
}
