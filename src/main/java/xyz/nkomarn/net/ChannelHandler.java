package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import xyz.nkomarn.Composter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChannelHandler extends ChannelInboundHandlerAdapter {

    Logger logger = Composter.getLogger();

    // Ran when the client connects
    public void channelActive(ChannelHandlerContext context) {
        Channel channel = context.channel();

        // Create new session
        final Session session = new Session(channel);
        logger.log(Level.INFO, String.format("New session (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    // Destroy session
    public void channelInactive(ChannelHandlerContext context) {
        Channel channel = context.channel();
        SessionManager.closeSession(channel);
        logger.log(Level.INFO, String.format("Session closed (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }
}
