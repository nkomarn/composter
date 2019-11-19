package xyz.nkomarn.protocol;

import xyz.nkomarn.protocol.packet.*;

public class PacketHandler {

    // Array of all opcodes and their corresponding class
    // The index represents the opcode
    private static Packet[] packets = new Packet[256];

    static {
        try {
            register(0x01, PacketLogin.class);
            register(0x02, PacketHandshake.class);
            register(0x03, PacketChat.class);
            register(0x0D, PacketPlayerPosition.class);
            register(0xFE, PacketServerListPing.class);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static void register(final int opcode, final Class<? extends Packet> clazz)
        throws IllegalAccessException, InstantiationException {
        Packet packet = clazz.newInstance();
        packets[opcode] = packet;
    }

    public static Packet getPacket(final int opcode) {
        return packets[opcode];
    }


}
