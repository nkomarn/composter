package xyz.nkomarn.composter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.command.CommandSource;
import xyz.nkomarn.composter.server.CommandManager;
import xyz.nkomarn.composter.server.NetworkManager;
import xyz.nkomarn.composter.server.PlayerManager;
import xyz.nkomarn.composter.server.WorldManager;
import xyz.nkomarn.composter.type.Location;
import xyz.nkomarn.composter.util.configuration.Config;

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

    private long currentTick = 0;
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

        SPAWN = new Location(worldManager.getWorlds().iterator().next(), 0, 128, 0);

        this.tickLoop = Executors.newSingleThreadScheduledExecutor();
        this.tickLoop.scheduleAtFixedRate(() -> {
            currentTick++;
            playerManager.tick();
            worldManager.tick();
        }, 0, 50, TimeUnit.MILLISECONDS);

        networkManager.bind(port);
    }

    @Override
    public String getName() {
        return "Server";
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

    public long currentTick() {
        return currentTick;
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        /*
         * TODO: Console color support.
         */
        getLogger().info(PlainTextComponentSerializer.plainText().serialize(message));
    }

    public static void main(String[] args) throws InterruptedException {
        //int port = getConfig().getInteger("network.port");
        new Composter(25565);
    }
}
