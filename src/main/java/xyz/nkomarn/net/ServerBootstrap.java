package xyz.nkomarn.net;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerBootstrap {

    public void start(int port) throws InterruptedException {
        System.out.println("Starting Composter.");

        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            io.netty.bootstrap.ServerBootstrap bootstrap = new io.netty.bootstrap.ServerBootstrap();
            bootstrap.group(masterGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new InboundHandler())
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind to default port
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("Server is running.");
            }
            channelFuture.channel().closeFuture().sync();
        } finally {
            System.out.println("Stopping server.");
            workerGroup.shutdownGracefully();
            masterGroup.shutdownGracefully();
        }
    }

}
