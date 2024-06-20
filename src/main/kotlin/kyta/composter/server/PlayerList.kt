package xyz.nkomarn.composter.server;

import org.jetbrains.annotations.Unmodifiable;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.Packet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerList {

    private final Map<String, Player> onlinePlayers;

    public PlayerList(MinecraftServer server) {
        this.onlinePlayers = new HashMap<>();
    }

    @Unmodifiable
    public Collection<Player> onlinePlayers() {
        return Collections.unmodifiableCollection(onlinePlayers.values());
    }

    public void broadcastPacket(Packet<?> packet) {
        onlinePlayers.values().forEach(player -> player.connection().sendPacket(packet));
    }

    public void playerJoined(Player player) {
        var username = player.getUsername();

        if (onlinePlayers.containsKey(username)) {
            throw new IllegalStateException("Player \"" + username + "\" is already registered.");
        }

        onlinePlayers.put(username, player);
    }

    public void playerDisconnected(Player player) {
        var username = player.getUsername();

        if (player.isRemoved()) {
            throw new IllegalStateException("Player \"" + username + "\" was unregistered before being marked as removed.");
        }

        onlinePlayers.remove(username);
    }
}
