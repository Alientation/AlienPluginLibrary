package me.alientation.doomboheadplugin.customgui.events;

import me.alientation.doomboheadplugin.customgui.CustomGUIBlueprint;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUITradeSelectEvent extends Event implements Cancellable {
	
	//create a way to make events using annotations
	
	private static final HandlerList HANDLERS = new HandlerList();
	private boolean isCancelled;
	
	private CustomGUIBlueprint customGUIBlueprint;
	private InventoryClickEvent event;
	
	
	public GUITradeSelectEvent(CustomGUIBlueprint customGUIBlueprint, InventoryClickEvent e) {
		this.customGUIBlueprint = customGUIBlueprint;
		this.event = e;
	}
	
	public CustomGUIBlueprint getCustomGUI() {
		return this.customGUIBlueprint;
	}
	
	public InventoryClickEvent getEvent() {
		return this.event;
	}
	
	
	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
		this.event.setCancelled(isCancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
}