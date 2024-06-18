package xyz.nkomarn.composter.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.command.CommandExecutor;
import xyz.nkomarn.composter.command.CommandSource;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.packet.s2c.WindowItemsS2CPacket;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {

    private final Logger LOGGER = LoggerFactory.getLogger("Command Manager");
    private final Composter server;
    private final HashMap<String, CommandExecutor> commands;

    public CommandManager(@NotNull Composter server) {
        this.server = server;
        this.commands = new HashMap<>();
        registerDefaults();
    }

    public void register(@NotNull String command, @NotNull CommandExecutor executor) {
        commands.put(command.toLowerCase(), executor);
    }

    public void handle(@NotNull CommandSource source, @NotNull String command) {
        LOGGER.info(source.getName() + " executed command \"" + command + "\"");

        command = command.substring(1);
        String[] arguments = command.split("\\s+");

        CommandExecutor executor = commands.get(arguments[0].toLowerCase());
        if (executor == null) {
            source.sendMessage(Component.text("Unrecognized command."));
            return;
        }

        executor.execute(source, Arrays.copyOfRange(arguments, 1, arguments.length));
    }

    public void registerDefaults() {
        register("about", (source, args) -> {
            source.sendMessage(Component.text("This server is running Composter Beta 1.7.3."));
            source.sendMessage(Component.text("This software is early alpha!", NamedTextColor.GRAY));
        });

        /* todo; cross-world tp
        register("tp", (source, args) -> {
            if (args.length < 3) {
                source.sendMessage(Component.text("Usage: /tp <x> <y> <z>", NamedTextColor.RED));
            } else {
                if (source instanceof Player) {
                    ((Player) source).teleport(new Location(
                            Composter.SPAWN.getWorld(),
                            Integer.parseInt(args[0]),
                            Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]),
                            0, 0
                    ));
                }
            }
        });
         */

        register("give", (source, arguments) -> {
            int[] items = new int[45];
            Arrays.fill(items, Integer.parseInt(arguments[0]));
            ((Player) source).connection().sendPacket(new WindowItemsS2CPacket(0, (short) 45, items));

            source.sendMessage(Component.text("GAVE YOU ITEMS LOL", NamedTextColor.LIGHT_PURPLE));
        });

        register("say", (source, arguments) -> {
            var message = String.join(" ", arguments);
            var component = Component.text(String.format("[%s] %s", source.getName(), message), NamedTextColor.LIGHT_PURPLE);
            server.getPlayerManager().broadcastMessage(component);
        });

        register("seed", (source, arguments) -> {
            var seed = server.getWorldManager().getWorlds().stream().findFirst().orElseThrow().getProperties().getSeed();
            var config = TextReplacementConfig.builder()
                    .match("<seed>")
                    .replacement(Component.text(seed, NamedTextColor.GREEN))
                    .build();
            var message = Component.text("The current world seed is <seed>")
                    .replaceText(config);

            source.sendMessage(message);
        });

        register("debug", (source, arguments) -> {
            int[] items = new int[45];
            for (var i = 0; i < items.length; i++) {
                items[i] = i + 1;
            }

            ((Player) source).connection().sendPacket(new WindowItemsS2CPacket(0, (short) 45, items));
        });
    }
}
