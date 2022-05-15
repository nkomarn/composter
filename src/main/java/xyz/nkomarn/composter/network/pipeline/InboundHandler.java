package xyz.nkomarn.composter.network.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundPacket;

public class InboundHandler extends SimpleChannelInboundHandler<ServerboundPacket> {

    private final Connection connection;

    public InboundHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, ServerboundPacket packet) throws Exception {
        System.out.println("Received packet " + packet.getClass().getSimpleName() + ".");
        packet.handle(connection.packetHandler());
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) throws Exception {
        connection.handleDisconnection();
    }
}
