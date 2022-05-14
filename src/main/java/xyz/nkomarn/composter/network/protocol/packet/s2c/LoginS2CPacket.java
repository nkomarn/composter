package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.util.ByteBufUtil;

public class LoginS2CPacket extends Packet<LoginS2CPacket> {

    private int entityId;
    private long seed;
    private byte dimension;

    public LoginS2CPacket() {
    }

    public LoginS2CPacket(int entityId, long seed, byte dimension) {
        this.entityId = entityId;
        this.seed = seed;
        this.dimension = dimension;
    }

    public int getEntityId() {
        return entityId;
    }

    public long getSeed() {
        return seed;
    }

    public byte getDimension() {
        return dimension;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return ByteBufUtil.writeString(Unpooled.buffer().writeInt(entityId), "")
                .writeLong(seed)
                .writeByte(dimension);
    }
}
