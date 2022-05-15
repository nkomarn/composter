package xyz.nkomarn.composter;

import xyz.nkomarn.composter.network.protocol.Protocol;
import xyz.nkomarn.composter.server.MinecraftServer;

public class Composter {

    public static void main(String[] args) {
        Protocol.init();

        var server = new MinecraftServer();

        /*
         * TODO:
         * This will get moved to a separate tick thread.
         */
        server.startServer();
    }
}
