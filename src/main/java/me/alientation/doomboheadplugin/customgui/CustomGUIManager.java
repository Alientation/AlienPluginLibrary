package me.alientation.doomboheadplugin.customgui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages GUIs
 */
public class CustomGUIManager {
	

	//Stores CustomGUI mappings
	private final Map<String,CustomGUI> GUI_MAP;
	private final Map<Inventory,CustomGUI> INVENTORY_MAP;
	private final JavaPlugin plugin;

	/**
	 * Constructs a GUI Manager for this plugin
	 *
	 * @param plugin Java Plugin
	 */
	public CustomGUIManager(JavaPlugin plugin) {
		this.plugin = plugin;
		this.GUI_MAP = new HashMap<>();
		this.INVENTORY_MAP = new HashMap<>();
	}
	
	/**
	 * Loads in a specified GUI from server save file to prevent having to waste resources maintaining inventories
	 */
	public void load() {
		
	}
	
	/**
	 * 
	 */
	public void save() {
		
	}

	/**
	 *
	 *
	 * @param inventory
	 * @param id
	 */
	public void addInventory(CustomGUI inventory, String id) {
		this.GUI_MAP.put(id, inventory);
		this.INVENTORY_MAP.put(inventory.getInventory(), inventory);
	}
	
	public JavaPlugin getPlugin() {
		return this.plugin;
	}
	
	public CustomGUI getGUI(String id) {
		return this.GUI_MAP.get(id);
	}
	
	public CustomGUI getGUI(Inventory inv) {
		return this.INVENTORY_MAP.get(inv);
	}
}
