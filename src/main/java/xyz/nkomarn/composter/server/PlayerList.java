package xyz.nkomarn.composter.server;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundPacket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayerList {

    private final MinecraftServer server;
    private final Map<String, Player> onlinePlayers;

    public PlayerList(MinecraftServer server) {
        this.server = server;
        this.onlinePlayers = new HashMap<>();
    }

    @NotNull
    public Collection<Player> onlinePlayers() {
        return onlinePlayers.values();
    }

    @Nullable
    public Player player(String username) {
        return onlinePlayers.get(username);
    }

    public void broadcastPacket(ClientboundPacket packet) {
        onlinePlayers().forEach(player -> player.connection().sendPacket(packet));
    }

    public void playerJoined(Player player) {
        var username = player.username();

        if (onlinePlayers.containsKey(username.toLowerCase())) {
            throw new IllegalStateException("Player \"" + username + "\" is already registered.");
        }

        onlinePlayers.put(username, player);
    }

    public void playerDisconnected(Player player) {
        var username = player.username();

        if (player.isRemoved()) {
            throw new IllegalStateException("Player \"" + username + "\" was unregistered before being marked as removed.");
        }

        onlinePlayers.remove(username.toLowerCase());
    }
}
