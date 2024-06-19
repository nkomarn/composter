package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.network.protocol.Packet;

import static xyz.nkomarn.composter.util.ByteBufUtil.toAbsoluteInteger;

public class ClientboundAddEntityPacket extends Packet<ClientboundAddEntityPacket> {
    private int id;
    private byte entityType;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;

    public ClientboundAddEntityPacket() {
    }

    public ClientboundAddEntityPacket(Entity entity) {
        this.id = entity.getId();
        this.entityType = 90;
        this.x = toAbsoluteInteger(entity.getX());
        this.y = toAbsoluteInteger(entity.getY());
        this.z = toAbsoluteInteger(entity.getZ());
        this.yaw = (byte) entity.getYaw();
        this.pitch = (byte) entity.getPitch();
    }

    @Override
    public int getId() {
        return 0x18;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer().writeInt(id)
                .writeByte(entityType)
                .writeInt(x)
                .writeInt(y)
                .writeInt(z)
                .writeByte(yaw)
                .writeByte(pitch)
                .writeByte(0x7F);
    }
}
