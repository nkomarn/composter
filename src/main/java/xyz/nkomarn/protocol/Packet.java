package xyz.nkomarn.protocol;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.net.Session;

public abstract class Packet {
    public abstract void handle(Session session, ByteBuf data);
}
