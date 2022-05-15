package xyz.nkomarn.composter.network.protocol.packet.serverbound;

import xyz.nkomarn.composter.network.WrappedBuff;
import xyz.nkomarn.composter.network.protocol.handler.PacketHandler;
import xyz.nkomarn.composter.network.protocol.packet.Packet;

public interface ServerboundPacket extends Packet {

    default void decode(WrappedBuff buff) {
    }

    void handle(PacketHandler handler);
}
