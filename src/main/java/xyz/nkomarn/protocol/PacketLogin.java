package xyz.nkomarn.protocol;

public final class PacketLogin extends Packet {

    private final int version;
    private final String username;

    public PacketLogin(final int version, final String username) {
        this.version = version;
        this.username = username;
    }

    public int getVersion() {
        return this.version;
    }

    public String getUserame() {
        return this.username;
    }

}
