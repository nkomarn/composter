package xyz.nkomarn.composter.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.NetworkController;
import xyz.nkomarn.composter.tick.Tickable;

public class MinecraftServer implements Tickable {

    private final NetworkController networkController;
    private final PlayerList playerList;

    public MinecraftServer() {
        this.networkController = new NetworkController(this);
        this.playerList = new PlayerList(this);
    }

    @NotNull
    public PlayerList playerList() {
        return playerList;
    }

    public void startServer() {
        try {
            networkController.bind(25565);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {

    }
}
