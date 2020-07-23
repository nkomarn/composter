package xyz.nkomarn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.server.NetworkManager;
import xyz.nkomarn.server.WorldManager;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.configuration.Config;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter {

    private static final Config config = new Config();
    private static final Logger logger = LoggerFactory.getLogger(Composter.class);
    private static ArrayList<Player> onlinePlayers = new ArrayList<>();

    private final NetworkManager networkManager;
    private final WorldManager worldManager;

    public static Location SPAWN;

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

        this.networkManager = new NetworkManager(this);
        this.worldManager = new WorldManager(this, Paths.get("worlds"));
        this.worldManager.load();

        SPAWN = new Location(worldManager.getWorlds().iterator().next(), 0, 15, 0);

        ScheduledExecutorService tickLoop = Executors.newSingleThreadScheduledExecutor();
        tickLoop.scheduleAtFixedRate(() -> onlinePlayers.forEach(Player::tick), 0, 50, TimeUnit.MILLISECONDS); // TODO change back to 50ms

        networkManager.bind(port);
    }

    public static void main(String[] args) throws InterruptedException {
        final int port = getConfig().getInteger("network.port");
        new Composter(port);
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    // TODO -------------------------------

    public static Logger getLogger() {
        return logger;
    }

    public static Config getConfig() {
        return config;
    }

    public static ArrayList<Player> getOnlinePlayers() {
        return onlinePlayers;
    }

    public static void addPlayer(final Player player) {
        onlinePlayers.add(player);
        Composter.broadcastMessage(String.format("§e%s joined the server.", player.getUsername()));
    }

    public static void brutallyMurderPlayer(final Player player) {
        onlinePlayers.remove(player);
        Composter.broadcastMessage(String.format("§e%s left the server.", player.getUsername()));
    }

    public static void broadcastMessage(final String message) {
        onlinePlayers.forEach(player -> player.sendMessage(message));
        logger.info(message);
    }
}
