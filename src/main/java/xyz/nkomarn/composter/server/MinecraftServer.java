package xyz.nkomarn.composter.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.command.CommandSource;

public class MinecraftServer implements Tickable, CommandSource {

    private static final Logger LOGGER = LoggerFactory.getLogger("Server");
    private static final PlainTextComponentSerializer SERIALIZER = PlainTextComponentSerializer.builder()
            .flattener(ComponentFlattener.basic())
            .build();
    private final PlayerList playerList;

    public MinecraftServer() {
        this.playerList = new PlayerList(this);
    }

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        /*
         * TODO: Console color support.
         */
        LOGGER.info(SERIALIZER.serialize(message));
    }

    public PlayerList playerList() {
        return playerList;
    }

    @Override
    public void tick() {

    }
}
