package me.alientation.doomboheadplugin.customgui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a slot in a CustomGUI inventory and actions associated with it
 */
public class ItemSlot {

	//slot id on the GUI inventory for this item slot
	private final int slotID;

	//parent gui container
	private final CustomGUI guiParent;

	//item represented at this slot
	private ItemStack item;

	//executor when clicked
	private Method actionMethod;

	static class Builder {
		int slotID;
		CustomGUI guiParent;
		ItemStack item;
		Method actionMethod;

		public Builder() {
			slotID = -1;
		}

		public Builder newInstance() {
			return new Builder();
		}

		public void verify() {
			if (slotID == -1) throw new IllegalStateException("slotID must be instantiated and valid");
			if (guiParent == null) throw new IllegalStateException("GUI parent must be instantiated");
			if (guiParent.isOutOfBounds(slotID)) throw new IllegalStateException("slotID must be valid");
		}

		public ItemSlot build() {
			verify();
			return new ItemSlot(this);
		}
	}

	/**
	 * Builds an item slot using Builder pattern
	 *
	 * @param builder builder pattern
	 */
	public ItemSlot(Builder builder) {
		this.slotID = builder.slotID;
		this.guiParent = builder.guiParent;
		this.item = builder.item;
		this.actionMethod = builder.actionMethod;
	}

	/**
	 * Processes Item Click by using reflection to invoke the supplied action method
	 *
	 * @param gui CustomGUI where the click event happened
	 * @param e Inventory click event
	 */
	public void onItemClick(CustomGUI gui, InventoryClickEvent e) {
		if (this.actionMethod != null) {
			
			Object[] params = new Object[this.actionMethod.getParameterCount()];
			int paramsIndex = 0;
			
			/*
			 * TODO: Add parameter flag annotations so that the user can greater customize the parameters that get accepted
			 */
			for (Class<?> c : this.actionMethod.getParameterTypes()) {
				if (c == CustomGUI.class)	params[paramsIndex] = gui;
				else if (c == InventoryClickEvent.class) 	params[paramsIndex] = e;
				else 							params[paramsIndex] = null;
				paramsIndex++;
			}
			
			try {
				this.actionMethod.invoke(this.actionMethod.getClass(), params);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
			}
		} else {
			
			
		}
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public void setItem(ItemStack item) { 
		this.item = item;
	}
	
	public Method getActionMethod() {
		return this.actionMethod;
	}
	
	public void setActionMethod(Method actionMethod) {
		this.actionMethod = actionMethod;
	}
	
	public int getSlotID() {
		return this.slotID;
	}
	
	public CustomGUI getGUIHolder() {
		return this.guiParent;
	}
}
