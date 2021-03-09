package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * A representation of a NBT tag, the base for the
 * NBT data format used in Minecraft.
 */
public abstract class Tag {

    protected String name;

    Tag(@Nullable String name) {
        this.name = name;
    }

    @NotNull
    public abstract Type getType();

    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Represents all possible NBT tag types.
     */
    public enum Type {

        END(data -> new EndTag()),
        BYTE(ByteTag::new),
        SHORT(ShortTag::new),
        INT(IntTag::new),
        LONG(LongTag::new),
        FLOAT(FloatTag::new),
        DOUBLE(DoubleTag::new),
        BYTE_ARRAY(ByteArrayTag::new),
        STRING(StringTag::new),
        LIST(ListTag::new),
        COMPOUND(CompoundTag::new),
        INT_ARRAY(IntArrayTag::new),
        LONG_ARRAY(LongArrayTag::new);

        private static final Type[] TYPES = Type.values();
        private final ReadFunction<DataInputStream, ? extends Tag> function;

        Type(@NotNull ReadFunction<DataInputStream, ? extends Tag> function) {
            this.function = function;
        }

        public byte getId() {
            return (byte) this.ordinal();
        }

        public <T extends Tag> T read(@NotNull DataInputStream data) throws IOException {
            return (T) function.apply(data);
        }

        @NotNull
        public static Type fromId(byte id) {
            return TYPES[id];
        }

        @FunctionalInterface
        public interface ReadFunction<T, R> {
            R apply(T t) throws IOException;
        }
    }
}
