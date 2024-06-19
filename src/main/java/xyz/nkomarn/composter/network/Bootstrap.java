package xyz.nkomarn.composter.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.pipeline.ChannelHandler;
import xyz.nkomarn.composter.network.pipeline.Decoder;
import xyz.nkomarn.composter.network.pipeline.Encoder;
import xyz.nkomarn.composter.server.NetworkManager;

public class Bootstrap {

    private final Logger logger;
    private final NetworkManager networkManager;

    public Bootstrap(@NotNull NetworkManager networkManager) {
        this.logger = LogManager.getLogger("Network Listener");
        this.networkManager = networkManager;
    }

    public void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // TODO use Epoll if possible!
//                     .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new Decoder())
                                    .addLast(new Encoder())
                                    .addLast(new ChannelHandler(networkManager.getServer()));
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            if (channelFuture.isSuccess()) {
                logger.info("Composter is ready for connections.");
            }

            channelFuture.channel().closeFuture().sync();
        } finally {
            logger.info("Closing network listener.");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
