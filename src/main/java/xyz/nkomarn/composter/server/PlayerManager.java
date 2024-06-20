package xyz.nkomarn.composter.server;

import kyta.composter.Tickable;
import kyta.composter.math.Vec3d;
import kyta.composter.protocol.ConnectionState;
import kyta.composter.world.BlockPos;
import kyta.composter.world.ChunkPos;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.entity.Player;
import kyta.composter.network.Connection;

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
//         server.sendMessage(message); // todo - net refactor
    }

    public void onLogin(@NotNull Connection connection, @NotNull String username) {
        if (connection.getState() != ConnectionState.LOGIN) {
            connection.disconnect("Invalid connection state."); // todo - net refactor
            return;
        }

        if (players.containsKey(username.toLowerCase())) {
            String playerName = username.toLowerCase();
            players.get(playerName).connection.disconnect("Logged in from another location."); // todo - net refactor
            players.remove(playerName);
        }

        /*
        Player player = new Player(server.getWorldManager().getWorlds().stream().findFirst().get(), connection, username);
        // connection.sendPacket(new LoginS2CPacket(player.getId(), 971768181197178410L, (byte) 0)); // TODO use actual coordinates // todo - net refactor
        players.put(username.toLowerCase(), player);
        connection.setPlayer(player);
        connection.setState(ConnectionState.PLAY);

        onJoin(player);
         */
    }

    public void onJoin(@NotNull Player player) {
        var worldSpawn = player.getWorld().getSpawn().up(3);

        player.setPos(new Vec3d(worldSpawn));
//         player.updateVisibleChunks();

        // todo - net refactor
        // player.connection().sendPacket(new SpawnPositionS2CPacket(worldSpawn.getX(), worldSpawn.getY(), worldSpawn.getZ()));
        // player.connection().sendPacket(new PlayerPosLookS2CPacket(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch(), player.getStance(), player.isOnGround()));
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
       /*
        if (message.startsWith("/")) {
            server.getCommandManager().handle(player, message);
            return;
        }
        */

        broadcastMessage(Component.text(String.format("<%s> %s", player.getUsername(), message)));
    }

    public void onLook(@NotNull Player player, float yaw, float pitch) {
        player.setYaw(yaw);
        player.setPitch(pitch);
    }

    public void onMove(@NotNull Player player, @NotNull Vec3d oldPos, @NotNull Vec3d newPos) {
        if (oldPos.distanceSqrt(newPos) >= 100) {
            player.connection.disconnect(String.format(
                    "Invalid movement: (%s, %s, %s) -> (%s, %s, %s)",
                    oldPos.getX(), oldPos.getY(), oldPos.getZ(),
                    newPos.getX(), newPos.getY(), newPos.getZ()
            ));
            return;
        }

        /*
         * don't let players move into unloaded areas.
         */
        var chunkPos = new ChunkPos(new BlockPos(newPos));
        if (!player.getWorld().isChunkLoaded(chunkPos)) {
            /*
            player.connection().sendPacket(
                    new PlayerPosLookS2CPacket(
                            oldPos.getX(),
                            oldPos.getY(),
                            oldPos.getZ(),
                            player.getYaw(),
                            player.getPitch(),
                            player.getStance(),
                            player.isOnGround()
                    ));


             */ // todo - net refactor
            return;
        }


        /*
        if (!player.getWorld().isChunkLoaded(newLocation.getChunk())) {
            // player.updateLocation();
            return;
        }
         */

        player.setPos(newPos);
    }

    @Override
    public void tick(long currentTick) {
        if (currentTick % 300 != 0) {
            return; // send out keep-alive every 10 seconds
        }

        for (var player : players.values()) {
            // player.connection().sendPacket(new KeepAliveBiPacket()); // todo - net refactor
        }
    }
}
