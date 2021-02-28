package xyz.nkomarn.composter.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;

public class StatusRequestC2SPacket extends Packet<StatusRequestC2SPacket> {

    public StatusRequestC2SPacket() {
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public StatusRequestC2SPacket decode(@NotNull ByteBuf buffer) {
        return new StatusRequestC2SPacket();
    }
}
