package xyz.nkomarn.protocol.packet;

import com.sun.media.jfxmedia.logging.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.IOUtils;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.type.Block;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;
import xyz.nkomarn.world.generator.FlatGenerator;
import xyz.nkomarn.world.generator.WorldGenerator;
import xyz.nkomarn.world.region.RegionFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.Deflater;

public class PacketLogin extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        State state = session.getState();

        if (state.equals(State.LOGIN)) {
            int protocol = buffer.readInt();

            // Protocol check
            if (protocol != 14) {
                session.disconnect("Unsupported protocol version '" + protocol + "'.");
                return;
            }

            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(0x01);
            buf.writeInt(1298); // bullshit entity id
            ByteBufUtil.writeString(buf, "");
            buf.writeLong(971768181197178410L); // bullshit seed
            buf.writeByte(0);
            session.send(buf);

            session.setState(State.PLAY);

            // pre chunk shit
            ByteBuf prechunk = Unpooled.buffer();
            prechunk.writeInt(0x32);
            prechunk.writeInt(0);
            prechunk.writeInt(0);
            prechunk.writeInt(1);
            //session.send(prechunk);



            // Send some dumb hardcoded data
            ByteBuf chunkData = Unpooled.buffer();
            chunkData.writeInt(0x33);
            chunkData.writeInt(0);
            chunkData.writeShort(0);
            chunkData.writeInt(0);
            chunkData.writeByte(15);
            chunkData.writeByte(127);
            chunkData.writeByte(15);

            Chunk chunk = Composter.getWorld().getChunkAt(0, 0);
            System.out.println("Block at 0, 10, 2 is " + chunk.getType(0, 10, 2));
            byte[] data = chunk.serializeTileData();
            byte[] compressedData = new byte[(16 * 16 * 128 * 5) / 2];

            Deflater deflater = new Deflater(Deflater.BEST_SPEED);
            deflater.setInput(data);
            deflater.finish();

            int compressed = deflater.deflate(compressedData);
            try {
                if (compressed == 0) {
                    Composter.getLogger().error("Not all bytes compressed.");
                }
            } finally {
                deflater.end();
            }

            System.out.println(compressed);
            chunkData.writeInt(compressed);
            chunkData.writeBytes(compressedData, 0, compressed);
            session.send(chunkData);


            // Send spawn position!
            ByteBuf spawnPosition = Unpooled.buffer();
            spawnPosition.writeInt(0x06);
            spawnPosition.writeInt(5);
            spawnPosition.writeInt(10);
            spawnPosition.writeInt(5);
            session.send(spawnPosition);

            // Player look + pos packet
            ByteBuf look = Unpooled.buffer();
            look.writeDouble(0.5D);
            look.writeDouble(67.240000009536743D);
            look.writeDouble(10.5D);
            look.writeDouble(0.5D);
            look.writeDouble(0.0D);
            look.writeDouble(0.0D);
            look.writeBoolean(false);
            session.send(look);

        } else {
            session.disconnect("Already logged in.");
        }
    }
}

