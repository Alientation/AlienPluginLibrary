package me.alientation.doomboheadplugin.customgui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a slot in a CustomGUIBlueprint inventory and actions associated with it
 */
public class ItemSlot {
	private final int slotID; //slot id on the GUI inventory for this item slot
	private final CustomGUIBlueprint guiParent; //parent gui container
	private ItemStack item; //item represented at this slot
	private Method actionMethod; //executor when clicked
	private boolean disabled, frozen, hidden; //whether item click events will be accepted, whether the item is locked in place or not, whether the item is hidden

	static class Builder {
		int slotID;
		CustomGUIBlueprint guiParent;
		ItemStack item;
		Method actionMethod;

		public Builder() {
			slotID = -1;
		}

		public static Builder newInstance() {
			return new Builder();
		}

		public Builder slotID(int slotID) {
			this.slotID = slotID;
			return this;
		}

		public Builder guiParent(CustomGUIBlueprint guiParent) {
			this.guiParent = guiParent;
			return this;
		}

		public Builder item(ItemStack item) {
			this.item = item;
			return this;
		}

		public Builder actionMethod(Method actionMethod) {
			this.actionMethod = actionMethod;
			return this;
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
	public ItemSlot(@NotNull Builder builder) {
		this.slotID = builder.slotID;
		this.guiParent = builder.guiParent;
		this.item = builder.item;
		this.actionMethod = builder.actionMethod;
	}

	/**
	 * Processes Item Click by using reflection to invoke the supplied action method
	 *
	 * @param gui CustomGUIBlueprint where the click event happened
	 * @param e Inventory click event
	 */
	public void onItemClick(CustomGUI gui, InventoryClickEvent e) {
		if (this.actionMethod == null) return;

		Object[] params = new Object[this.actionMethod.getParameterCount()];
		int paramsIndex = 0;

		//TODO: Add parameter flag annotations so that the user can greater customize the parameters that get accepted
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
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public void setItem(ItemStack item) {
		if (this.guiParent.getSlot(this.slotID) != this) throw new IllegalStateException(this + " is not a valid slot in parent " + guiParent);

		this.item = item;
		this.guiParent.setSlot(this.slotID,this);
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
	
	public CustomGUIBlueprint getGUIHolder() {
		return this.guiParent;
	}

	@Override
	public String toString() {
		return slotID + ":" + item;
	}
}
