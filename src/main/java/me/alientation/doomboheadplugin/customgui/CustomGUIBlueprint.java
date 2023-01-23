package me.alientation.doomboheadplugin.customgui;

import java.util.HashMap;
import java.util.Map;

import me.alientation.doomboheadplugin.customgui.listeners.GUIListener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a GUI based within a chest inventory and handles functionality like button clicks
 * <p>
 * todo have general gui that keeps functionality across all players but shows different stuff to each (sort of like sky block backpacks)
 *
 */
public class CustomGUIBlueprint {
	public static final String MAIN_COPY_ID = "CustomGUI Blueprint Main Copy";
	private CustomGUIBlueprintManager manager;

	//id of the custom gui
	private final String blueprintID;

	//title of the bukkit inventory
	private final String blueprintTitle;

	private final InventoryType blueprintInventoryType;

	//size of the bukkit inventory
	private final int size;

	//attached listener so that this gui can listen to events
	private final GUIListener blueprintGUIListener;

	//map of slot location to ItemSlot
	private final Map<Integer, ItemSlot> blueprintSlotsMap = new HashMap<>();

	//copies of this blueprint
	private final Map<String,CustomGUI> guiCopies;

	private final boolean copiesUniqueToPlayer;


	public static class Builder {
		String blueprintID, blueprintTitle;
		Map<String,CustomGUI> guiCopies;
		InventoryType blueprintInventoryType;
		int size;
		GUIListener blueprintGUIListener;
		boolean copiesUniqueToPlayer;
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

		newCopy(MAIN_COPY_ID);
	}

	public void registerManager(CustomGUIBlueprintManager manager) {
		if (this.manager != null) throw new IllegalStateException("This Blueprint's manager has already been registered!");
		this.manager = manager;

		for (CustomGUI copy : guiCopies.values()) manager.registerBlueprintCopy(copy.getInventory(),this);
	}

	public CustomGUI newCopy(String guiID) {
		if (guiCopies.containsKey(guiID))
			throw new IllegalStateException("GUIBlueprint copy " + guiID + " already exists! Delete the copy before creating a new one");

		CustomGUI newCopy = CustomGUI.Builder.newInstance().guiID(guiID).guiBlueprint(this).build();
		this.guiCopies.put(guiID,newCopy);

		manager.registerBlueprintCopy(newCopy.getInventory(), this);

		return newCopy;
	}

	public CustomGUI deleteCopy(String guiID) {
		if (this.guiCopies.get(guiID) == null) throw new IllegalStateException("GUICopy " + guiID + " does not exist!");

		manager.unregisterBlueprintCopy(guiCopies.get(guiID).getInventory());
		return this.guiCopies.remove(guiID);
	}

	public CustomGUI getCopy(String guiID) {
		return guiCopies.get(guiID);
	}

	public void open(@NotNull Player player) { //todo have more options to specific how specific a blueprint copy is (ex. per team, per server)
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
	
	public void setSlot(int index, ItemSlot item) { //todo itemslot states such as frozen to prevent these modifications?
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
