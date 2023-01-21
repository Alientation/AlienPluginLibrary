package me.alientation.doomboheadplugin.customcommand.events;

import me.alientation.doomboheadplugin.customcommand.CustomCommand;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CommandModifySuccessEvent extends Event {

    private static final HandlerList HANDLER = new HandlerList();

    private final CustomCommand commandCalled;

    public CommandModifySuccessEvent(CustomCommand command) {
        this.commandCalled = command;
    }

    public CustomCommand getCommand() {
        return this.commandCalled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER;
    }
}
