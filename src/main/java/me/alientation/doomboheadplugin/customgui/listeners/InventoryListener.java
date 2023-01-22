package me.alientation.doomboheadplugin.customgui.listeners;

import me.alientation.doomboheadplugin.customgui.CustomGUI;
import me.alientation.doomboheadplugin.customgui.CustomGUIManager;
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

/**
 * Listens to inventory events and forwards if necessary
 */
public class InventoryListener implements Listener {
	
	private JavaPlugin plugin;
	private CustomGUIManager manager;
	
	public InventoryListener(JavaPlugin plugin, CustomGUIManager manager) {
		this.plugin = plugin;
		this.manager = manager;
	}
	
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
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
	public void onInventoryOpen(InventoryOpenEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onOpen(gui,e);
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onClose(gui,e);
	}
	
	@EventHandler
	public void onBrewComplete(BrewEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getContents());
		
		if (gui == null) return;
		
		gui.getGUIListener().onBrewComplete(gui,e);
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onCraftItem(gui,e);
	}
	
	@EventHandler
	public void onFurnaceExtract(FurnaceExtractEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getPlayer().getOpenInventory().getTopInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onFurnaceExtract(gui,e);
	}
	
	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onInventoryDrag(gui,e);
	}
	
	@EventHandler
	public void onPlayerAction(InventoryEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onPlayerAction(gui,e);
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryInteractEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onInventoryInteract(gui,e);
	}
	
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getSource());
		
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
	public void onInventoryPickupItem(InventoryPickupItemEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onInventoryPickupItem(gui,e);
	}
	
	@EventHandler
	public void onAnvilInsert(PrepareAnvilEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onAnvilInsert(gui,e);
	}
	
	@EventHandler
	public void onCraftInsert(PrepareItemCraftEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onCraftInsert(gui,e);
	}
	
	@EventHandler
	public void onSmithingInsert(PrepareSmithingEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onSmithingInsert(gui,e);
	}
	
	@EventHandler
	public void onSmithItem(SmithItemEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onSmithItem(gui,e);
	}
	
	@EventHandler
	public void onTradeSelect(TradeSelectEvent e) {
		CustomGUI gui = this.manager.getGUI(e.getInventory());
		
		if (gui == null) return;
		
		gui.getGUIListener().onTradeSelect(gui,e);
	}
	
	public CustomGUIManager getCustomGUIManager() { return this.manager; }
	public JavaPlugin getPlugin() { return this.plugin; }
}
