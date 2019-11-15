package xyz.nkomarn.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class BufferUtil {

    public static String readString(ByteBuf buf) {
        int length = buf.readUnsignedShort();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes, CharsetUtil.UTF_8);
    }

}
