package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketAnimation;

public class AnimationCodec extends Codec<PacketAnimation> {
    public AnimationCodec() {
        super(PacketAnimation.class, 0x12);
    }

    @Override
    public ByteBuf encode(PacketAnimation packet) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(packet.getEntityId());
        buffer.writeByte(packet.getAnimate());
        return buffer;
    }

    @Override
    public PacketAnimation decode(ByteBuf buffer) {
        int entityId = buffer.readInt();
        int animate = buffer.readByte();
        return new PacketAnimation(entityId, animate);
    }
}
