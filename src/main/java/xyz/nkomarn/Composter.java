package xyz.nkomarn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.net.Bootstrap;
import xyz.nkomarn.world.World;
import xyz.nkomarn.world.generator.FlatGenerator;

public final class Composter {

    private static final World world = new World(new FlatGenerator());

    private static final Logger logger =
        LoggerFactory.getLogger(Composter.class);

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

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
