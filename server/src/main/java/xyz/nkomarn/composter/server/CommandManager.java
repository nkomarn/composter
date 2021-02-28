package xyz.nkomarn.composter.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.command.CommandExecutor;
import xyz.nkomarn.composter.command.CommandSource;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.type.Location;

import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {

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
        command = command.substring(1);
        String[] arguments = command.split("\\s+");

        CommandExecutor executor = commands.get(arguments[0].toLowerCase());
        if (executor == null) {
            source.sendMessage("Unrecognized command.");
            return;
        }

        executor.execute(source, Arrays.copyOfRange(arguments, 1, arguments.length));
    }

    public void registerDefaults() {
        register("about", (source, args) -> {
            source.sendMessage("This server is running Composter Beta 1.7.3.");
            source.sendMessage("§7This software is early alpha!");
        });

        register("tp", (source, args) -> {
            if (args.length < 3) {
                source.sendMessage("§cUsage: /tp <x> <y> <z>");
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
    }
}
