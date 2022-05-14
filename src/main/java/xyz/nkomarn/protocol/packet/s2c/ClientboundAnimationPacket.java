package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.entity.Entity;
import xyz.nkomarn.protocol.Packet;

public class ClientboundAnimationPacket extends Packet<ClientboundAnimationPacket> {

    private Entity entity;
    private int animationId;

    public ClientboundAnimationPacket() {
    }

    public ClientboundAnimationPacket(Entity entity, int animationId) {
        this.entity = entity;
        this.animationId = animationId;
    }

    @Override
    public int getId() {
        return 0x12;
    }

    @Override
    public ByteBuf encode() {
        return Unpooled.buffer()
                .writeInt(entity.getId())
                .writeByte(animationId);
    }
}
