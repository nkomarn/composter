package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

public class InboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context, Object message) throws InterruptedException {

        // Print packet ID
        ByteBuf buffer = (ByteBuf) message;
        System.out.println();

        byte packet = buffer.readByte();
        System.out.println("Recieved packet " + packet);

        System.out.println(((ByteBuf) message).toString(CharsetUtil.UTF_8));

        // Handshake
        if (packet == 0x02) {
            System.out.println("Handshaking");
            ByteBuf data = Unpooled.buffer();
            data.writeByte(0x02);
            data.writeByte(0);
            data.writeByte(1);
            data.writeByte(0);
            data.writeByte(0x2d);
            context.write(data);
            context.flush();
        } else if (packet == 0x01) {
            System.out.println("Logging in");
        }

        ByteBuf d = Unpooled.buffer();
        d.writeByte(0xFF);
        d.writeByte(0);
        d.writeByte(26);
        d.writeByte(0);
        d.writeBytes("§6Connected to Composter!".getBytes(CharsetUtil.UTF_16LE));
        context.write(d);
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        context.close();
    }

}
