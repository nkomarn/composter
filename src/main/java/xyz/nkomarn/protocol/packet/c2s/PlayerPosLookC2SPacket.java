package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class PlayerPosLookC2SPacket extends Packet<PlayerPosLookC2SPacket> {

    private double x;
    private double y;
    private double z;
    private double stance;
    private float yaw;
    private float pitch;
    private boolean grounded;

    public PlayerPosLookC2SPacket() {
    }

    public PlayerPosLookC2SPacket(double x, double y, double z, float yaw, float pitch, double stance, boolean grounded) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.stance = stance;
        this.grounded = grounded;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public double getStance() {
        return stance;
    }

    public boolean isGrounded() {
        return grounded;
    }

    @Override
    public int getId() {
        return 0x0D;
    }

    @Override
    public PlayerPosLookC2SPacket decode(@NotNull ByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double stance = buffer.readDouble();
        double z = buffer.readDouble();
        float yaw = buffer.readFloat();
        float pitch = buffer.readFloat();
        boolean grounded = buffer.readBoolean();

        return new PlayerPosLookC2SPacket(x, y, z, yaw, pitch, stance, grounded);
    }
}
