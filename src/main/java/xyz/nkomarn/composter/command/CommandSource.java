package xyz.nkomarn.composter.command;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface CommandSource {

    String getName();

    void sendMessage(@NotNull Component message);
}
