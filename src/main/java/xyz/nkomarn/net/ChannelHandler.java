package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import xyz.nkomarn.Composter;

public class ChannelHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = Composter.getLogger();

    // Ran when the client connects
    public void channelActive(ChannelHandlerContext context) {
        final Channel channel = context.channel();

        // Create new session
        new Session(channel);
        logger.info(String.format("New session (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    // Destroy session
    public void channelInactive(ChannelHandlerContext context) {
        final Channel channel = context.channel();
        SessionManager.closeSession(channel);
        logger.info(String.format("Session closed (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }
}
