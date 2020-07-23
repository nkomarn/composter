package xyz.nkomarn.protocol.packet.bi;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class KeepAliveBiPacket extends Packet<KeepAliveBiPacket> {

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public KeepAliveBiPacket decode(@NotNull ByteBuf buffer) {
        return new KeepAliveBiPacket();
    }
}
