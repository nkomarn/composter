package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketDisconnect extends Packet {
    private final String message;

    public PacketDisconnect(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
