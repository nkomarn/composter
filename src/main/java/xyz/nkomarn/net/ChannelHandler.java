package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Packet;

public class ChannelHandler extends SimpleChannelInboundHandler<Packet> {
    private Logger logger = Composter.getLogger();
    private static Session session;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();

        // Create new session
        session = new Session(channel);
        logger.info(String.format("New session opened- channel: %s.", channel.toString()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // Destroy session
        final Channel channel = ctx.channel();
        session = null;
        logger.info(String.format("Session closed- channel: %s.", channel.toString()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet message) {
        session.handlePacket(message);
    }

    //TODO catch exceptions
}
