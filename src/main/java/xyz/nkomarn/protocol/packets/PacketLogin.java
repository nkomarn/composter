package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketLogin extends Packet {
    private final int id, dimension;
    private final String username;
    private final long seed;

    public PacketLogin(final int id, final String username, final long seed, final int dimension) {
        this.id = id;
        this.username = username;
        this.seed = seed;
        this.dimension = dimension;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public long getSeed() {
        return this.seed;
    }

    public int getDimension() {
        return this.dimension;
    }
}
