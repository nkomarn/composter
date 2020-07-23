package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class HandshakeS2CPacket extends Packet<HandshakeS2CPacket> {

    private String hash;

    public HandshakeS2CPacket() {
    }

    public HandshakeS2CPacket(@NotNull String hash) {
        this.hash = hash;
    }

    @Override
    public int getId() {
        return 0x02;
    }

    @Override
    public @NotNull ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer();
        ByteBufUtil.writeString(buffer, "-");
        return buffer;
    }
}
