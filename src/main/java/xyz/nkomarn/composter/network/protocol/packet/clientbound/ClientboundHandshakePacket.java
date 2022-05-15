package xyz.nkomarn.composter.network.protocol.packet.clientbound;

import xyz.nkomarn.composter.network.WrappedBuff;

public class ClientboundHandshakePacket implements ClientboundPacket {

    private final String hash;

    public ClientboundHandshakePacket(String hash) {
        this.hash = hash;
    }

    @Override
    public void encode(WrappedBuff buff) {
        buff.writeString(hash);
    }
}
