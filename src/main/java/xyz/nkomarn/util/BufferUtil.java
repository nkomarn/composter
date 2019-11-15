package xyz.nkomarn.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class BufferUtil {

    public static String readString(ByteBuf buf) {
        int len = buf.readUnsignedShort();

        char[] characters = new char[len];
        for (int i = 0; i < len; i++) {
            characters[i] = buf.readChar();
        }
        return new String(characters);
    }

    /**
     * Reads a UTF-8 encoded string from the buffer.
     * @param buf The buffer.
     * @return The string.
     */
    public static String readUtf8String(ByteBuf buf) {
        int len = buf.readUnsignedShort();

        byte[] bytes = new byte[len];
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
