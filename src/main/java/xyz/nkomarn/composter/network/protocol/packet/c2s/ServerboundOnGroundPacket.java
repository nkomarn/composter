package xyz.nkomarn.composter.network.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class ServerboundOnGroundPacket extends Packet<ServerboundOnGroundPacket> {

    private boolean onGround;

    public ServerboundOnGroundPacket() {
    }

    public ServerboundOnGroundPacket(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public int getId() {
        return 0x0A;
    }

    @Override
    public ServerboundOnGroundPacket decode(@NotNull ByteBuf buffer) {
        return new ServerboundOnGroundPacket(buffer.readBoolean());
    }
}
