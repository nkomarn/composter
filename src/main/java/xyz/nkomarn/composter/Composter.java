package xyz.nkomarn.composter;

import kyta.composter.protocol.Protocol;
import kyta.composter.server.MinecraftServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter {
    private final MinecraftServer server;
    private long currentTick = 0;

    public Composter() throws InterruptedException {
        this.server = new MinecraftServer(this);

        ScheduledExecutorService tickLoop = Executors.newSingleThreadScheduledExecutor();
        tickLoop.scheduleAtFixedRate(() -> {
            var tickStart = System.currentTimeMillis();
            currentTick++;

            try {
                server.tick(currentTick);
                // playerManager.tick(currentTick);
                // worldManager.tick(currentTick);
            } catch (Throwable x) {
                server.getLogger().error("an error occurred while ticking the server (tick #{})", currentTick, x);
            }

            server.getLogger().debug("tick #{} ended in {}ms.", currentTick, (System.currentTimeMillis() - tickStart));
        }, 0, 50, TimeUnit.MILLISECONDS);

        // start server
        Protocol.INSTANCE.bootstrap();
        server.startServer();
    }

    public static void main(String[] args) throws InterruptedException {
        //int port = getConfig().getInteger("network.port");
        new Composter();
    }
}
