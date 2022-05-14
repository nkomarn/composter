package xyz.nkomarn.composter.network.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

public class ServerboundPlayerActionPacket extends Packet<ServerboundPlayerActionPacket> {

    private int entityId;
    private Action action;

    public ServerboundPlayerActionPacket() {
    }

    public ServerboundPlayerActionPacket(int entityId, Action action) {
        this.entityId = entityId;
        this.action = action;
    }

    public int getEntityId() {
        return entityId;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public int getId() {
        return 0x13;
    }

    @Override
    public ServerboundPlayerActionPacket decode(@NotNull ByteBuf buffer) {
        return new ServerboundPlayerActionPacket(buffer.readInt(), Action.LEAVE_BED /* Action.values()[buffer.readByte() - 1] */);
    }

    public enum Action {

        START_CROUCHING,
        STOP_CROUCHING,
        LEAVE_BED
    }
}
