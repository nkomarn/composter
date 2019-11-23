package xyz.nkomarn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.net.Bootstrap;
import xyz.nkomarn.net.SessionManager;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.configuration.Config;
import xyz.nkomarn.world.World;
import xyz.nkomarn.world.generator.FlatGenerator;
import xyz.nkomarn.world.generator.NoiseGenerator;;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter {

    // TODO configurable chunk threading in config
    public static ExecutorService chunkThread = Executors.newFixedThreadPool(3);

    private static Config config = new Config();
    private static final World world = new World(new NoiseGenerator());
    private static final Logger logger = LoggerFactory.getLogger(Composter.class);

    private static ArrayList<Player> onlinePlayers = new ArrayList<>();

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

        ScheduledExecutorService tickLoop = Executors.newSingleThreadScheduledExecutor();
        tickLoop.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SessionManager.tick();

                onlinePlayers.forEach(player -> {
                    if (!player.getSession().getChannel().isActive()) {
                        System.out.println("Removing old player object.");
                        onlinePlayers.remove(player);
                        broadcastMessage("uh some player left the game");
                        return;
                    }
                    player.tick();
                }); // tick each player

                /*int currentTick = 0;
                double[] recentTps = new double[3];

                if ( ++currentTick % 20 == 0 ) {
                   // TODO TPS Calculation (not that it matters)
                }*/
            }
        }, 0, 20, TimeUnit.MILLISECONDS); // 20 tps

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
    }

    public static void broadcastMessage(final String message) {
        onlinePlayers.forEach(player -> player.sendMessage(message));
    }
}
