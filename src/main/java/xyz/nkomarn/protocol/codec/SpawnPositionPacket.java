package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketSpawnPosition;

import java.io.IOException;

public class SpawnPositionPacket extends Codec<PacketSpawnPosition> {
    public SpawnPositionPacket() {
        super(PacketSpawnPosition.class, 0x06);
    }

    @Override
    public ByteBuf encode(PacketSpawnPosition packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(packet.getX());
        buffer.writeInt(packet.getY());
        buffer.writeInt(packet.getZ());
        return buffer;
    }

    @Override
    public PacketSpawnPosition decode(ByteBuf buffer) throws IOException {
        return null; // Server to client only
    }
}
