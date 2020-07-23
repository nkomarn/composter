package xyz.nkomarn.protocol.bidirectional;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class KeepAliveBiPacket implements Packet<KeepAliveBiPacket> {

    @Override
    public @NotNull ByteBuf encode(KeepAliveBiPacket packet) {
        return Unpooled.EMPTY_BUFFER;
    }

    @Override
    public KeepAliveBiPacket decode(@NotNull ByteBuf buffer) {
        return new KeepAliveBiPacket();
    }
}
