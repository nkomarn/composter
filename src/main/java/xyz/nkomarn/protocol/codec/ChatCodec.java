package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketChat;
import xyz.nkomarn.util.ByteBufUtil;

import java.io.IOException;

public class ChatCodec extends Codec<PacketChat> {
    public ChatCodec() {
        super(PacketChat.class, 0x03);
    }

    @Override
    public ByteBuf encode(PacketChat packet) {
        ByteBuf buffer = Unpooled.buffer();
        ByteBufUtil.writeString(buffer, packet.getMessage());
        return buffer;
    }

    @Override
    public PacketChat decode(ByteBuf buffer) {
        final String message = ByteBufUtil.readString(buffer);
        Composter.getLogger().info("Received chat: " + message);
        return new PacketChat(message);
    }
}
