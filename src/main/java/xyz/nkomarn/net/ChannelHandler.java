package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelHandler extends ChannelInboundHandlerAdapter {
    public void channelActive(ChannelHandlerContext context) {
        // Ran when the client connects
        Channel channel = context.channel();

        // Create new session
        final Session session = new Session(channel);
        System.out.println(String.format("New session (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    public void channelInactive(ChannelHandlerContext context) {
        // Destroy session
        Channel channel = context.channel();
        SessionManager.closeSession(channel);
        System.out.println(String.format("Session closed (%s total). Channel: %s.",
            String.valueOf(SessionManager.sessionCount()), channel.toString()));
    }

    public void channelRead(ChannelHandlerContext context, Object message) {
        final Session session = SessionManager.getSession(context.channel());
        // TODO handler stuff
    }
}
