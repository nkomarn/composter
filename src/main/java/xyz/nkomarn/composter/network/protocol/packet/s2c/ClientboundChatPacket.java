package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.util.ByteBufUtil;

public class ClientboundChatPacket extends Packet<ClientboundChatPacket> {

    private static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder().build();
    private Component message;

    public ClientboundChatPacket() {
    }

    public ClientboundChatPacket(@NotNull Component message) {
        this.message = message;
    }

    public Component getMessage() {
        return message;
    }

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public ByteBuf encode() {
        return ByteBufUtil.writeString(Unpooled.buffer(), SERIALIZER.serialize(message));
    }
}
