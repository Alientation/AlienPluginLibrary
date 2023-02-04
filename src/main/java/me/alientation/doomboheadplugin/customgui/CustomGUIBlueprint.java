package me.alientation.doomboheadplugin.customgui;

import java.util.HashMap;
import java.util.Map;

import me.alientation.doomboheadplugin.customgui.listeners.GUIListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a GUI based within a chest inventory and handles functionality like button clicks
 */
public class CustomGUIBlueprint {
	public static final String MAIN_COPY_ID = "CustomGUI Blueprint Main Copy";
	private CustomGUIBlueprintManager manager;
	private final String blueprintID; //id of the custom gui
	private final String blueprintTitle; //title of the bukkit inventory
	private final InventoryType blueprintInventoryType;
	private final int size; //size of the bukkit inventory
	private final GUIListener blueprintGUIListener; //attached listener so that this gui can listen to events
	private final Map<Integer, ItemSlot> blueprintSlotsMap = new HashMap<>(); //map of slot location to ItemSlot
	private final Map<String,CustomGUI> guiCopies; //copies of this blueprint
	private final boolean copiesUniqueToPlayer; //when opened, whether to open a copy that is relative to the player's name or just the main copy
	private final boolean allowOverridingCopies; //automatically safe deletes the previous copy if there is one, essentially overriding past copies

	public static class Builder {
		String blueprintID, blueprintTitle;
		Map<String,CustomGUI> guiCopies;
		InventoryType blueprintInventoryType;
		int size;
		GUIListener blueprintGUIListener;
		boolean copiesUniqueToPlayer, allowOverridingCopies;
		public Builder() {
			blueprintInventoryType = InventoryType.CHEST;
			size = 54;
			guiCopies = new HashMap<>();
			copiesUniqueToPlayer = false;
		}

		public static Builder newInstance() {
			return new Builder();
		}

		public Builder blueprintID(String blueprintID) {
			this.blueprintID = blueprintID;
			return this;
		}

		public Builder blueprintTitle(String blueprintTitle) {
			this.blueprintTitle = blueprintTitle;
			return this;
		}

		public Builder guiCopies(Map<String,CustomGUI> guiCopes) {
			this.guiCopies.putAll(guiCopes);
			return this;
		}

		public Builder guiCopy(String guiID, CustomGUI gui) {
			this.guiCopies.put(guiID, gui);
			return this;
		}

		public Builder blueprintInventoryType(InventoryType blueprintInventoryType) {
			this.blueprintInventoryType = blueprintInventoryType;
			return this;
		}

		public Builder size(int size) {
			this.size = size;
			return this;
		}

		public Builder blueprintGUIListener(GUIListener blueprintGUIListener) {
			this.blueprintGUIListener = blueprintGUIListener;
			return this;
		}

		public Builder copiesUniqueToPlayer(boolean copiesUniqueToPlayer) {
			this.copiesUniqueToPlayer = copiesUniqueToPlayer;
			return this;
		}

		public Builder copiesUniqueToPlayer() {
			return copiesUniqueToPlayer(true);
		}

		public Builder allowOverridingCopies(boolean allowOverridingCopies) {
			this.allowOverridingCopies = allowOverridingCopies;
			return this;
		}

		public Builder allowOverridingCopies() {
			return allowOverridingCopies(true);
		}

		public void verify() {
			if (blueprintID == null) throw new IllegalStateException("GUI id cannot be null");

			//inventory type chest, size must be multiple of 9
			if (blueprintInventoryType == InventoryType.CHEST && (size == 0 || size % 9 != 0 || size > 54)) throw new IllegalStateException("GUI size must be multiple of 9 to 54 for a InventoryType chest");
			else if (blueprintInventoryType != InventoryType.CHEST && size != 0) throw new IllegalStateException("GUI size must not be predefined if not using Inventory Type CHEST");
		}

		public CustomGUIBlueprint build() {
			verify();
			return new CustomGUIBlueprint(this);
		}
	}

	/**
	 * Constructs a custom GUI using Builder pattern
	 *
	 * @param builder builder pattern
	 */
	public CustomGUIBlueprint(@NotNull Builder builder) {
		this.blueprintID = builder.blueprintID;
		this.blueprintTitle = builder.blueprintTitle;
		this.blueprintInventoryType = builder.blueprintInventoryType;
		this.size = builder.size;

		this.blueprintGUIListener = builder.blueprintGUIListener;
		this.guiCopies = builder.guiCopies;

		this.copiesUniqueToPlayer = builder.copiesUniqueToPlayer;
		this.allowOverridingCopies = builder.allowOverridingCopies;

		newCopy(MAIN_COPY_ID);
	}

	/**
	 * Registers a manager to this blueprint
	 *
	 * @param manager CustomGUIBlueprint manager
	 */
	public void registerManager(CustomGUIBlueprintManager manager) {
		if (this.manager != null) throw new IllegalStateException("This Blueprint's manager has already been registered!");


		this.manager = manager;

		//registers existing child copies to the manager
		for (CustomGUI copy : guiCopies.values()) manager.registerBlueprintCopy(copy.getInventory(),this);
	}

	/**
	 * Creates a new copy of this blueprint
	 *
	 * @param guiID id of the blueprint copy
	 * @return the blueprint copy
	 */
	public CustomGUI newCopy(String guiID) {
		//safety checks, make user delete copy first
		if (guiCopies.containsKey(guiID)) {
			if (allowOverridingCopies) deleteCopy(guiID);
			else throw new IllegalStateException("GUIBlueprint copy " + guiID + " already exists! Delete the copy before creating a new one");
		}

		//creates a new copy of this blueprint and maps it to the guiID
		CustomGUI newCopy = CustomGUI.Builder.newInstance().guiID(guiID).guiBlueprint(this).build();
		this.guiCopies.put(guiID,newCopy);

		//registers the blueprint copy's inventory to this blueprint in the manager
		if (manager != null) manager.registerBlueprintCopy(newCopy.getInventory(), this);

		return newCopy;
	}

	/**
	 * Deletes copy from the guiCopies and unregisters from the manager to remove all references to the copy and allow garbage collection to do its thing
	 *
	 * @param guiID id of the copy to be deleted
	 * @return the deleted copy
	 */
	public CustomGUI deleteCopy(String guiID) {
		if (this.guiCopies.get(guiID) == null) throw new IllegalStateException("GUICopy " + guiID + " does not exist!");

		manager.unregisterBlueprintCopy(guiCopies.get(guiID).getInventory());
		return this.guiCopies.remove(guiID);
	}

	public CustomGUI getCopy(String guiID) {
		return guiCopies.get(guiID);
	}

	/**
	 * Opens the inventory for the player depending on the settings of this blueprint
	 * todo have more options to specific how specific a blueprint copy is (ex. per team, per server)
	 * @param player Player to open an inventory
	 */
	public void open(@NotNull Player player) {
		if (copiesUniqueToPlayer) {
			CustomGUI playerSpecificCopy = getCopy(player.getName());
			if (playerSpecificCopy == null) playerSpecificCopy = newCopy(player.getName());
			playerSpecificCopy.open(player);
			return;
		}

		getCopy(MAIN_COPY_ID).open(player);
	}

	public boolean isOutOfBounds(int index) {
		return index >= size();
	}

	public ItemSlot getSlot(int index) {
		return blueprintSlotsMap.get(index);
	}

	/**
	 * Updates the slot for the blueprint and all children copies
	 * todo itemslot states such as frozen to prevent these modifications?
	 * @param index index of the slot to be replaced
	 * @param item the new item slot
	 */
	public void setSlot(int index, ItemSlot item) {
		if (isOutOfBounds(index)) throw new IndexOutOfBoundsException("Index " + index + " out of bounds in " + this);

		this.blueprintSlotsMap.put(index, item);

		for (CustomGUI child : guiCopies.values())
			child.setSlot(index,item);
	}
	
	public String getBlueprintID() {
		return this.blueprintID;
	}
	
	public String getBlueprintTitle() {
		return this.blueprintTitle;
	}

	public InventoryType getBlueprintInventoryType() {
		return this.blueprintInventoryType;
	}
	
	public int size() {
		return this.size;
	}
	
	public GUIListener getGUIListener() {
		return this.blueprintGUIListener;
	}

	@Override
	public String toString() {
		return "CustomGUIBlueprint:CHEST" + size + "(" + blueprintID + " | " + blueprintTitle + ")";
	}
}
