package xyz.nkomarn;

import xyz.nkomarn.net.Bootstrap;
import java.util.logging.Logger;

public final class Composter {

    private static final Logger logger
        = Logger.getLogger("Composter");

    public static Logger getLogger() {
        return logger;
    }

    public Composter() throws InterruptedException {
        logger.info("Starting Composter.");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start(25565); // TODO server config file
        // TODO pipeline stuff & netty init
    }

    public static void main(String[] args) throws InterruptedException{
        Composter composter = new Composter();
    }
}
