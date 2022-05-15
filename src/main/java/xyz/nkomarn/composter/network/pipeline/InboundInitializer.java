package xyz.nkomarn.composter.network.pipeline;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.server.MinecraftServer;

public class InboundInitializer extends ChannelInitializer<SocketChannel> {

    private final MinecraftServer server;

    public InboundInitializer(MinecraftServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        var connection = new Connection(server, channel);

        channel.pipeline()
                .addLast(new PacketDecoder(connection))
                .addLast(new PacketEncoder(connection))
                .addLast(new InboundHandler(connection));
    }
}
