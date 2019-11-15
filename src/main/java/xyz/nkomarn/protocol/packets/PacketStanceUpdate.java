package xyz.nkomarn.protocol.packets;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.protocol.Packet;

public class PacketStanceUpdate extends Packet {
    @Override
    public ByteBuf encode(Object message) {
        return null;
    }

    @Override
    public Object decode(ByteBuf buffer) {
        return null;
    }
    /**
     * TODO 0x1B
     * Wiki has no data on this packet
     */
}
