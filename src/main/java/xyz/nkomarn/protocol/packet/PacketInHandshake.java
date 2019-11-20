package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.protocol.Packet;

public class PacketInHandshake implements Packet<PacketInHandshake> {


    @Override
    public ByteBuf encode(PacketInHandshake packet) {
        return null;
    }

    @Override
    public PacketInHandshake decode(ByteBuf data) {
        return null;
    }
}
