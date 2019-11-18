package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;
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
            session.attachPlayer("TechToolbox");

            // Send spawn position! TODO send world spawn pos
            ByteBuf spawnPosition = Unpooled.buffer();
            spawnPosition.writeInt(0x06);
            spawnPosition.writeInt(5);
            spawnPosition.writeInt(10);
            spawnPosition.writeInt(5);
            session.send(spawnPosition);

            // Player look + pos packet
            ByteBuf look = Unpooled.buffer();
            look.writeByte(0x0D);
            look.writeDouble(5D);
            look.writeDouble(67.240000009536743D);
            look.writeDouble(10D);
            look.writeDouble(0.5D);
            look.writeFloat(0.0F);
            look.writeFloat(0.0F);
            look.writeBoolean(false);
            session.send(look);

            // Chat message :D
            session.sendMessage("§6Hey, welcome to Composter :)");
            session.sendMessage("§cComposter is still early in development.");

        } else {
            //session.disconnect("Already logged in.");
        }
    }
}

