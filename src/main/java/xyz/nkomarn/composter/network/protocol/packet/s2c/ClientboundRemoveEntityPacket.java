package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.network.protocol.Packet;

public class ClientboundRemoveEntityPacket extends Packet<ClientboundRemoveEntityPacket> {

    private int entityId;

    public ClientboundRemoveEntityPacket() {
    }

    public ClientboundRemoveEntityPacket(Entity entity) {
        this.entityId = entity.getId();
    }

    @Override
    public int getId() {
        return 0x1D;
    }

    @Override
    public ByteBuf encode() {
        return Unpooled.buffer().writeInt(entityId);
    }
}
