package xyz.nkomarn.composter.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;
import xyz.nkomarn.composter.util.DataTypeUtils;

import java.util.Arrays;
import java.util.UUID;

public class LoginSuccessS2CPacket extends Packet<LoginSuccessS2CPacket> {

    private UUID uuid;
    private String username;

    public LoginSuccessS2CPacket() {
    }

    public LoginSuccessS2CPacket(@NotNull UUID uuid, @NotNull String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    @NotNull
    public ByteBuf encode() {
        var buffer = Unpooled.buffer();
        DataTypeUtils.writeUuid(buffer, uuid);
        DataTypeUtils.writeString(buffer, username);
        return buffer;
    }
}
