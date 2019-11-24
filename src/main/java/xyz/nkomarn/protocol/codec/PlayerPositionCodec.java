package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketPlayerPosition;

import java.io.IOException;

public class PlayerPositionCodec extends Codec<PacketPlayerPosition> {
    public PlayerPositionCodec() {
        super(PacketPlayerPosition.class, 0x0B);
    }

    @Override
    public ByteBuf encode(PacketPlayerPosition packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeDouble(packet.getX());
        buffer.writeDouble(packet.getY());
        buffer.writeDouble(packet.getStance());
        buffer.writeDouble(packet.getZ());
        buffer.writeBoolean(packet.isOnGround());
        return buffer;
    }

    @Override
    public PacketPlayerPosition decode(ByteBuf buffer) throws IOException {
        final double x = buffer.readDouble();
        final double y = buffer.readDouble();
        final double stance = buffer.readDouble();
        final double z = buffer.readDouble();
        final boolean onGround = buffer.readBoolean();
        return new PacketPlayerPosition(x, y, stance, z, onGround);
    }
}
