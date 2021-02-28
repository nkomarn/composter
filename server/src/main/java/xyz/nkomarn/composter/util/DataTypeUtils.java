package xyz.nkomarn.composter.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class DataTypeUtils {

    public static int readVarInt(ByteBuf buffer) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buffer.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static ByteBuf writeVarInt(ByteBuf buffer, int value) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            buffer.writeByte(temp);
        } while (value != 0);

        return buffer;
    }

    public static long readVarLong(ByteBuf buffer) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = buffer.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static ByteBuf writeVarLong(ByteBuf buffer, long value) {
        do {
            byte temp = (byte) (value & 0b01111111);
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            buffer.writeByte(temp);
        } while (value != 0);

        return buffer;
    }

    public static String readString(ByteBuf buffer) {
        int length = readVarInt(buffer);
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static ByteBuf writeString(ByteBuf buffer, String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        writeVarInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
        return buffer;
    }
}
