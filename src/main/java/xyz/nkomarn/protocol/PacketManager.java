package xyz.nkomarn.protocol;

import xyz.nkomarn.protocol.packets.PacketLoginRequest;

import java.util.HashMap;

public class PacketManager {

    private final static HashMap<Integer, Class<? extends Packet>> packets =
        new HashMap<Integer, Class<? extends Packet>>() {{
            put(0x00, PacketKeepAlive.class);
            put(0x01, PacketLoginRequest.class);
            //put(0x02, PacketHandshake.class);
        }};

    public static Class<? extends Packet> lookup(final int opCode) {
        return packets.get(opCode);
    }

}
