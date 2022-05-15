package xyz.nkomarn.composter.entity;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.Connection;

public class Player extends Entity {

    private final Connection connection;
    private final String username;

    public Player(Connection connection, String username) {
        this.connection = connection;
        this.username = username;
    }

    @NotNull
    public Connection connection() {
        return connection;
    }

    @NotNull
    public String username() {
        return username;
    }
}
