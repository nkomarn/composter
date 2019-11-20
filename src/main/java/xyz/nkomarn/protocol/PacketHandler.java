package xyz.nkomarn.protocol;

import xyz.nkomarn.protocol.packet.*;

public class PacketHandler {

    // Array of all opcodes and their corresponding class
    // The index represents the opcode
    private static Packet[] packets = new Packet[256];

    static {
        try {
            register(0x01, new PacketLogin());
            register(0x02, new PacketOutHandshake());
        } finally {

        }
    }

    private static void register(final int opcode, final Packet packet) {
        packets[opcode] = packet;
    }

    public static Packet getPacket(final int opcode) {
        return packets[opcode];
    }


}
