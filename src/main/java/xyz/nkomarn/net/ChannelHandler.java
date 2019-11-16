package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import xyz.nkomarn.Composter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelHandler extends ChannelInboundHandlerAdapter {

    Logger logger = Composter.getLogger();

    public void channelActive(ChannelHandlerContext context) {
        // Ran when the client connects
        Channel channel = context.channel();

        // Create new session
        final Session session = new Session(channel);
        logger.log(Level.INFO, String.format("New session (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    public void channelInactive(ChannelHandlerContext context) {
        // Destroy session
        Channel channel = context.channel();
        SessionManager.closeSession(channel);
        logger.log(Level.INFO, String.format("Session closed (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }
}
