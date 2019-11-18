package xyz.nkomarn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.net.Bootstrap;
import xyz.nkomarn.world.World;
import xyz.nkomarn.world.generator.FlatGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter {

    private static final World world = new World(new FlatGenerator());

    private static final Logger logger =
        LoggerFactory.getLogger(Composter.class);

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

        ScheduledExecutorService tickLoop = Executors.newSingleThreadScheduledExecutor();
        tickLoop.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getWorld().getPlayers().forEach(p -> p.tick()); // tick each player
            }
        }, 0, 100, TimeUnit.MILLISECONDS); // 10 tps for testing

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start(port);
    }

    public static void main(String[] args) throws InterruptedException{
        new Composter(25565); // TODO server config file
    }

    public static Logger getLogger() {
        return logger;
    }

    public static World getWorld() {
        return world;
    }


}
