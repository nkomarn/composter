package xyz.nkomarn.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.command.CommandExecutor;
import xyz.nkomarn.command.CommandSource;
import xyz.nkomarn.entity.Player;
import xyz.nkomarn.protocol.packet.s2c.WindowItemsS2CPacket;
import xyz.nkomarn.type.Location;

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

        register("give", (source, arguments) -> {
            int[] items = new int[45];
            Arrays.fill(items, Integer.parseInt(arguments[0]));
            ((Player) source).getSession().sendPacket(new WindowItemsS2CPacket(0, (short) 45, items));

            source.sendMessage("§dGAVE YOU ITEMS LOL");
        });

        register("say", (source, arguments) -> {
            var message = String.join(" ", arguments);
            server.getPlayerManager().broadcastMessage(String.format("§d[%s] %s", source.getName(), message));
        });

        register("seed", (source, arguments) -> {
            source.sendMessage("The current world seed is §a[" + server.getWorldManager().getWorlds().stream().findFirst().orElseThrow().getProperties().getSeed() + "]");
        });
    }
}
