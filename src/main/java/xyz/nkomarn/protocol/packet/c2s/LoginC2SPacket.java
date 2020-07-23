package xyz.nkomarn.protocol.c2s;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class LoginC2SPacket implements Packet<LoginC2SPacket> {

    private final int protocol;
    private final String username;

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
    public @NotNull ByteBuf encode(LoginC2SPacket packet) {
        return Unpooled.EMPTY_BUFFER; // C2S
    }

    @Override
    public LoginC2SPacket decode(@NotNull ByteBuf buffer) {
        return new LoginC2SPacket(buffer.readInt(), ByteBufUtil.readString(buffer));
    }
}
