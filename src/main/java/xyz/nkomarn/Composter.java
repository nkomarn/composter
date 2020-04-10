package xyz.nkomarn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.net.Bootstrap;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.configuration.Config;
import xyz.nkomarn.world.World;
import xyz.nkomarn.world.generator.FlatGenerator;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter {

    // TODO configurable chunk threading in config
    //public static ExecutorService chunkThread = Executors.newFixedThreadPool(3);

    private static Config config = new Config();
    private static final World world = new World(new FlatGenerator());
    private static final Logger logger = LoggerFactory.getLogger(Composter.class);

    private static ArrayList<Player> onlinePlayers = new ArrayList<>();

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

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

    public static World getWorld() {
        return world;
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
