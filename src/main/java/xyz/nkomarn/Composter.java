package xyz.nkomarn;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.net.Bootstrap;
import xyz.nkomarn.server.WorldManager;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.configuration.Config;
import xyz.nkomarn.world.World;
import xyz.nkomarn.world.generator.FlatGenerator;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter {

    // TODO configurable chunk threading in config
    //public static ExecutorService chunkThread = Executors.newFixedThreadPool(3);

    private static final Config config = new Config();
    private static final Logger logger = LoggerFactory.getLogger(Composter.class);
    private static ArrayList<Player> onlinePlayers = new ArrayList<>();

    private final WorldManager worldManager;

    public static Location SPAWN;

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

        this.worldManager = new WorldManager(this, Paths.get("worlds"));
        this.worldManager.load();

        SPAWN = new Location(worldManager.getWorlds().iterator().next(), 0, 15, 0);

        ScheduledExecutorService tickLoop = Executors.newSingleThreadScheduledExecutor();
        tickLoop.scheduleAtFixedRate(() -> onlinePlayers.forEach(Player::tick), 0, 1000, TimeUnit.MILLISECONDS);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start(port); // start the netty socket server
    }

    public static void main(String[] args) throws InterruptedException {
        final int port = getConfig().getInteger("network.port");
        new Composter(port);
    }

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
