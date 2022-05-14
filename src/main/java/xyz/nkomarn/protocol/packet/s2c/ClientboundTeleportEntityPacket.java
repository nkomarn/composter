package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.entity.Entity;
import xyz.nkomarn.protocol.Packet;

import static xyz.nkomarn.util.ByteBufUtil.toAbsolute;

public class ClientboundTeleportEntityPacket extends Packet<ClientboundTeleportEntityPacket> {

    private int id;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;

    public ClientboundTeleportEntityPacket() {
    }

    public ClientboundTeleportEntityPacket(Entity entity) {
        this.id = entity.getId();
        this.x = toAbsolute(entity.getLocation().getX());
        this.y = toAbsolute(entity.getLocation().getY());
        this.z = toAbsolute(entity.getLocation().getZ());
        this.yaw = (byte) (entity.getLocation().getYaw() * 256.0f / 360.0f);
        this.pitch = (byte) (entity.getLocation().getPitch() * 256.0f / 360.0f);
    }


    public ClientboundTeleportEntityPacket(int id, int x, int y, int z, byte yaw, byte pitch) {
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
