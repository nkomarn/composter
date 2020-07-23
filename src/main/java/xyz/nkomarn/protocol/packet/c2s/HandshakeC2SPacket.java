package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class HandshakeC2SPacket extends Packet<HandshakeC2SPacket> {

    private String username;

    public HandshakeC2SPacket() {
    }

    public HandshakeC2SPacket(@NotNull String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    public HandshakeC2SPacket decode(@NotNull ByteBuf buffer) {
        return new HandshakeC2SPacket(ByteBufUtil.readString(buffer));
    }
}
