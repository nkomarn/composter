package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.network.protocol.Packet;

import static xyz.nkomarn.composter.util.ByteBufUtil.toAbsoluteInteger;

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

        var pos = entity.getPos();
        this.x = toAbsoluteInteger(pos.getX());
        this.y = toAbsoluteInteger(pos.getY());
        this.z = toAbsoluteInteger(pos.getZ());
        this.yaw = (byte) (entity.getYaw() * 256F / 360F);
        this.pitch = (byte) (entity.getPitch() * 256F / 360F);

        System.out.printf("(%s, %s, %s)\n", x, y, z);
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
