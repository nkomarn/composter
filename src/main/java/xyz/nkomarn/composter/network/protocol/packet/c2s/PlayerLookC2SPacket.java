package xyz.nkomarn.composter.network.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class PlayerLookC2SPacket extends Packet<PlayerLookC2SPacket> {

    private float yaw;
    private float pitch;
    private boolean grounded;

    public PlayerLookC2SPacket() {
    }

    public PlayerLookC2SPacket(float yaw, float pitch, boolean grounded) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.grounded = grounded;
    }

    @Override
    public int getId() {
        return 0x0C;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isGrounded() {
        return grounded;
    }

    @Override
    public PlayerLookC2SPacket decode(@NotNull ByteBuf buffer) {
        return new PlayerLookC2SPacket(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean());
    }
}
