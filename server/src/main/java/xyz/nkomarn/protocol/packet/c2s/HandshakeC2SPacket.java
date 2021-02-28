package xyz.nkomarn.protocol.packet.c2s;

import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.net.ConnectionState;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.DataTypeUtils;

public class HandshakeC2SPacket extends Packet<HandshakeC2SPacket> {

    private int protocol;
    private String address;
    private short port;
    private ConnectionState nextState;

    public HandshakeC2SPacket() {
    }

    public HandshakeC2SPacket(int protocol, String address, short port, ConnectionState nextState) {
        this.protocol = protocol;
        this.address = address;
        this.port = port;
        this.nextState = nextState;
    }

    public int getProtocol() {
        return protocol;
    }

    public String getAddress() {
        return address;
    }

    public short getPort() {
        return port;
    }

    public ConnectionState getNextState() {
        return nextState;
    }

    @Override
    public int getId() {
        return 0x00;
    }

    @Override
    public HandshakeC2SPacket decode(@NotNull ByteBuf buffer) {
        var protocol = DataTypeUtils.readVarInt(buffer);
        var address = DataTypeUtils.readString(buffer);
        var port = buffer.readShort();
        var nextState = ConnectionState.getById(DataTypeUtils.readVarInt(buffer));
        return new HandshakeC2SPacket(protocol, address, port, nextState);
    }
}
