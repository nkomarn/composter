package xyz.nkomarn.protocol;

public abstract class PacketHandshake extends Packet {

    private final String username;

    public PacketHandshake(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}
