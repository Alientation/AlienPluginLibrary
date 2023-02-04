package me.alientation.doomboheadplugin.customgui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a copy of a CustomGUIBlueprint
 */
public class CustomGUI {
    private final String guiID; //id of this blueprint copy
    private final String guiTitle; //title to be displayed
    private final CustomGUIBlueprint guiBlueprint; //the parent blueprint
    private final Inventory inventory; //inventory that holds this gui
    private final Map<Integer, ItemSlot> slotsMap = new HashMap<>(); //map of slot location to ItemSlot


    public static class Builder {

        String guiID, guiTitle;
        CustomGUIBlueprint guiBlueprint;

        public Builder() {

        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder guiID(String guiID) {
            this.guiID = guiID;
            return this;
        }

        public Builder guiTitle(String guiTitle) {
            this.guiTitle = guiTitle;
            return this;
        }

        public Builder guiBlueprint(CustomGUIBlueprint guiBlueprint) {
            this.guiBlueprint = guiBlueprint;
            if (this.guiTitle == null) this.guiTitle = guiBlueprint.getBlueprintTitle();
            return this;
        }

        public void verify() {
            if (this.guiID == null) throw new IllegalStateException("guiID cannot be null");
            if (this.guiBlueprint == null) throw new IllegalStateException("guiBlueprint cannot be null");
        }

        public CustomGUI build() {
            return new CustomGUI(this);
        }
    }

    /**
     * Builds using Builder pattern
     *
     * @param builder Builder pattern
     */
    public CustomGUI(@NotNull Builder builder) {
        this.guiID = builder.guiTitle;
        this.guiTitle = builder.guiTitle;
        this.guiBlueprint = builder.guiBlueprint;

        if (guiBlueprint.getBlueprintInventoryType() == InventoryType.CHEST)
            this.inventory = Bukkit.createInventory(null, guiBlueprint.size(), guiTitle);
        else
            this.inventory = Bukkit.createInventory(null, guiBlueprint.getBlueprintInventoryType(), guiTitle);
    }

    public void onClick(InventoryClickEvent event) {

    }

    public String getGuiID() {
        return guiID;
    }

    public String getGuiTitle() {
        return guiTitle;
    }

    public CustomGUIBlueprint getGuiBlueprint() {
        return guiBlueprint;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ItemSlot getSlot(int index) {
        if (getGuiBlueprint().isOutOfBounds(index)) throw new IndexOutOfBoundsException("Index " + index + " is out of bounds of " + this);
        return slotsMap.get(index);
    }

    /**
     * Updates slot map for the given index and the inventory
     *
     * @param index Index of the updated slot
     * @param item New slot
     */
    public void setSlot(int index, ItemSlot item) {
        if (guiBlueprint.isOutOfBounds(index)) throw new IndexOutOfBoundsException("Index " + index + " out of bounds in " + this);

        this.slotsMap.put(index, item);

        this.inventory.setItem(index,item.getItem());
    }

    public void moveSlot(int indexFrom, int indexTo) {
        //todo
    }

    public void swapSlot(int indexFrom, int indexTo) {
        //todo
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public String toString() {
        return guiID + "@" + guiTitle + ":" + guiBlueprint;
    }
}
