package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketHandshake extends Packet {
    private final String username;

    public PacketHandshake(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
