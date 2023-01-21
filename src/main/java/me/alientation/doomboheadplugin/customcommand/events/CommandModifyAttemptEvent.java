package me.alientation.doomboheadplugin.customcommand.events;

import me.alientation.doomboheadplugin.customcommand.CustomCommand;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when an attempt to modify a command on runtime was initiated
 */
public class CommandModifyAttemptEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER = new HandlerList();

    private final CustomCommand commandCalled;

    public CommandModifyAttemptEvent(CustomCommand command) {
        this.commandCalled = command;
    }

    public CustomCommand getCommand() {
        return this.commandCalled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {

    }
}
