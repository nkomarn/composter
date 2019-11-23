package xyz.nkomarn.protocol;

import xyz.nkomarn.protocol.handler.ChatHandler;
import xyz.nkomarn.protocol.handler.HandshakeHandler;
import xyz.nkomarn.protocol.handler.LoginHandler;
import xyz.nkomarn.protocol.handler.PlayerPositionAndLookHandler;
import xyz.nkomarn.protocol.packets.PacketChat;
import xyz.nkomarn.protocol.packets.PacketHandshake;
import xyz.nkomarn.protocol.packets.PacketLogin;
import xyz.nkomarn.protocol.packets.PacketPlayerPositionAndLook;

import java.util.HashMap;
import java.util.Map;

public class HandlerHandler {
    private static final Map<Class<? extends Packet>, PacketHandler<?>> handlers =
        new HashMap<>();

    static {
        try {
            register(PacketLogin.class, LoginHandler.class);
            register(PacketHandshake.class, HandshakeHandler.class);
            register(PacketChat.class, ChatHandler.class);
            register(PacketPlayerPositionAndLook.class, PlayerPositionAndLookHandler.class);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); // TODO handle error somehow
        }
    }

    private static <T extends Packet> void register(Class<T> clazz, Class<? extends PacketHandler<T>> handlerClass)
        throws InstantiationException, IllegalAccessException {
        PacketHandler<T> handler = handlerClass.newInstance();
        handlers.put(clazz, handler);
    }

    public static <T extends Packet> PacketHandler<T> getHandler(Class<T> clazz) {
        return (PacketHandler<T>) handlers.get(clazz);
    }
}
