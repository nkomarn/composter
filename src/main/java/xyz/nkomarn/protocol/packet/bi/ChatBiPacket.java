package xyz.nkomarn.protocol.packet.bi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class ChatBiPacket extends Packet<ChatBiPacket> {

    private String message;

    public ChatBiPacket() {
    }

    public ChatBiPacket(@NotNull String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return ByteBufUtil.writeString(Unpooled.buffer(), message);
    }

    @Override
    public ChatBiPacket decode(@NotNull ByteBuf buffer) {
        return new ChatBiPacket(ByteBufUtil.readString(buffer));
    }
}
