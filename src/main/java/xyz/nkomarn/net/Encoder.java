package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.CodecHandler;
import xyz.nkomarn.protocol.Packet;

import java.util.List;

public class Encoder extends MessageToMessageEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, List out) throws Exception {
        if (message instanceof Packet) {
            Packet packet = (Packet) message;
            Class<? extends Packet> clazz = packet.getClass();
            Codec<Packet> codec = (Codec<Packet>) CodecHandler.getCodec(clazz); // TODO check cast

            if (codec == null) {
                // TODO
                return;
            }

            //Composter.getLogger().info(String.format("Sending %s.", clazz.getName()));

            ByteBuf opcode = Unpooled.buffer(1);
            opcode.writeByte(codec.getId());
            out.add(Unpooled.wrappedBuffer(opcode, codec.encode(packet)));
        }
        out.add(message);
    }
}
