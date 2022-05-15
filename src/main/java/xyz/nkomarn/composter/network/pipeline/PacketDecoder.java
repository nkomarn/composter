package xyz.nkomarn.composter.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.WrappedBuff;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundPacket;

import java.util.List;

import static xyz.nkomarn.composter.network.FlowDirection.SERVERBOUND;

public class PacketDecoder extends ReplayingDecoder<ServerboundPacket> {

    private final Connection connection;

    public PacketDecoder(Connection connection) {
        this.connection = connection;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buff, List<Object> decoded) throws Exception {
        short packetId = buff.readUnsignedByte();
        var packet = (ServerboundPacket) connection.state().packetById(SERVERBOUND, packetId);

        if (packet == null) {
            return;
        }

        packet.decode(WrappedBuff.wrap(buff));
        decoded.add(packet);
    }
}
