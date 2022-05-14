package xyz.nkomarn.composter.network.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class PlayerPosLookC2SPacket extends ServerboundOnGroundPacket {

    private double x;
    private double y;
    private double z;
    private double stance;
    private float yaw;
    private float pitch;

    public PlayerPosLookC2SPacket() {
    }

    public PlayerPosLookC2SPacket(double x, double y, double z, float yaw, float pitch, double stance) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.stance = stance;
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
        super.decode(buffer);

        return new PlayerPosLookC2SPacket(x, y, z, yaw, pitch, stance);
    }
}
