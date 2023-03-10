package me.alientation.doomboheadplugin.customcommand.events;

import me.alientation.doomboheadplugin.customcommand.CustomCommand;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when a command call was successful
 */
public class CommandCallSuccessEvent extends Event {
	
	private static final HandlerList HANDLER = new HandlerList();
	
	private final CustomCommand commandCalled;
	
	public CommandCallSuccessEvent(CustomCommand command) {
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
