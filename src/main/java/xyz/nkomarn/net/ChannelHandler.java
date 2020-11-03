package xyz.nkomarn.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Packet;

import java.util.Optional;

public class ChannelHandler extends SimpleChannelInboundHandler<Packet<?>> {

    private final Composter server;
    private final Logger logger;
    private Session session;

    public ChannelHandler(@NotNull Composter server) {
        this.server = server;
        logger = LogManager.getLogger("Channel Handler");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        session = new Session(server, ctx.channel());
        ctx.channel().attr(Session.SESSION_KEY).set(session);
        logger.info("New session opened: " + ctx.channel().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        session.getPlayer().ifPresent(player -> server.getPlayerManager().onDisconnect(player));
        logger.info(String.format("Session closed- channel: %s.", ctx.channel().toString()));
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<?> packet) {
        if (session != null) {
            session.handlePacket(packet);
        }
    }

    //TODO catch exceptions
}
