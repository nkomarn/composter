package xyz.nkomarn.composter.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.network.protocol.Packet;

public class ChannelHandler extends SimpleChannelInboundHandler<Packet<?>> {

    private final Composter server;
    private final Logger logger;

    public ChannelHandler(@NotNull Composter server) {
        this.server = server;
        logger = LogManager.getLogger("Channel Handler");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().attr(Session.SESSION_KEY).set(new Session(server, ctx.channel()));
        logger.info("New session opened: " + ctx.channel().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctx.channel().attr(Session.SESSION_KEY).get().getPlayer().ifPresent(player -> server.getPlayerManager().onDisconnect(player));
        logger.info(String.format("Session closed- channel: %s.", ctx.channel().toString()));
        ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<?> packet) {
        var session = ctx.channel().attr(Session.SESSION_KEY).get();

        if (session != null) {
            session.handlePacket(packet);
        }
    }

    //TODO catch exceptions
}
