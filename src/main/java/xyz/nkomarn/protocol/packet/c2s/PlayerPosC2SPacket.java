package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class PlayerPosC2SPacket extends Packet<PlayerPosC2SPacket> {

    private double x;
    private double y;
    private double z;
    private double stance;
    private boolean grounded;


    public PlayerPosC2SPacket() {
    }

    public PlayerPosC2SPacket(double x, double y, double z, double stance, boolean grounded) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public double getStance() {
        return stance;
    }

    public boolean isGrounded() {
        return grounded;
    }

    @Override
    public int getId() {
        return 0x0B;
    }

    @Override
    public PlayerPosC2SPacket decode(@NotNull ByteBuf buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double stance = buffer.readDouble();
        double z = buffer.readDouble();
        boolean grounded = buffer.readBoolean();

        return new PlayerPosC2SPacket(x, y, z, stance, grounded);
    }
}
