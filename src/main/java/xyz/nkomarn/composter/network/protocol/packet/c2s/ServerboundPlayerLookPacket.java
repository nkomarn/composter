package xyz.nkomarn.composter.network.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class ServerboundPlayerLookPacket extends Packet<ServerboundPlayerLookPacket> {

    private float yaw;
    private float pitch;
    private boolean grounded;

    public ServerboundPlayerLookPacket() {
    }

    public ServerboundPlayerLookPacket(float yaw, float pitch, boolean grounded) {
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
    public ServerboundPlayerLookPacket decode(@NotNull ByteBuf buffer) {
        return new ServerboundPlayerLookPacket(buffer.readFloat(), buffer.readFloat(), buffer.readBoolean());
    }
}
