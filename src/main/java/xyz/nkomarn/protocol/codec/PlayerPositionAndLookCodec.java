package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketPlayerPositionAndLook;

import java.io.IOException;

public class PlayerPositionAndLookCodec extends Codec<PacketPlayerPositionAndLook> {
    public PlayerPositionAndLookCodec() {
        super(PacketPlayerPositionAndLook.class, 0x0D);
    }

    @Override
    public ByteBuf encode(PacketPlayerPositionAndLook packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeDouble(packet.getX());
        buffer.writeDouble(packet.getStance());
        buffer.writeDouble(packet.getY());
        buffer.writeDouble(packet.getZ());
        buffer.writeFloat(packet.getYaw());
        buffer.writeFloat(packet.getPitch());
        buffer.writeBoolean(packet.isOnGround());
        return buffer;
    }

    @Override
    public PacketPlayerPositionAndLook decode(ByteBuf buffer) throws IOException {
        final double x = buffer.readDouble();
        final double y = buffer.readDouble();
        final double stance = buffer.readDouble();
        final double z = buffer.readDouble();
        final float yaw = buffer.readFloat();
        final float pitch = buffer.readFloat();
        final boolean onGround = buffer.readBoolean();
        return new PacketPlayerPositionAndLook(x, y, stance, z, yaw, pitch, onGround);
    }
}
