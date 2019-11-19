package xyz.nkomarn;

import com.flowpowered.noise.Noise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.net.Bootstrap;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.world.World;
import xyz.nkomarn.world.generator.FlatGenerator;
import xyz.nkomarn.world.generator.NoiseGenerator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class Composter {

    // TODO configurable chunk threading in config
    public static ExecutorService chunkPool = Executors.newFixedThreadPool(3);

    private static final World world = new World(new FlatGenerator());

    private static final Logger logger =
        LoggerFactory.getLogger(Composter.class);

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

        ScheduledExecutorService tickLoop = Executors.newSingleThreadScheduledExecutor();
        tickLoop.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getWorld().getPlayers().forEach(Player::tick); // tick each player

                /*int currentTick = 0;
                double[] recentTps = new double[3];

                if ( ++currentTick % 20 == 0 ) {
                   // TODO TPS Calculation (not that it matters)
                }*/
            }
        }, 0, 20, TimeUnit.MILLISECONDS); // 20 tps

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
