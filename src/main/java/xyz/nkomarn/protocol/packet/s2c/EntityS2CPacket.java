package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class EntityS2CPacket extends Packet<EntityS2CPacket> {

    private int id;

    public EntityS2CPacket() {
    }

    public EntityS2CPacket(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return 0x1E;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer()
                .writeInt(id);
    }
}
