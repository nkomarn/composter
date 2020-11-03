package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class StatusPingC2SPacket extends Packet<StatusPingC2SPacket> {

    private long timestamp;

    public StatusPingC2SPacket() {
    }

    public StatusPingC2SPacket(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int getId() {
        return 0x01;
    }

    @Override
    public StatusPingC2SPacket decode(@NotNull ByteBuf buffer) {
        System.out.println("reading timestamp");
        var timestamp = buffer.readLong();
        System.out.println("finished reading timestamp");
        return new StatusPingC2SPacket(timestamp);
    }
}
