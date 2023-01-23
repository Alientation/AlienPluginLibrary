package me.alientation.doomboheadplugin.customgui;

import java.util.HashMap;
import java.util.Map;

import me.alientation.doomboheadplugin.customgui.listeners.InventoryListener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages GUIs
 */
public class CustomGUIBlueprintManager {
	

	//Stores CustomGUIBlueprint mappings
	private final Map<String, CustomGUIBlueprint> GUI_MAP;
	private final Map<Inventory, CustomGUIBlueprint> INVENTORY_MAP;
	private final JavaPlugin plugin;

	private InventoryListener inventoryListener;

	/**
	 * Constructs a GUI Manager for this plugin
	 *
	 * @param plugin Java Plugin
	 */
	public CustomGUIBlueprintManager(JavaPlugin plugin) {
		this.plugin = plugin;
		this.GUI_MAP = new HashMap<>();
		this.INVENTORY_MAP = new HashMap<>();
	}

	/**
	 * Adds CustomGUIBlueprint blueprint and id key to mappings
	 *
	 * @param id id of the CustomGUIBlueprint
	 * @param blueprint CustomGUIBlueprint blueprint to be managed by this manager
	 */
	public void addBlueprint(String id, CustomGUIBlueprint blueprint) {
		if (inventoryListener == null) {
			//registers listener to server to talk with the gui manager
			this.inventoryListener = new InventoryListener(plugin, this);
			plugin.getServer().getPluginManager().registerEvents(this.inventoryListener,plugin);
		}

		this.GUI_MAP.put(id, blueprint);
		blueprint.registerManager(this);
	}

	public void registerBlueprintCopy(Inventory inventory, CustomGUIBlueprint blueprint) {
		this.INVENTORY_MAP.put(inventory,blueprint);
	}

	public void unregisterBlueprintCopy(Inventory inventory) {
		this.INVENTORY_MAP.remove(inventory);
	}

	public JavaPlugin getPlugin() {
		return this.plugin;
	}
	
	public CustomGUIBlueprint getGUI(String id) {
		return this.GUI_MAP.get(id);
	}
	
	public CustomGUIBlueprint getGUI(Inventory inv) {
		return this.INVENTORY_MAP.get(inv);
	}

	public InventoryListener getInventoryListener() {
		return this.inventoryListener;
	}
}
