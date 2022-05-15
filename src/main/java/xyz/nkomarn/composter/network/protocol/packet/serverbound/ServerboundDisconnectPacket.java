package xyz.nkomarn.composter.network.protocol.packet.serverbound;

import xyz.nkomarn.composter.network.protocol.handler.PacketHandler;

public class ServerboundDisconnectPacket implements ServerboundPacket {

    @Override
    public void handle(PacketHandler handler) {
        handler.handleDisconnect(this);
    }
}
