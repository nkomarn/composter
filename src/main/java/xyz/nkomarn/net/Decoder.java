package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Direction;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.Protocol;
import xyz.nkomarn.util.ByteBufUtil;

import java.util.List;

public class Decoder extends ReplayingDecoder<Packet<?>> {

    private final Protocol protocol;

    public Decoder(@NotNull Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> list) {
        var session = ctx.attr(Session.SESSION_KEY).get();
        int id = ByteBufUtil.readVarInt(buffer);
        System.out.println(String.format("Packet received: %s. State: %s", id, session.getState()));

        var packet = session.getState().getPacketById(Direction.C2S, id);

        /*if (packet == null) { // TODO this is retarded, but im also retarded so fix this later when im less retarded
            packet = protocol.getPacketById(id, Protocol.Direction.BI);

            if (packet == null) {
                System.out.println("No packet exists for ID " + id + ".");
                return;
            }
        }*/

        if (packet == null) {
            System.out.println(" -> Null packet to handle.");
            return;
        }

        list.add(packet.decode(buffer));
    }
}
