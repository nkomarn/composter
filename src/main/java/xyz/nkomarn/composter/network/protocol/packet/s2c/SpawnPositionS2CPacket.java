package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class SpawnPositionS2CPacket extends Packet<SpawnPositionS2CPacket> {

    private int x;
    private int y;
    private int z;

    public SpawnPositionS2CPacket() {
    }

    public SpawnPositionS2CPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getId() {
        return 0x06;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer()
                .writeInt(x)
                .writeInt(y)
                .writeInt(z);
    }
}
