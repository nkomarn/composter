package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class ClientboundSetTimePacket extends Packet<ClientboundSetTimePacket> {

    private long time;

    public ClientboundSetTimePacket() {
    }

    public ClientboundSetTimePacket(long time) {
        this.time = time;
    }

    @Override
    public int getId() {
        return 0x04;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer()
                .writeLong(time);
    }
}
