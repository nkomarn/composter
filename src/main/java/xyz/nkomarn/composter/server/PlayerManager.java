package xyz.nkomarn.composter.server;

import kyta.composter.Tickable;
import kyta.composter.math.Vec3d;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.ConnectionState;
import xyz.nkomarn.composter.network.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.LoginS2CPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.PlayerPosLookS2CPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.SpawnPositionS2CPacket;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager implements Tickable {
    private final Composter server;
    private final Map<String, Player> players;

    public PlayerManager(@NotNull Composter server) {
        this.server = server;
        this.players = new ConcurrentHashMap<>();
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

        Player player = new Player(server.getWorldManager().getWorlds().stream().findFirst().get(), connection, username);
        connection.sendPacket(new LoginS2CPacket(player.getId(), 971768181197178410L, (byte) 0)); // TODO use actual coordinates
        players.put(username.toLowerCase(), player);
        connection.setPlayer(player);
        connection.setState(ConnectionState.PLAY);

        onJoin(player);
    }

    public void onJoin(@NotNull Player player) {
        player.setPos(new Vec3d(player.getWorld().getSpawn()));
        player.updateVisibleChunks();

        var worldSpawn = player.getWorld().getSpawn();
        player.connection().sendPacket(new SpawnPositionS2CPacket(worldSpawn.getX(), worldSpawn.getY(), worldSpawn.getZ()));
        player.connection().sendPacket(new PlayerPosLookS2CPacket(worldSpawn.getX(), worldSpawn.getY(), worldSpawn.getZ(), 0, 0, 67.240000009536743D, false));
        broadcastMessage(Component.text(player.getUsername() + " joined the game.", NamedTextColor.YELLOW));

        player.sendMessage(Component.text("Welcome to Composter :)", NamedTextColor.GOLD));
        player.sendMessage(Component.text("Composter is still in early development.", NamedTextColor.RED));
        player.sendMessage(Component.text("Many features are incomplete!", NamedTextColor.RED));
        player.sendMessage(Component.empty());

        var pos = player.getBlockPos();
        player.sendMessage(Component.text(String.format("You are currently at (%s, %s, %s)", pos.getX(), pos.getY(), pos.getZ())));

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

    public void onMove(@NotNull Player player, @NotNull Vec3d oldPos, @NotNull Vec3d newPos, float pitch, float yaw) {
        /*
        if (!player.getWorld().isChunkLoaded(newLocation.getChunk())) {
            // player.updateLocation();
            return;
        }
         */

        // TODO check for invalid move (so people can't teleport halfway across the world with no limitations)
        player.setPos(newPos);
        player.setPitch(pitch);
        player.setYaw(yaw);
    }

    @Override
    public void tick(long currentTick) {
        if (currentTick % 300 != 0) {
            return; // send out keep-alive every 10 seconds
        }

        for (var player : players.values()) {
            player.connection().sendPacket(new KeepAliveBiPacket());
        }
    }
}
