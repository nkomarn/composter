package xyz.nkomarn;

import io.netty.channel.ChannelFactory;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import xyz.nkomarn.net.ServerBootstrap;

import java.util.logging.Logger;

public final class Composter {

    private static final Logger logger
        = Logger.getLogger(Composter.class.getName());

    public Composter() {
        // TODO server config file

        logger.info("Starting Composter.");
        // TODO pipeline stuff & netty init
    }

    public void bind() {
        // TODO bind netty
    }

    public Logger getLogger() {
        return logger;
    }

    public static void main(String[] args) {
        Composter composter = new Composter();
        
        /*try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.start(25565);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
