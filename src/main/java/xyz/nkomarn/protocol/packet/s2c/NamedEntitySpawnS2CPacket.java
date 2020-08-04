package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class NamedEntitySpawnS2CPacket extends Packet<NamedEntitySpawnS2CPacket> {

    private int id;
    private String name;
    private int x;
    private int y;
    private int z;
    private byte rotation;
    private byte pitch;
    private short item;

    public NamedEntitySpawnS2CPacket() {
    }

    public NamedEntitySpawnS2CPacket(int id, @NotNull String name, int x, int y, int z, byte rotation, byte pitch, short item) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.pitch = pitch;
        this.item = item;
    }

    @Override
    public int getId() {
        return 0x14;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return ByteBufUtil.writeString(Unpooled.buffer().writeInt(id), name)
                .writeInt(x)
                .writeInt(y)
                .writeInt(z)
                .writeByte(rotation)
                .writeByte(pitch)
                .writeShort(item);
    }
}
