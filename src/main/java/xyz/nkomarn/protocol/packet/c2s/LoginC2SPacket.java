package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class LoginC2SPacket extends Packet<LoginC2SPacket> {

    private int protocol;
    private String username;

    public LoginC2SPacket() {
    }

    public LoginC2SPacket(int protocol, @NotNull String username) {
        this.protocol = protocol;
        this.username = username;
    }

    public int getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public LoginC2SPacket decode(@NotNull ByteBuf buffer) {
        return new LoginC2SPacket(buffer.readInt(), ByteBufUtil.readString(buffer));
    }
}
