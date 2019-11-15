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

    public static void writeString(ByteBuf buf, String str) {
        int len = str.length();
        if (len >= 65536) {
            throw new IllegalArgumentException("String too long.");
        }

        buf.writeShort(len);
        for (int i = 0; i < len; i++) {
            buf.writeChar(str.charAt(i));
        }
    }

}
