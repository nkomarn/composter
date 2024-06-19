package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.util.ByteBufUtil;

import static xyz.nkomarn.composter.util.ByteBufUtil.toAbsoluteInteger;

public class ClientboundAddPlayerPacket extends Packet<ClientboundAddPlayerPacket> {

    private int id;
    private String name;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private short item;

    public ClientboundAddPlayerPacket() {
    }

    public ClientboundAddPlayerPacket(Player player) {
        this.id = player.getId();
        this.name = player.getUsername();
        this.x = toAbsoluteInteger(player.getX());
        this.y = toAbsoluteInteger(player.getY());
        this.z = toAbsoluteInteger(player.getZ());
        this.yaw = (byte) (player.getYaw() * 256.0F / 360.0F);
        this.pitch = (byte) (player.getPitch() * 256.0F / 360.0F);
        this.item = (short) 0;
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
                .writeByte(yaw)
                .writeByte(pitch)
                .writeShort(item);
    }
}
