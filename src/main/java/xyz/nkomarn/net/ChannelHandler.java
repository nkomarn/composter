package xyz.nkomarn.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Packet;

public class ChannelHandler extends SimpleChannelInboundHandler<Packet> {
    private Logger logger = Composter.getLogger();
    private Session session;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // Create new session
        session = new Session(ctx.channel());
        logger.info(String.format("New session opened- channel: %s.", ctx.channel().toString()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info(String.format("Session closed- channel: %s.", ctx.channel().toString()));
        Composter.brutallyMurderPlayer(session.getPlayer());
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet message) {
        if (session != null) session.handlePacket(message);
    }

    //TODO catch exceptions
}
