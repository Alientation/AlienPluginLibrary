package me.alientation.doomboheadplugin.customcommand.events;

import me.alientation.doomboheadplugin.customcommand.CustomCommand;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandCallAttemptEvent extends Event implements Cancellable{
	
	private static final HandlerList HANDLER = new HandlerList();
	private boolean isCancelled;
	
	private CustomCommand commandCalled;
	
	public CommandCallAttemptEvent(CustomCommand command) {
		this.commandCalled = command;
	}
	
	public CustomCommand getCommand() {
		return this.commandCalled;
	}
	
	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLER;
	}

}
