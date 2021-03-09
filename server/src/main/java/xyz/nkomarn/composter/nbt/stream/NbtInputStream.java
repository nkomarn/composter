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

        do {
            var type = Tag.Type.fromId(stream.readByte());
            var nameLength = stream.readShort();
            var name = new String(stream.readNBytes(nameLength));

            System.out.println(type);
            System.out.println(nameLength);
            System.out.println(name);

            System.out.println("Type of first item in tag: " + Tag.Type.fromId(stream.readByte()));
            new StringTag(stream);
            System.exit(0);
        } while (true);
    }
}
