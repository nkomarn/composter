package xyz.nkomarn.composter.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.network.protocol.ConnectionState;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.*;
import xyz.nkomarn.composter.type.Location;
import xyz.nkomarn.composter.entity.Player;

import java.util.Collection;
import java.util.HashMap;

public class PlayerManager {

    private final Composter server;
    private final HashMap<String, Player> players;

    public PlayerManager(@NotNull Composter server) {
        this.server = server;
        this.players = new HashMap<>();
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public void broadcastMessage(@NotNull Component message) {
        getPlayers().forEach(player -> player.sendMessage(message));
        server.sendMessage(message);
    }

    public void onLogin(@NotNull Connection connection, @NotNull String username) {
        if (connection.state() != ConnectionState.LOGIN) {
            connection.disconnect("Invalid connection state.");
            return;
        }

        if (players.containsKey(username.toLowerCase())) {
            String playerName = username.toLowerCase();
            players.get(playerName).connection().disconnect("Logged in from another location.");
            players.remove(playerName);
        }

        Player player = new Player(connection, username);
        connection.sendPacket(new LoginS2CPacket(1298, 971768181197178410L, (byte) 0)); // TODO use actual coordinates
        players.put(username.toLowerCase(), player);
        connection.setPlayer(player);
        connection.setState(ConnectionState.PLAY);

        onJoin(player);
    }

    public void onJoin(@NotNull Player player) {
        player.setLocation(player.getWorld().getSpawn());
        player.syncChunks(true);

        Location worldSpawn = player.getWorld().getSpawn();
        player.connection().sendPacket(new SpawnPositionS2CPacket(worldSpawn.getBlockX(), worldSpawn.getBlockY(), worldSpawn.getBlockZ()));
        player.connection().sendPacket(new PlayerPosLookS2CPacket(0, 128, 0, 0, 0, 67.240000009536743D, false));
        broadcastMessage(Component.text(player.getUsername() + " joined the game.", NamedTextColor.YELLOW));

        player.sendMessage(Component.text("Welcome to Composter :)", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Composter is still in early development.", NamedTextColor.RED));
        player.sendMessage(Component.text("Many features are incomplete!", NamedTextColor.RED));

        player.getWorld().addEntity(player);
    }

    public void onDisconnect(@NotNull Player player) {
        String username = player.getUsername();
        players.remove(username.toLowerCase());
        broadcastMessage(Component.text(username + " left the game.", NamedTextColor.YELLOW));

        player.markRemoved();
    }

    public void onChat(@NotNull Player player, @NotNull String message) {
        if (message.startsWith("/")) {
            server.getCommandManager().handle(player, message);
            return;
        }

        broadcastMessage(Component.text(String.format("<%s> %s", player.getUsername(), message)));
    }

    public void onMove(@NotNull Player player, @NotNull Location oldLocation, @NotNull Location newLocation) {
        /*
        if (!player.getWorld().isChunkLoaded(newLocation.getChunk())) {
            // player.updateLocation();
            return;
        }
         */

        // TODO check for invalid move (so people can't teleport halfway across the world with no limitations)
        player.setLocation(newLocation);
    }

    public void tick() {
        players.values().forEach(Player::tick);

        if (server.currentTick() % 300 != 0) {
            return; // send out keep-alive every 10 seconds
        }

        for (var player : players.values()) {
            player.connection().sendPacket(new KeepAliveBiPacket());
        }
    }
}
