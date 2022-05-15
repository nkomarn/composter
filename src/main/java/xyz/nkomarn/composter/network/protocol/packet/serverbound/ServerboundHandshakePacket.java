package xyz.nkomarn.composter.network.protocol.packet.serverbound;

import xyz.nkomarn.composter.network.WrappedBuff;
import xyz.nkomarn.composter.network.protocol.handler.PacketHandler;

public class ServerboundHandshakePacket implements ServerboundPacket {

    private String username;

    public String username() {
        return username;
    }

    @Override
    public void decode(WrappedBuff buff) {
        username = buff.readString();
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handleHandshake(this);
    }
}
