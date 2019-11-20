package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import xyz.nkomarn.Composter;

public class ChannelHandler implements io.netty.channel.ChannelHandler {
    private Logger logger = Composter.getLogger();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        final Channel channel = ctx.channel();

        // Create new session
        new Session(channel);
        logger.info(String.format("New session (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // Destroy session
        final Channel channel = ctx.channel();
        SessionManager.closeSession(channel);
        logger.info(String.format("Session closed (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
