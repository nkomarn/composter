package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.util.ByteBufUtil;

public class DisconnectS2CPacket extends Packet<DisconnectS2CPacket> {

    private String message;

    public DisconnectS2CPacket() {
    }

    public DisconnectS2CPacket(@NotNull String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getId() {
        return 0xFF;
    }

    @Override
    public @NotNull ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        ByteBufUtil.writeString(buffer, message);
        return buffer;
    }
}
