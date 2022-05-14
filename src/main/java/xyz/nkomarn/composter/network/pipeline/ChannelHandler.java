package xyz.nkomarn.composter.network.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.Packet;

import static xyz.nkomarn.composter.network.Connection.SESSION_KEY;

public class ChannelHandler extends SimpleChannelInboundHandler<Packet<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger("Channel Handler");
    private final Composter server;

    public ChannelHandler(@NotNull Composter server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.channel().attr(Connection.SESSION_KEY).set(new Connection(server, ctx.channel()));
        LOGGER.info("New session opened: " + ctx.channel().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        channelConnection(ctx).getPlayer().ifPresent(player -> server.getPlayerManager().onDisconnect(player));
        LOGGER.info(String.format("Session closed- channel: %s.", ctx.channel().toString()));
        // ctx.channel().close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet<?> packet) {
        var session = ctx.channel().attr(Connection.SESSION_KEY).get();

        if (session != null) {
            session.handlePacket(packet);
        }
    }

    private Connection channelConnection(ChannelHandlerContext context) {
        return context.channel().attr(SESSION_KEY).get();
    }
    //TODO catch exceptions
}
