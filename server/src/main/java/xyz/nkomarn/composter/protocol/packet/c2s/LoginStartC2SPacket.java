package xyz.nkomarn.composter.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;
import xyz.nkomarn.composter.util.DataTypeUtils;

public class LoginStartC2SPacket extends Packet<LoginStartC2SPacket> {

    private String username;

    public LoginStartC2SPacket() {
    }

    public LoginStartC2SPacket(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public LoginStartC2SPacket decode(@NotNull ByteBuf buffer) {
        return new LoginStartC2SPacket(DataTypeUtils.readString(buffer));
    }
}
