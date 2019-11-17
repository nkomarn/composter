package xyz.nkomarn.protocol;

import xyz.nkomarn.protocol.packet.PacketHandshake;
import xyz.nkomarn.protocol.packet.PacketLogin;
import xyz.nkomarn.protocol.packet.PacketPing;
import xyz.nkomarn.protocol.packet.PacketServerListPing;

public class PacketHandler {

    // Array of all opcodes and their corresponding class
    // The index represents the opcode
    private static Packet[] packets = new Packet[256];

    static {
        try {
            register(0x00, PacketPing.class);
            register(0x01, PacketLogin.class);
            register(0x02, PacketHandshake.class);
            register(0xFE, PacketServerListPing.class);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private static void register(final int opcode, final Class<? extends Packet> clazz) throws IllegalAccessException, InstantiationException {
        Packet packet = clazz.newInstance();
        packets[opcode] = packet;
    }

    public static Packet getPacket(final int opcode) {
        if (packets[opcode] == null) return null;
        return packets[opcode];
    }


}
