package xyz.nkomarn.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.ConnectionState;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.protocol.packet.s2c.*;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.entity.Player;

import java.util.Arrays;
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

    public void broadcastMessage(@NotNull String message) {
        getPlayers().forEach(player -> player.sendMessage(message));
        server.getLogger().info(message);
    }

    public void onLogin(@NotNull Session session, @NotNull String username) {
        if (session.connectionState() != ConnectionState.LOGIN) {
            session.disconnect("Invalid connection state.");
            return;
        }

        if (players.containsKey(username.toLowerCase())) {
            String playerName = username.toLowerCase();
            players.get(playerName).getSession().disconnect("Logged in from another location.");
            players.remove(playerName);
        }

        Player player = new Player(session, username);
        session.sendPacket(new LoginS2CPacket(1298, 971768181197178410L, (byte) 0)); // TODO use actual coordinates
        players.put(username.toLowerCase(), player);
        session.setPlayer(player);
        session.setState(ConnectionState.PLAY);

        onJoin(player);
    }

    public void onJoin(@NotNull Player player) {
        player.setLocation(player.getWorld().getSpawn());
        player.syncChunks(true);

        Location worldSpawn = player.getWorld().getSpawn();
        player.getSession().sendPacket(new SpawnPositionS2CPacket(worldSpawn.getBlockX(), worldSpawn.getBlockY(), worldSpawn.getBlockZ()));
        player.getSession().sendPacket(new PlayerPosLookS2CPacket(0, 128, 0, 0, 0, 67.240000009536743D, false));
        broadcastMessage("§e" + player.getUsername() + " joined the game.");

        player.sendMessage("§6Welcome to Composter :)");
        player.sendMessage("§cComposter is still in early development.");
        player.sendMessage("§cMany features are incomplete!");

        player.getWorld().addEntity(player);
    }

    public void onDisconnect(@NotNull Player player) {
        String username = player.getUsername();
        players.remove(username.toLowerCase());
        broadcastMessage("§e" + username + " left the game.");

        player.markRemoved();
    }

    public void onChat(@NotNull Player player, @NotNull String message) {
        if (message.startsWith("/")) {
            server.getCommandManager().handle(player, message);
            return;
        }

        broadcastMessage(String.format("<%s> %s", player.getUsername(), message));
    }

    public void onMove(@NotNull Player player, @NotNull Location oldLocation, @NotNull Location newLocation) {
        if (!player.getWorld().isChunkLoaded(newLocation.getChunk())) {
            // player.updateLocation();
            return;
        }

        // TODO check for invalid move (so people can't teleport halfway across the world with no limitations)
        player.setLocation(newLocation);
    }

    public void tick() {
        players.values().forEach(Player::tick);

        if (server.currentTick() % 300 != 0) {
            return; // send out keep-alive every 10 seconds
        }

        for (var player : players.values()) {
            player.getSession().sendPacket(new KeepAliveBiPacket());
        }
    }
}
