package xyz.nkomarn.composter.protocol;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.net.ConnectionState;
import xyz.nkomarn.composter.net.Session;
import xyz.nkomarn.composter.protocol.objects.EncryptionResponseObject;
import xyz.nkomarn.composter.protocol.packet.c2s.EncryptionResponseC2SPacket;
import xyz.nkomarn.composter.protocol.packet.c2s.HandshakeC2SPacket;
import xyz.nkomarn.composter.protocol.packet.s2c.*;
import xyz.nkomarn.composter.protocol.packet.c2s.LoginStartC2SPacket;
import xyz.nkomarn.composter.protocol.packet.c2s.StatusPingC2SPacket;
import xyz.nkomarn.composter.util.BrokenHash;
import xyz.nkomarn.composter.util.RSA;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;



public class PacketHandler {

    // TODO maybe storing classes instead of raw ids would make updates easier
    private static final EnumMap<ConnectionState, Int2ObjectMap<Handler>> HANDLERS = new EnumMap<>(ConnectionState.class);
    private final Composter server;
    private static String username;
    private static UUID id;
    private static RSA rsa;
    private static Auth auth;

    public PacketHandler(@NotNull Composter server) throws NoSuchAlgorithmException {
        this.server = server;
        rsa = new RSA();
        auth = new Auth();
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
            var loginPacket = (LoginStartC2SPacket) packet;
            var publicKey = rsa.getPublicKey();

            username = loginPacket.getUsername();
            System.out.println("Login started w/ username " + username);
            session.sendPacket(new EncryptionRequestS2CPacket("", publicKey.getEncoded().length, publicKey.getEncoded(), 4, rsa.getToken()));
        });

        register(0x01, ConnectionState.LOGIN, (session, packet) -> {
            var encryptionResponse = (EncryptionResponseC2SPacket) packet;
            var secret = rsa.decrypt(encryptionResponse.getSecret());
            var token = rsa.decrypt(encryptionResponse.getToken());

            String serverHash = BrokenHash.encryptServerHash(rsa.getPublicKey().getEncoded(), secret);
            EncryptionResponseObject response =  auth.clientAuth(serverHash, username);

            UUID uuid = java.util.UUID.fromString(
                    response.id
                            .replaceFirst(
                                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                            )
            );
            System.out.println(uuid.toString());
            if (Arrays.equals(token, rsa.getToken())) {
                session.sendPacket(new LoginSuccessS2CPacket(uuid, response.name));
                session.setState(ConnectionState.PLAY);
            }
            session.disconnect("Bad RSA token");
        });
    }

    @FunctionalInterface
    public interface Handler {

        void handle(@NotNull Session session, @NotNull Packet<?> packet) throws NoSuchAlgorithmException;
    }
}
