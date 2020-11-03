package xyz.nkomarn.protocol.packet.c2s;

import xyz.nkomarn.protocol.Packet;

public class StatusRequestC2SPacket extends Packet<StatusRequestC2SPacket> {

    public StatusRequestC2SPacket() {
    }

    @Override
    public int getId() {
        return 0x00;
    }
}
