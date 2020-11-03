package xyz.nkomarn.protocol.packet.bi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.DataTypeUtils;

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
        return DataTypeUtils.writeVarLong(Unpooled.buffer(), id);
    }

    @Override
    public KeepAliveBiPacket decode(@NotNull ByteBuf buffer) {
        return new KeepAliveBiPacket(DataTypeUtils.readVarLong(buffer));
    }
}
