package xyz.nkomarn.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.Packet;

public class WindowItemsS2CPacket extends Packet<WindowItemsS2CPacket> {

    private int id;
    private short slots;
    private int[] items;

    public WindowItemsS2CPacket() {
    }

    public WindowItemsS2CPacket(int id, short slots, int[] items) {
        this.id = id;
        this.slots = slots;
        this.items = items;
    }

    @Override
    public int getId() {
        return 0x68;
    }

    @Override
    public @NotNull ByteBuf encode() {
        ByteBuf buffer = Unpooled.buffer()
                .writeByte(id)
                .writeShort(slots);

        for (int item : items) {
            if (item == 0) {
                buffer.writeShort(-1);
            } else {
                buffer.writeShort(item)
                        .writeByte((byte) 127) // TODO
                        .writeShort((short) 7);
            }
        }

        return buffer;
    }
}
