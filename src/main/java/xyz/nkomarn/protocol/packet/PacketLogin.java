package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.io.IOUtils;
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
            buf.writeInt(0);
            buf.writeByte(0);
            buf.writeByte(1);
            buf.writeByte(128);
            buf.writeByte(8);
            session.send(buf);

            session.setState(State.PLAY);

            /*WorldGenerator generator = new FlatGenerator();
            RegionFile regionFile = null;
            try {
                regionFile = new RegionFile(new File("region.mca"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataOutputStream outputStream = regionFile.getChunkDataOutputStream(0, 0);
            Chunk newChunk = generator.generate(0, 0);
            for (Block block : newChunk.getBlocks()) {
                try {
                    outputStream.write(block.getType().getId());
                    outputStream.write(block.getType().getMetadata());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                outputStream.close();
                regionFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Try to send the chunk?

            ByteBuf mapChunk = Unpooled.buffer();
            mapChunk.writeByte(0x33);
            mapChunk.writeInt(0);
            mapChunk.writeByte(0);
            mapChunk.writeInt(0);
            mapChunk.writeByte(15);
            mapChunk.writeByte(127);
            mapChunk.writeByte(15);

            // sending the data.. oof
            DataInputStream is = null;
            try {
                is = regionFile.getChunkDataInputStream(0, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = null;
            try {
                data = IOUtils.toByteArray(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mapChunk.writeInt(data.length);
            mapChunk.writeBytes(data);
            session.send(mapChunk);*/

        } else {
            session.disconnect("Already logged in.");
        }
    }
}

