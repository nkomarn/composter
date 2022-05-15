package xyz.nkomarn.composter.network.protocol.packet.clientbound;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import xyz.nkomarn.composter.network.WrappedBuff;

public class ClientboundDisconnectPacket implements ClientboundPacket {

    private final Component reason;

    public ClientboundDisconnectPacket(Component reason) {
        this.reason = reason;
    }

    @Override
    public void encode(WrappedBuff buff) {
        buff.writeString(PlainTextComponentSerializer.plainText().serialize(reason));
    }
}
