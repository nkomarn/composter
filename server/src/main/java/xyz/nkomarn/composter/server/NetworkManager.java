package xyz.nkomarn.composter.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.net.Bootstrap;
import xyz.nkomarn.composter.protocol.PacketHandler;
import xyz.nkomarn.composter.protocol.Protocol;

import java.security.NoSuchAlgorithmException;

public class NetworkManager {

    private final Composter server;
    private final Protocol protocol;
    private final PacketHandler handler;
    private final Bootstrap bootstrap;

    public NetworkManager(@NotNull Composter server) throws NoSuchAlgorithmException {
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
