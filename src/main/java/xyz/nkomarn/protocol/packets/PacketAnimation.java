package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketAnimation extends Packet {
    private int entityId;
    private int animate;

    public PacketAnimation(int entityId, int animate) {
        this.entityId = entityId;
        this.animate = animate;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getAnimate() {
        return animate;
    }
}
