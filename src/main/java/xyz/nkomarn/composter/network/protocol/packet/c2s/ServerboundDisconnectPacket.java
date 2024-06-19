package xyz.nkomarn.composter.network.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class ServerboundDisconnectPacket extends Packet<ServerboundDisconnectPacket> {

    public ServerboundDisconnectPacket() {
    }

    @Override
    public int getId() {
        return 0xFF;
    }

    @Override
    public ServerboundDisconnectPacket decode(@NotNull ByteBuf buffer) {
        return this;
    }
}
