package me.alientation.doomboheadplugin.customgui;

import java.util.HashMap;
import java.util.Map;

import me.alientation.doomboheadplugin.customgui.listeners.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Represents a GUI based within a chest inventory and handles functionality like button clicks
 */
public class CustomGUI {

	//id of the custom gui
	private final String id;

	//title of the bukkit inventory
	private final String title;

	//bukkit inventory that holds the gui
	private final Inventory inventory;

	//size of the bukkit inventory
	private final int size;

	//attached listener so that this gui can listen to events
	private GUIListener guiListener;

	//map of slot location to ItemSlot
	private Map<Integer, ItemSlot> slotsMap = new HashMap<>();


	static class Builder {
		String id, title;
		Inventory inventory;
		int size;
		GUIListener guiListener;
		public Builder() {
			size = 54;
		}

		public static Builder newInstance() {
			return new Builder();
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder inventory(Inventory inventory) {
			this.inventory = inventory;
			return this;
		}

		public Builder size(int size) {
			this.size = size;
			return this;
		}

		public Builder guiListener(GUIListener guiListener) {
			this.guiListener = guiListener;
			return this;
		}

		public void verify() {
			if (id == null) throw new IllegalStateException("GUI id cannot be null");
			if (size == 0 || size % 9 != 0 || size > 54) throw new IllegalStateException("GUI size must be a multiple of 9 maxed at 54");
			if (inventory == null) inventory = Bukkit.createInventory(null, size, title);
		}

		public CustomGUI build() {
			verify();
			return new CustomGUI(this);
		}
	}

	/**
	 * Constructs a custom GUI using Builder pattern
	 *
	 * @param builder builder pattern
	 */
	public CustomGUI(Builder builder) {
		this.id = builder.id;
		this.inventory = builder.inventory;
		this.title = builder.title;
		this.size = builder.size;
		this.guiListener = builder.guiListener;
	}

	public void open(Player player) {
		player.openInventory(this.inventory);
	}

	public boolean isOutOfBounds(int index) {
		return index >= size();
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
	
	public GUIListener getGUIListener() {
		return this.guiListener;
	}
}
