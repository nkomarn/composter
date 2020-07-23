package xyz.nkomarn.util;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ByteBufUtil {

    // TODO refactor
    public static String readString(ByteBuf buf) {
        int len = buf.readUnsignedShort();

        char[] characters = new char[len];
        for (int i = 0; i < len; i++) {
            characters[i] = buf.readChar();
        }
        return new String(characters);
    }

    /**
     * Writes a string into a buffer as chars
     * @param buffer
     * @param string
     */
    public static ByteBuf writeString(ByteBuf buffer, String string) {
        //buffer.writeCharSequence(CharBuffer.wrap("-".toCharArray()), Charset.defaultCharset());
        final char[] chars = string.toCharArray();
        buffer.writeShort(chars.length);
        for (char c : chars) buffer.writeChar(c);
        return buffer;
    }
}
