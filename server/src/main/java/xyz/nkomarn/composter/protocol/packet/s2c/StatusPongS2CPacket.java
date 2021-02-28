package xyz.nkomarn.composter.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;

public class StatusPongS2CPacket extends Packet<StatusPongS2CPacket> {

    private long timestamp;

    public StatusPongS2CPacket() {
    }

    public StatusPongS2CPacket(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    @NotNull
    public ByteBuf encode() {
        return Unpooled.buffer()
                .writeLong(timestamp);
    }
}
