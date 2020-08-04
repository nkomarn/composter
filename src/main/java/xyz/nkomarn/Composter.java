package xyz.nkomarn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.command.CommandSource;
import xyz.nkomarn.server.CommandManager;
import xyz.nkomarn.server.NetworkManager;
import xyz.nkomarn.server.PlayerManager;
import xyz.nkomarn.server.WorldManager;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.util.configuration.Config;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter implements CommandSource {

    private final Config config = new Config();
    private final Logger logger;

    private final CommandManager commandManager;
    private final PlayerManager playerManager;
    private final NetworkManager networkManager;
    private final WorldManager worldManager;

    private long ticks = 0;
    private final ScheduledExecutorService tickLoop;

    public static Location SPAWN;

    public Composter(final int port) throws InterruptedException {
        this.logger = LogManager.getLogger("Server");
        this.logger.info("Starting Composter.");

        this.commandManager = new CommandManager(this);
        this.playerManager = new PlayerManager(this);
        this.networkManager = new NetworkManager(this);
        this.worldManager = new WorldManager(this, Paths.get("worlds"));
        this.worldManager.load();

        SPAWN = new Location(worldManager.getWorlds().iterator().next(), 0, 15, 0);

        this.tickLoop = Executors.newSingleThreadScheduledExecutor();
        this.tickLoop.scheduleAtFixedRate(() -> {
            playerManager.tick();
            worldManager.tick();
            ticks ++;
        }, 0, 50, TimeUnit.MILLISECONDS);

        networkManager.bind(port);
    }

    public @NotNull Config getConfig() {
        return config;
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    public @NotNull CommandManager getCommandManager() {
        return commandManager;
    }

    public @NotNull PlayerManager getPlayerManager() {
        return playerManager;
    }

    public @NotNull NetworkManager getNetworkManager() {
        return networkManager;
    }

    public @NotNull WorldManager getWorldManager() {
        return worldManager;
    }

    public long getTicks() {
        return ticks;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        getLogger().info(message);
    }

    public static void main(String[] args) throws InterruptedException {
        //int port = getConfig().getInteger("network.port");
        new Composter(25565);
    }
}
