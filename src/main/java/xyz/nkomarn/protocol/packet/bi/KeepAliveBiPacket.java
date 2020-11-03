package xyz.nkomarn.protocol.packet.bi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class KeepAliveBiPacket extends Packet<KeepAliveBiPacket> {

    public long id;

    public KeepAliveBiPacket() {
    }

    public KeepAliveBiPacket(long id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return 0x10;
    }

    @Override
    @NotNull
    public ByteBuf encode() {
        var buffer = Unpooled.buffer();
        ByteBufUtil.writeVarLong(id, buffer);
        return buffer;
    }

    @Override
    public KeepAliveBiPacket decode(@NotNull ByteBuf buffer) {
        return new KeepAliveBiPacket(ByteBufUtil.readVarLong(buffer));
    }
}
