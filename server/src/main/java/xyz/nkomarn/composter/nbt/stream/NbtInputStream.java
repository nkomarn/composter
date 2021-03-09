package xyz.nkomarn.composter.nbt.stream;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.nbt.StringTag;
import xyz.nkomarn.composter.nbt.Tag;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

public class NbtInputStream {

    public static void main(String[] args) throws Exception {
        var file = new File("test.nbt");
        var stream = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));

        stream.readByte();
        System.out.println(Tag.Type.COMPOUND.read(stream).toString());

        /*
        do {
            var type = Tag.Type.fromId(stream.readByte());
            var nameLength = stream.readShort();
            var name = new String(stream.readNBytes(nameLength));

            System.out.println(type);
            System.out.println(nameLength);
            System.out.println(name);

            var tag = Tag.Type.fromId(stream.readByte()).read(stream);
            System.out.println(tag);
            System.exit(0);
        } while (true);
         */
    }
}
