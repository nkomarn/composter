package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class PlayerC2SPacket extends Packet<PlayerC2SPacket> {

    private boolean grounded;

    public PlayerC2SPacket() {
    }

    public PlayerC2SPacket(boolean grounded) {
        this.grounded = grounded;
    }

    @Override
    public int getId() {
        return 0x0A;
    }

    @Override
    public PlayerC2SPacket decode(@NotNull ByteBuf buffer) {
        return new PlayerC2SPacket(buffer.readBoolean());
    }
}
