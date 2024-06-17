package xyz.nkomarn.composter.network.protocol.packet.play;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class ClientboundChunkOperationPacket extends Packet<ClientboundChunkOperationPacket> {

    private int x;
    private int z;
    private boolean mode;

    public ClientboundChunkOperationPacket() {
    }

    public ClientboundChunkOperationPacket(int x, int z, boolean mode) {
        this.x = x;
        this.z = z;
        this.mode = mode;
    }

    @Override
    public int getId() {
        return 0x32;
    }

    @Override
    public @NotNull ByteBuf encode() {
        return Unpooled.buffer()
                .writeInt(x)
                .writeInt(z)
                .writeBoolean(mode);
    }
}
