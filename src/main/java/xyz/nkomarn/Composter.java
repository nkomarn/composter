package xyz.nkomarn;

import xyz.nkomarn.net.Bootstrap;
import java.util.logging.Logger;

public final class Composter {

    public Composter(final int port) throws InterruptedException {
        logger.info("Starting Composter.");

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.start(port);
    }

    public static void main(String[] args) throws InterruptedException{
        new Composter(25565); // TODO server config file
    }

    private static final Logger logger
        = Logger.getLogger(Composter.class.getName());

    public static Logger getLogger() {
        return logger;
    }
}
