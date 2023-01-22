package me.alientation.doomboheadplugin.customgui;

import java.util.HashMap;
import java.util.Map;

import me.alientation.doomboheadplugin.customgui.listeners.InventoryListener;
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

	private final InventoryListener inventoryListener;

	/**
	 * Constructs a GUI Manager for this plugin
	 *
	 * @param plugin Java Plugin
	 */
	public CustomGUIManager(JavaPlugin plugin) {
		this.plugin = plugin;
		this.GUI_MAP = new HashMap<>();
		this.INVENTORY_MAP = new HashMap<>();

		//registers listener to server to talk with the gui manager
		this.inventoryListener = new InventoryListener(plugin, this);
		plugin.getServer().getPluginManager().registerEvents(this.inventoryListener,plugin);
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
	 * Adds CustomGUI inventory and id key to mappings
	 *
	 * @param id id of the CustomGUI
	 * @param inventory CustomGUI inventory to be managed by this manager
	 */
	public void addInventory(String id, CustomGUI inventory) {
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

	public InventoryListener getInventoryListener() {
		return this.inventoryListener;
	}
}
