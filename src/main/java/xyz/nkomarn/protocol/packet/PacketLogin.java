package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketLogin extends Packet {
    @Override
    public void handle(Session session, ByteBuf data) {
        final State state = session.getState();

        if (state == State.LOGIN) {
            final int protocol = data.readInt();
            final String username = ByteBufUtil.readString(data);

            if (protocol != 14) {
                session.disconnect(String.format("Unsupported protocol version '%s'.",
                    protocol));
                return;
            }

            // Finish login stage
            ByteBuf login = Unpooled.buffer();
            login.writeByte(0x01);
            login.writeInt(1298); // TODO return actual entity id
            ByteBufUtil.writeString(login, "");
            login.writeLong(971768181197178410L); // TODO return actual seed
            login.writeByte(0);
            session.write(login);
            session.setState(State.PLAY);

            // Set player object for the session
            final Player player = new Player(session, username);
            session.setPlayer(player);
            Composter.getWorld().addPlayer(player);
        } /*else {
            session.disconnect("Already logged in.");
        }*/
    }
}
