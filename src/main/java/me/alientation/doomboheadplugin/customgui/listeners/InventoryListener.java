package me.alientation.doomboheadplugin.customgui.listeners;

import me.alientation.doomboheadplugin.customgui.CustomGUIBlueprint;
import me.alientation.doomboheadplugin.customgui.CustomGUIBlueprintManager;
import me.alientation.doomboheadplugin.customgui.ItemSlot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Listens to inventory events and forwards if necessary
 */
public class InventoryListener implements Listener {
	
	private final JavaPlugin plugin;
	private final CustomGUIBlueprintManager manager;

	/**
	 * Constructs inventory listener specific to a gui manager
	 *
	 * @param plugin Java Plugin
	 * @param manager GUI Manager
	 */
	public InventoryListener(JavaPlugin plugin, CustomGUIBlueprintManager manager) {
		this.plugin = plugin;
		this.manager = manager;
	}
	
	
	@EventHandler
	public void onInvClick(@NotNull InventoryClickEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		if (e.getClickedInventory() instanceof PlayerInventory) {
			gui.getGUIListener().onPlayerInventoryClick(gui, e);
			return;
		}



		ItemSlot itemSlot = gui.getSlot(e.getRawSlot());
		if (itemSlot != null) {
			itemSlot.onItemClick(gui, e);
		}
	}
	
	
	@EventHandler
	public void onInventoryOpen(@NotNull InventoryOpenEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onOpen(gui,e);
	}
	
	@EventHandler
	public void onInventoryClose(@NotNull InventoryCloseEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onClose(gui,e);
	}
	
	@EventHandler
	public void onBrewComplete(@NotNull BrewEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getContents());
		
		if (gui == null) return;
		
		gui.getGUIListener().onBrewComplete(gui,e);
	}
	
	@EventHandler
	public void onCraftItem(@NotNull CraftItemEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onCraftItem(gui,e);
	}
	
	@EventHandler
	public void onFurnaceExtract(@NotNull FurnaceExtractEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getPlayer().getOpenInventory().getTopInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onFurnaceExtract(gui,e);
	}
	
	@EventHandler
	public void onInventoryDrag(@NotNull InventoryDragEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onInventoryDrag(gui,e);
	}
	
	@EventHandler
	public void onPlayerAction(@NotNull InventoryEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onPlayerAction(gui,e);
	}
	
	@EventHandler
	public void onInventoryInteract(@NotNull InventoryInteractEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onInventoryInteract(gui,e);
	}
	
	@EventHandler
	public void onInventoryMoveItem(@NotNull InventoryMoveItemEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getSource());
		
		if (gui != null)
			gui.getGUIListener().onInventoryItemExit(gui,e);
		
		gui = this.manager.getGUI(e.getDestination());
		
		if (gui != null)
			gui.getGUIListener().onInventoryItemEnter(gui, e);
		
		gui = this.manager.getGUI(e.getInitiator());
		
		if (gui != null)
			gui.getGUIListener().onInventoryItemMoveInitated(gui, e);
	}
	
	@EventHandler
	public void onInventoryPickupItem(@NotNull InventoryPickupItemEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onInventoryPickupItem(gui,e);
	}
	
	@EventHandler
	public void onAnvilInsert(@NotNull PrepareAnvilEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onAnvilInsert(gui,e);
	}
	
	@EventHandler
	public void onCraftInsert(@NotNull PrepareItemCraftEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onCraftInsert(gui,e);
	}
	
	@EventHandler
	public void onSmithingInsert(@NotNull PrepareSmithingEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onSmithingInsert(gui,e);
	}
	
	@EventHandler
	public void onSmithItem(@NotNull SmithItemEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onSmithItem(gui,e);
	}
	
	@EventHandler
	public void onTradeSelect(@NotNull TradeSelectEvent e) {
		CustomGUIBlueprint gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onTradeSelect(gui,e);
	}
	
	public CustomGUIBlueprintManager getCustomGUIManager() { return this.manager; }
	public JavaPlugin getPlugin() { return this.plugin; }
}
