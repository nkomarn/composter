package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketLogin extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        State state = session.getState();

        if (state.equals(State.LOGIN)) {
            int protocol = buffer.readInt();
            final String username = ByteBufUtil.readString(buffer);

            /*if (protocol != 14) {
                session.disconnect("Unsupported protocol version '" + protocol + "'.");
                return;
            }*/

            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(0x01);
            buf.writeInt(1298); // bullshit entity id
            ByteBufUtil.writeString(buf, "");
            buf.writeLong(971768181197178410L); // bullshit seed
            buf.writeByte(0);
            session.write(buf);

            Player player = new Player(session, username);
            session.setPlayer(player);
            session.setState(State.PLAY);
            Composter.getWorld().addPlayer(player);

            /*final Location location = player.getLocation();
            ByteBuf playerPosition = Unpooled.buffer();
            playerPosition.writeInt(0x0D);
            playerPosition.writeDouble(location.getX());
            playerPosition.writeDouble(67.24D); // TODO move this var somewhere else
            playerPosition.writeDouble(location.getY());
            playerPosition.writeDouble(location.getZ());
            playerPosition.writeFloat(location.getYaw());
            playerPosition.writeFloat(location.getPitch());
            playerPosition.writeBoolean(true);
            session.write(playerPosition);*/
        }
    }
}

