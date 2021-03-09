package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

public class StringTag extends Tag {

    private String data;

    public StringTag(@NotNull String name) {
        super(name);
    }

    public StringTag(@NotNull String name, @NotNull String data) {
        super(name);
        this.data = data;
    }

    StringTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = new String(data.readNBytes(data.readShort()));
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.STRING;
    }

    // TODO temporary
    @Override
    public String toString() {
        return "StringTag{" +
                "data='" + data + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
