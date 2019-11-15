package xyz.nkomarn.protocol.scaffold;

import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.PacketHandshake;
import xyz.nkomarn.protocol.PacketKeepAlive;
import xyz.nkomarn.protocol.PacketLogin;

import java.util.HashMap;

public class PacketLookup {

    private final static HashMap<Integer, PacketScaffold<?>> packets =
        new HashMap<Integer, PacketScaffold<?>>() {{
            put(0x00, new PacketKeepAliveScaffold());
            put(0x01, new PacketLoginScaffold());
            //put(0x02, PacketHandshake.class);
    }};

    public static PacketScaffold<?> find(final int id) {
        return packets.get(id);
    }

}
