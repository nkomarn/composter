package xyz.nkomarn.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Bootstrap;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.Protocol;

public class NetworkManager {

    private final Composter server;
    private final Protocol protocol;
    private final PacketHandler handler;
    private final Bootstrap bootstrap;

    public NetworkManager(@NotNull Composter server) {
        this.server = server;
        this.protocol = new Protocol();
        this.handler = new PacketHandler(server);
        this.bootstrap = new Bootstrap(this);
    }

    public Composter getServer() {
        return server;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public PacketHandler getHandler() {
        return handler;
    }

    public void bind(int port) throws InterruptedException {
        bootstrap.start(port);
    }
}
