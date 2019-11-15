package xyz.nkomarn;

import xyz.nkomarn.net.Bootstrap;
import java.util.logging.Logger;

public final class Composter {

    private static final Logger logger
        = Logger.getLogger("Composter");

    public Composter() {
        // TODO server config file

        logger.info("Starting Composter.");
        // TODO pipeline stuff & netty init
    }

    public void bind() {
        // TODO bind netty
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        Composter composter = new Composter();

        // TODO move this into main composter class
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.start(25565);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
