package xyz.nkomarn.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.server.NetworkManager;

public class Bootstrap {

    private final NetworkManager networkManager;

    public Bootstrap(@NotNull NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    // TODO separate executor (if not already)
    public void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                            .addLast(new Decoder(networkManager.getProtocol()))
                            .addLast(new Encoder())
                            .addLast(new ChannelHandler(networkManager.getServer()));
                    }
                });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            if (channelFuture.isSuccess()) {
                Composter.getLogger().info("Composter is ready for connections.");
            }

            channelFuture.channel().closeFuture().sync();
        } finally {
            Composter.getLogger().info("Stopping Composter.");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
