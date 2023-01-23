package me.alientation.doomboheadplugin.customgui.listeners;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.alientation.doomboheadplugin.customgui.CustomGUIBlueprint;
import me.alientation.doomboheadplugin.customgui.annotations.EventHandler;
import me.alientation.doomboheadplugin.customgui.annotations.ParameterFlagAnnotation;
import me.alientation.doomboheadplugin.customgui.exceptions.InvalidMethodException;
import me.alientation.doomboheadplugin.customgui.exceptions.UnflaggedParameterException;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.event.inventory.TradeSelectEvent;

/**
 * Receives events from Inventory Listener and handles functionality connections to client annotated methods by reflection
 */
public class GUIListener {
	
	//details what methods correspond to what events
	private final Map<Class<?>,List<Method>> methodMap = new HashMap<>();

	//maps the flagged parameters of a methods
	private final Map<Method,List<ParameterFlagAnnotation>> methodParameters = new HashMap<>();

	/**
	 * Registers annotated methods in this class
	 */
	public GUIListener() {
		registerMethods();
	}

	/**
	 * Maps information about EventHandler methods
	 */
	public void registerMethods() {
		for (Method method : this.getClass().getMethods()) {
			if (!method.isAnnotationPresent(EventHandler.class)) continue;

			this.methodParameters.put(method,new ArrayList<>());

			boolean eventParamFound = false;
			for (Parameter parameter : method.getParameters()) {

				//flagged parameter
				if (parameter.isAnnotationPresent(ParameterFlagAnnotation.class)) {
					this.methodParameters.get(method).add(parameter.getAnnotation(ParameterFlagAnnotation.class));
					continue;
				}

				//If not a flagged parameter, it must be of type Event
				if (!(parameter.getParameterizedType() instanceof Event)) throw new UnflaggedParameterException();

				//Only one Event parameter is allowed
				if (eventParamFound) throw new InvalidMethodException();
				eventParamFound = true;

				List<Method> methods = this.methodMap.getOrDefault(parameter.getType(), new ArrayList<>());
				methods.add(method);
				this.methodMap.put(parameter.getType(), methods);

			}

		}
	}
	
	public void callMethod(Event e) {
		/*
		 * TODO: Add a parameters builder so that specific parameters can be passed along at the user's discretion
		 */
	}
	
	public Map<Class<?>,List<Method>> getMethodMap() {
		return this.methodMap;
	}
	
	public void onPlayerInventoryClick(CustomGUIBlueprint gui, InventoryClickEvent e) {

	}
	
	public void onGUIClick(CustomGUIBlueprint gui, InventoryClickEvent e) {
		
	}
	
	public void onOpen(CustomGUIBlueprint gui, InventoryOpenEvent e) {
		
	}
	
	public void onClose(CustomGUIBlueprint gui, InventoryCloseEvent e) {
		
	}
	
	public void onBrewComplete(CustomGUIBlueprint gui, BrewEvent e) {
		
	}
	
	public void onCraftItem(CustomGUIBlueprint gui, CraftItemEvent e) {
		
	}
	
	public void onFurnaceExtract(CustomGUIBlueprint gui, FurnaceExtractEvent e) {
		
	}
	
	public void onInventoryDrag(CustomGUIBlueprint gui, InventoryDragEvent e) {
		
	}
	
	public void onPlayerAction(CustomGUIBlueprint gui, InventoryEvent e) {
		
	}
	
	public void onInventoryInteract(CustomGUIBlueprint gui, InventoryInteractEvent e) {
		
	}
	
	public void onInventoryItemExit(CustomGUIBlueprint gui, InventoryMoveItemEvent e) {
		
	}
	
	public void onInventoryItemEnter(CustomGUIBlueprint gui, InventoryMoveItemEvent e) {
		
	}
	
	public void onInventoryItemMoveInitated(CustomGUIBlueprint gui, InventoryMoveItemEvent e) {
		
	}
	
	public void onInventoryPickupItem(CustomGUIBlueprint gui, InventoryPickupItemEvent e) {
		
	}
	
	public void onAnvilInsert(CustomGUIBlueprint gui, PrepareAnvilEvent e) {
		
	}
	
	public void onCraftInsert(CustomGUIBlueprint gui, PrepareItemCraftEvent e) {
		
	}
	
	public void onSmithingInsert(CustomGUIBlueprint gui, PrepareSmithingEvent e) {
		
	}
	
	public void onSmithItem(CustomGUIBlueprint gui, SmithItemEvent e) {
		
	}
	
	public void onTradeSelect(CustomGUIBlueprint gui, TradeSelectEvent e) {
		
	}
	
}
