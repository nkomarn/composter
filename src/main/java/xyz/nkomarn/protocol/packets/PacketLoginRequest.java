package xyz.nkomarn.protocol.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.BufferUtil;

public class PacketLoginRequest extends Packet<PacketLoginRequest> {

    private final int protocol;
    private final String username;
    private final int id;
    private final long seed;
    private final int mode;
    private final byte dimension;
    private final byte difficulty;
    private final byte height;
    private final byte maxPlayers;

    private PacketLoginRequest(final int id, final long seed, final int mode,
                               final byte dimension, final byte difficulty, final byte height, final byte maxPlayers) {
        this.id = id;
        this.seed = seed;
        this.mode = mode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.height = height;
        this.maxPlayers = maxPlayers;
    }

    private PacketLoginRequest(final int protocol, final String username) {
        this.protocol = protocol;
        this.username = username;
    }

    @Override
    public ByteBuf encode(PacketLoginRequest message) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(this.id);
        buffer.writeBytes("".getBytes(CharsetUtil.UTF_8));
        buffer.writeLong(this.seed);
        buffer.writeInt(this.mode);
        buffer.writeByte(this.dimension);
        buffer.writeByte(this.difficulty);
        buffer.writeByte(this.height);
        buffer.writeByte(this.maxPlayers);
        return buffer;
    }

    @Override
    public PacketLoginRequest decode(ByteBuf buffer) {
        int protocol = buffer.readInt();
        String username = BufferUtil.readString(buffer);
        return new PacketLoginRequest(protocol, username);
    }
}
