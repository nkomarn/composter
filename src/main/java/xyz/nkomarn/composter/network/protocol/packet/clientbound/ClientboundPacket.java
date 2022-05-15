package xyz.nkomarn.composter.network.protocol.packet.clientbound;

import xyz.nkomarn.composter.network.WrappedBuff;
import xyz.nkomarn.composter.network.protocol.packet.Packet;

public interface ClientboundPacket extends Packet {

    default void encode(WrappedBuff buff) {
    }
}
