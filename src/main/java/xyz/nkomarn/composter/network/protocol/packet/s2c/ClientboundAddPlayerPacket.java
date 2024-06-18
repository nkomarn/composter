package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.util.ByteBufUtil;

import static xyz.nkomarn.composter.util.ByteBufUtil.toAbsolute;

public class ClientboundAddPlayerPacket extends Packet<ClientboundAddPlayerPacket> {

    private int id;
    private String name;
    private int x;
    private int y;
    private int z;
    private byte rotation;
    private byte pitch;
    private short item;

    public ClientboundAddPlayerPacket() {
    }

    public ClientboundAddPlayerPacket(Player player) {
        this.id = player.getId();
        this.name = player.getUsername();

        var pos = player.getPos();
        this.x = toAbsolute(player.getX());
        this.y = toAbsolute(player.getY());
        this.z = toAbsolute(player.getZ());
        this.rotation = (byte) 0;
        this.pitch = (byte) player.getPitch();
        this.item = (short) 0;
    }

    public ClientboundAddPlayerPacket(int id, String name, int x, int y, int z, byte rotation, byte pitch, short item) {
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
