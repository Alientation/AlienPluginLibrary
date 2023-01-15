package me.alientation.doomboheadplugin.customgui;

import java.util.HashMap;
import java.util.Map;

import me.alientation.doomboheadplugin.customgui.listeners.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class CustomGUI {
	
	private String id;
	
	private Inventory inventory;
	private String title;
	private int size;
	private InventoryType inventoryType;
	
	private GUIListener guiListener;
	
	private Map<Integer, ItemSlot> slotsMap = new HashMap<>();
	
	public CustomGUI(String name, InventoryType type, String id, GUIListener guiListener) {
		this.id = id;
		this.inventory = Bukkit.createInventory(null, type, name);
		this.title = name;
		this.size = 0;
		this.inventoryType = type;
		this.guiListener = guiListener;
	}
	
	public CustomGUI(String name, int size, String id, GUIListener guiListener) {
		this.id = id;
		this.inventory = Bukkit.createInventory(null, size, name);
		this.title = name;
		this.size = size;
		this.inventoryType = InventoryType.CHEST;
		this.guiListener = guiListener;
	}
	
	public void open(Player player) {
		player.openInventory(this.inventory);
	}
	
	public boolean isOutOfBounds(int index) {
		return false;
	}

	public ItemSlot getSlot(int index) {
		return slotsMap.get(index);
	}
	
	public void setSlot(int index, ItemSlot item) {
		this.inventory.setItem(index, item.getItem());
		
		this.slotsMap.put(index, item);
	}
	
	public String getID() {
		return this.id;
	}
	
	public Inventory getInventory() { 
		return this.inventory;
	}
	
	public String getName() {
		return this.title;
	}
	
	public int size() {
		return this.size;
	}
	
	public InventoryType type() {
		return this.inventoryType;
	}
	
	public GUIListener getGUIListener() {
		return this.guiListener;
	}
}
