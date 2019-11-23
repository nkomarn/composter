package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Packet;

public class ChannelHandler extends SimpleChannelInboundHandler<Packet> {
    private Logger logger = Composter.getLogger();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();

        // Create new session
        new Session(channel);
        logger.info(String.format("New session (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // Destroy session
        final Channel channel = ctx.channel();
        SessionManager.closeSession(channel);
        logger.info(String.format("Session closed (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet message) {
        Session session = SessionManager.getSession(ctx.channel());
        session.queuePacket(message);
    }

    //TODO catch exceptions
}
