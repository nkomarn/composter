package xyz.nkomarn.composter.command;

import org.jetbrains.annotations.NotNull;

public interface CommandSource {

    String getName();

    void sendMessage(@NotNull String message);
}
