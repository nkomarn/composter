package xyz.nkomarn.command;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CommandExecutor {

    void execute(@NotNull CommandSource source, @NotNull String[] arguments);
}
