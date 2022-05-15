package xyz.nkomarn.composter.network.protocol.handler;

import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundDisconnectPacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundHandshakePacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundLoginPacket;

public interface PacketHandler {

    void handleHandshake(ServerboundHandshakePacket packet);

    void handleLogin(ServerboundLoginPacket packet);

    void handleDisconnect(ServerboundDisconnectPacket packet);
}
