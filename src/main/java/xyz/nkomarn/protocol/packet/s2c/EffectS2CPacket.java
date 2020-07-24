package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class EffectS2CPacket extends Packet<EffectS2CPacket> {

    private int id;
    private int x;
    private byte y;
    private int z;
    private int data;

    public EffectS2CPacket() {
    }

    public EffectS2CPacket(int id, int x, byte y, int z, int data) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.data = data;
    }

    @Override
    public int getId() {
        return 0x3D;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer()
                .writeInt(id)
                .writeInt(x)
                .writeByte(y)
                .writeInt(z)
                .writeInt(data);
    }
}
