package me.alientation.doomboheadplugin.customgui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class ItemSlot {
	private int slotID;
	private CustomGUI guiHolder;
	private ItemStack item;
	private Method actionMethod;
	
	public ItemSlot(ItemStack item, int slotID, CustomGUI guiHolder) {
		this.item = item;
		this.slotID = slotID;
		this.guiHolder = guiHolder;
	}
	
	public ItemSlot(Method actionMethod, int slotID, CustomGUI guiHolder) {
		this.actionMethod = actionMethod;
		this.slotID = slotID;
		this.guiHolder = guiHolder;
	}
	
	public ItemSlot(ItemStack item, Method actionMethod, int slotID, CustomGUI guiHolder) {
		this.item = item;
		this.actionMethod = actionMethod;
		this.slotID = slotID;
		this.guiHolder = guiHolder;
	}
	
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
	
	
	public static ItemStack createItem() {
		return null;
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
	
	public void setSlotID(int slotID) {
		this.slotID = slotID;
	}
	
	public CustomGUI getGUIHolder() {
		return this.guiHolder;
	}
}
