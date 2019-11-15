package xyz.nkomarn.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import xyz.nkomarn.Composter;

public class Bootstrap {

    /*
        Starts the Netty server
     */
    public void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override // TODO move this into a dedicated class (maybe?)
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new Decoder());
                        channel.pipeline().addLast(new ChannelHandler());
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) Composter.getLogger().info("Composter is up and running.");
            channelFuture.channel().closeFuture().sync();
        } finally {
            Composter.getLogger().info("Stopping Composter.");
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}
