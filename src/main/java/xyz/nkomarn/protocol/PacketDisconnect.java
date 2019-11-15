package xyz.nkomarn.protocol;

public abstract class PacketDisconnect extends Packet {

    private final String reason;

    public PacketDisconnect(final String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

}
