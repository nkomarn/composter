package xyz.nkomarn.composter.network.protocol.packet.c2s;

import xyz.nkomarn.composter.network.protocol.Packet;

public class ServerboundDisconnectPacket extends Packet<ServerboundDisconnectPacket> {

    public ServerboundDisconnectPacket() {
    }

    @Override
    public int getId() {
        return 0xFF;
    }
}
