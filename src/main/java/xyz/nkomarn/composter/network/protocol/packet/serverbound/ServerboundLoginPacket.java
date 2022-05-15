package xyz.nkomarn.composter.network.protocol.packet.serverbound;

import xyz.nkomarn.composter.network.WrappedBuff;
import xyz.nkomarn.composter.network.protocol.handler.PacketHandler;

public class ServerboundLoginPacket implements ServerboundPacket {

    private int protocolVersion;
    private String username;

    public int protocolVersion() {
        return protocolVersion;
    }

    public String username() {
        return username;
    }

    @Override
    public void decode(WrappedBuff buff) {
        protocolVersion = buff.readInt();
        username = buff.readString();
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handleLogin(this);
    }
}
