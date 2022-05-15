package xyz.nkomarn.composter.network.protocol.packet.clientbound;

import xyz.nkomarn.composter.network.WrappedBuff;

public class ClientboundLoginPacket implements ClientboundPacket {

    private final int entityId;
    private final long seed;
    private final byte dimension;

    public ClientboundLoginPacket(int entityId, long seed, byte dimension) {
        this.entityId = entityId;
        this.seed = seed;
        this.dimension = dimension;
    }

    @Override
    public void encode(WrappedBuff buff) {
        buff.writeInt(entityId)
                .writeString("")
                .writeLong(seed)
                .writeByte(dimension);
    }
}
