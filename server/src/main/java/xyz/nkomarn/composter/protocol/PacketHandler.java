package xyz.nkomarn.composter.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.net.ConnectionState;
import xyz.nkomarn.composter.net.Session;
import xyz.nkomarn.composter.protocol.packet.c2s.EncryptionResponseC2SPacket;
import xyz.nkomarn.composter.protocol.packet.c2s.HandshakeC2SPacket;
import xyz.nkomarn.composter.protocol.packet.s2c.*;
import xyz.nkomarn.composter.protocol.packet.c2s.LoginStartC2SPacket;
import xyz.nkomarn.composter.protocol.packet.c2s.StatusPingC2SPacket;
import xyz.nkomarn.composter.util.RSAKeyGen;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.EnumMap;
import java.util.UUID;

public class PacketHandler {

    // TODO maybe storing classes instead of raw ids would make updates easier
    private static final EnumMap<ConnectionState, Int2ObjectMap<Handler>> HANDLERS = new EnumMap<>(ConnectionState.class);
    private final Composter server;
    private static RSAKeyGen keyGen;

    public PacketHandler(@NotNull Composter server) throws NoSuchAlgorithmException {
        this.server = server;
        keyGen = new RSAKeyGen();
    }

    public static void register(int id, ConnectionState state, Handler handler) {
        HANDLERS.computeIfAbsent(state, state1 -> new Int2ObjectOpenHashMap<>()).put(id, handler);
    }

    public void handle(Session session, Packet<?> packet) throws NoSuchAlgorithmException {
        var handler = HANDLERS.get(session.getState()).get(packet.getId());
        if (handler == null) {
            // TODO something idk maybe asdhjkhjsadkfhasdjfhdhjbdasfhjkasdhkfafasjfadskhfjhk :)))) (i must) ~ sebastian
            return;
        }

        handler.handle(session, packet);
    }

    static {
        register(0x00, ConnectionState.HANDSHAKING, (session, packet) -> {
            var handshakePacket = (HandshakeC2SPacket) packet; // TODO eliminate this with the classes suggestion above
            session.setState(handshakePacket.getNextState());
        });

        register(0x01, ConnectionState.STATUS, (session, packet) -> {
            var pingPacket = (StatusPingC2SPacket) packet;
            session.sendPacket(new StatusPongS2CPacket(pingPacket.getTimestamp()));
        });

        register(0x00, ConnectionState.STATUS, (session, packet) -> {
            session.sendPacket(new StatusResponseS2CPacket()); // TODO edit MOTD field later
        });

        register(0x00, ConnectionState.LOGIN, (session, packet) -> {
            var pKey = keyGen.getPublicKey();
            var loginPacket = (LoginStartC2SPacket) packet;
            System.out.println("Login started w/ username " + loginPacket.getUsername() + ", Public Key Length: " + pKey.getEncoded().length);
            session.sendPacket(new EncryptionRequestS2CPacket("", pKey.getEncoded().length, pKey.getEncoded(), 4, keyGen.generateBytes()));
        });

        register(0x01, ConnectionState.LOGIN, (session, packet) -> {
            var encryptionResponse = (EncryptionResponseC2SPacket) packet;
            System.out.println("yessir");
        });
    }

    @FunctionalInterface
    public interface Handler {

        void handle(@NotNull Session session, @NotNull Packet<?> packet) throws NoSuchAlgorithmException;
    }
}
