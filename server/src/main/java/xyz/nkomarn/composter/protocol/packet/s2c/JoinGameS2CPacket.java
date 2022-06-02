package xyz.nkomarn.composter.protocol.packet.s2c;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.protocol.Packet;

public class JoinGameS2CPacket extends Packet<JoinGameS2CPacket> {

    public JoinGameS2CPacket() {}

    //public JoinGameS2CPacket(@NotNull )

    @Override
    public int getId() {
        return 0x26;
    }
}
