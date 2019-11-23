package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketChat extends Packet {
    private final String message;

    public PacketChat(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
