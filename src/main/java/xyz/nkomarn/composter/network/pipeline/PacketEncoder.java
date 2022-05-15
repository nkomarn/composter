package xyz.nkomarn.composter.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.WrappedBuff;
import xyz.nkomarn.composter.network.protocol.Protocol;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundPacket;

public class PacketEncoder extends MessageToByteEncoder<ClientboundPacket> {

    private final Connection connection;

    public PacketEncoder(Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ClientboundPacket packet, ByteBuf buff) throws Exception {
        var packetId = Protocol.packetId(packet);

        if (packetId == -1) {
            throw new IllegalStateException("Packet " + packet.getClass() + " is not registered.");
        }

        buff.writeByte(packetId);
        packet.encode(WrappedBuff.wrap(buff));
    }
}
