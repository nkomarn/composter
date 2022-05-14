package xyz.nkomarn.composter.network.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.util.ByteBufUtil;

public class ServerboundChatPacket extends Packet<ServerboundChatPacket> {

    private String rawMessage;

    public ServerboundChatPacket() {
    }

    public ServerboundChatPacket(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    @NotNull
    public String rawMessage() {
        return rawMessage;
    }

    @Override
    public int getId() {
        return 0x03;
    }

    @Override
    public ServerboundChatPacket decode(@NotNull ByteBuf buffer) {
        return new ServerboundChatPacket(ByteBufUtil.readString(buffer));
    }
}
