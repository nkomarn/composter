package xyz.nkomarn;

import xyz.nkomarn.net.ServerBootstrap;

public final class Composter {

    public static void main(String[] args) {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.start(25565);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
