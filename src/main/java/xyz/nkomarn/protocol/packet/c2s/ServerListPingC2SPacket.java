package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class ServerListPingC2SPacket extends Packet<ServerListPingC2SPacket> {

    public ServerListPingC2SPacket() {
    }

    @Override
    public int getId() {
        return 0xFE;
    }

    @Override
    public ServerListPingC2SPacket decode(@NotNull ByteBuf buffer) {
        return new ServerListPingC2SPacket();
    }
}
