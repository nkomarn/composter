package xyz.nkomarn.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class TCPChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new InboundHandler());
    }

}
