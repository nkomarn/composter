package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class ByteTag extends Tag {

    private byte data;

    public ByteTag(@NotNull String name) {
        super(name);
    }

    public ByteTag(@NotNull String name, byte data) {
        super(name);
        this.data = data;
    }

    ByteTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = data.readByte();
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.BYTE;
    }
}
