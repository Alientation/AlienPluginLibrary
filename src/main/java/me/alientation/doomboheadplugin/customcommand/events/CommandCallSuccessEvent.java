package me.alientation.doomboheadplugin.customcommand.events;

import me.alientation.doomboheadplugin.customcommand.CustomCommand;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandCallSuccessEvent extends Event{
	
	private static final HandlerList HANDLER = new HandlerList();
	
	private final CustomCommand commandCalled;
	
	public CommandCallSuccessEvent(CustomCommand command) {
		this.commandCalled = command;
	}
	
	public CustomCommand getCommand() {
		return this.commandCalled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLER;
	}

}
