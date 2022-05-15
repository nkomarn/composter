package xyz.nkomarn.composter.network.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import xyz.nkomarn.composter.network.FlowDirection;
import xyz.nkomarn.composter.network.protocol.packet.Packet;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundDisconnectPacket;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundHandshakePacket;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundKeepAlivePacket;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundLoginPacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundDisconnectPacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundHandshakePacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundLoginPacket;

public class Protocol {

    private static final Object2IntMap<Class<? extends Packet>> PACKET_IDS = new Object2IntOpenHashMap<>();

    static {
        register(0x00, ClientboundKeepAlivePacket.class, FlowDirection.CLIENTBOUND, ConnectionState.PLAY);
        register(0x01, ClientboundLoginPacket.class, FlowDirection.CLIENTBOUND, ConnectionState.LOGIN);
        register(0x01, ServerboundLoginPacket.class, FlowDirection.SERVERBOUND, ConnectionState.LOGIN);
        register(0x02, ClientboundHandshakePacket.class, FlowDirection.CLIENTBOUND, ConnectionState.HANDSHAKING);
        register(0x02, ServerboundHandshakePacket.class, FlowDirection.SERVERBOUND, ConnectionState.HANDSHAKING);
        register(0xFF, ClientboundDisconnectPacket.class, FlowDirection.CLIENTBOUND, ConnectionState.values());
        register(0xFF, ServerboundDisconnectPacket.class, FlowDirection.SERVERBOUND, ConnectionState.values());
    }

    public static void init() {
    }

    public static int packetId(Packet packet) {
        return PACKET_IDS.getInt(packet.getClass());
    }

    private static void register(int id, Class<? extends Packet> clazz, FlowDirection direction, ConnectionState... validStates) {
        for (var state : validStates) {
            state.packets().computeIfAbsent(direction, a -> new Int2ObjectOpenHashMap<>()).put(id, clazz);
        }

        PACKET_IDS.put(clazz, id);
    }
}
