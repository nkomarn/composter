package xyz.nkomarn.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.command.CommandExecutor;
import xyz.nkomarn.command.CommandSource;

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
        register("about", (source, arguments) -> {
            source.sendMessage("This server is running Composter Beta 1.7.3.");
            source.sendMessage("§7This software is early alpha!");
        });
    }
}
