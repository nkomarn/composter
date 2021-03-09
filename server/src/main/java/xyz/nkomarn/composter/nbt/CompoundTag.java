package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompoundTag extends Tag {

    private Map<String, Tag> data;

    public CompoundTag(@NotNull String name) {
        super(name);
        this.data = new HashMap<>();
    }

    CompoundTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = new HashMap<>();

        do {
            var tag = Tag.Type.fromId(data.readByte()).read(data);

            if (tag.getType() == Type.END) {
                System.out.println("Finished parsing.");
                break;
            }

            this.data.put(tag.getName(), tag);
        } while (true);
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.COMPOUND;
    }

    @Override
    public String toString() {
        return "CompoundTag{" +
                "data=" + data +
                ", name='" + name + '\'' +
                '}';
    }
}
