package xyz.nkomarn.composter.network;

import io.netty.buffer.ByteBuf;

public class WrappedBuff {

    private final ByteBuf buffer;

    private WrappedBuff(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public static WrappedBuff wrap(ByteBuf buffer) {
        return new WrappedBuff(buffer);
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public short readShort() {
        return buffer.readShort();
    }

    public int readInt() {
        return buffer.readInt();
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public long readLong() {
        return buffer.readLong();
    }

    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    public String readString() {
        var length = buffer.readShort();
        var chars = new char[length];

        for (var i = 0; i < length; i++) {
            chars[i] = buffer.readChar();
        }

        return new String(chars);
    }

    public WrappedBuff writeByte(byte value) {
        buffer.writeByte(value);
        return this;
    }

    public WrappedBuff writeShort(short value) {
        buffer.writeShort(value);
        return this;
    }

    public WrappedBuff writeInt(int value) {
        buffer.writeInt(value);
        return this;
    }

    public WrappedBuff writeFloat(float value) {
        buffer.writeFloat(value);
        return this;
    }

    public WrappedBuff writeDouble(double value) {
        buffer.writeDouble(value);
        return this;
    }

    public WrappedBuff writeLong(long value) {
        buffer.writeLong(value);
        return this;
    }

    public WrappedBuff writeString(String value) {
        var chars = value.toCharArray();
        buffer.writeShort(chars.length);

        for (var c : chars) {
            buffer.writeChar(c);
        }

        return this;
    }
}
