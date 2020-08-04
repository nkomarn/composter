package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class EntityTeleportS2CPacket extends Packet<EntityTeleportS2CPacket> {

    private int id;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;

    public EntityTeleportS2CPacket() {
    }

    public EntityTeleportS2CPacket(int id, int x, int y, int z, byte yaw, byte pitch) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public int getId() {
        return 0x22;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer()
                .writeInt(id)
                .writeInt(x)
                .writeInt(y)
                .writeInt(z)
                .writeByte(yaw)
                .writeByte(pitch);
    }
}
