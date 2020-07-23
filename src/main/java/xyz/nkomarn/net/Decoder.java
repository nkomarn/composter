package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.Protocol;

import java.io.IOException;
import java.util.List;

public class Decoder extends ReplayingDecoder<Packet<?>> {

    private final Protocol protocol;

    public Decoder(@NotNull Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) {
        final int id = buffer.readUnsignedByte();
        if (id == 0) return; // TODO this is retarded smh

        // System.out.println(String.format("Packet received: %s", id));

        Packet<?> packet = protocol.getPacketById(id, Protocol.Direction.C2S);

        if (packet == null) { // TODO this is retarded, but im also retarded so fix this later when im less retarded
            packet = protocol.getPacketById(id, Protocol.Direction.BI);

            if (packet == null) {
                // System.out.println("No packet exists for ID " + id + ".");
                return;
            }
        }

        list.add(packet.decode(buffer));
    }
}
