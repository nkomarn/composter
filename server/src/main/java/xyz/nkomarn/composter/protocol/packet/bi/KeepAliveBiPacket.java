package xyz.nkomarn.composter.protocol.packet.bi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;
import xyz.nkomarn.composter.util.DataTypeUtils;

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
