package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class LoginS2CPacket extends Packet<LoginS2CPacket> {

    private int entityId;
    private long seed;
    private byte dimension, difficulty;

    public LoginS2CPacket() {
    }

    public LoginS2CPacket(int entityId, long seed, byte dimension, byte difficulty) {
        this.entityId = entityId;
        this.seed = seed;
        this.dimension = dimension;
        this.difficulty = difficulty;
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

    public byte getDifficulty() {
        return difficulty;
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
