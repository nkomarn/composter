package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class PlayerPosLookS2CPacket extends Packet<PlayerPosLookS2CPacket> {

    private double x;
    private double y;
    private double z;
    private double stance;
    private float yaw;
    private float pitch;
    private boolean grounded;

    public PlayerPosLookS2CPacket() {
    }

    public PlayerPosLookS2CPacket(double x, double y, double z, float yaw, float pitch, double stance, boolean grounded) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.stance = stance;
        this.grounded = grounded;
    }

    @Override
    public int getId() {
        return 0x0D;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer()
                .writeDouble(x)
                .writeDouble(y)
                .writeDouble(stance)
                .writeDouble(z)
                .writeFloat(yaw)
                .writeFloat(pitch)
                .writeByte(grounded ? 1 : 0);
    }
}
