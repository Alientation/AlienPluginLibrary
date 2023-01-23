package me.alientation.doomboheadplugin.customgui;

import me.alientation.doomboheadplugin.customgui.annotations.GUIMarkerAnnotation;
import me.alientation.doomboheadplugin.customgui.annotations.SlotIDAnnotation;
import me.alientation.doomboheadplugin.customgui.exceptions.UnknownGUIException;
import me.alientation.doomboheadplugin.customgui.exceptions.UnknownManagerException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Links annotated methods with the Custom GUIs
 */
public class CustomGUIBlueprintAPI {
	
	private final Map<String,Method> methodMap;
	private CustomGUIBlueprintManager manager;

	public CustomGUIBlueprintAPI(CustomGUIBlueprintManager manager) {
		this.methodMap = new HashMap<>();
		registerManager(manager);
	}
	
	public void registerManager(CustomGUIBlueprintManager manager) {
		this.manager = manager;
	}

	/**
	 * Registers annotated methods
	 */
	public void registerMethods() {
		if (this.manager == null)
			throw new UnknownManagerException();
		
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(GUIMarkerAnnotation.class) && method.isAnnotationPresent(SlotIDAnnotation.class)) {
				String guiID = method.getAnnotation(GUIMarkerAnnotation.class).value();
				CustomGUIBlueprint gui = this.manager.getGUI(guiID);

				//gui does not exist yet
				if (gui == null) throw new UnknownGUIException();

				//registers slots
				for (SlotIDAnnotation slotIDAnnotation : method.getAnnotationsByType(SlotIDAnnotation.class)) {
					int slotID = slotIDAnnotation.value();

					if (gui.isOutOfBounds(slotID)) throw new IndexOutOfBoundsException("slot " + slotID + " out of bounds of " + gui);
					if (gui.getSlot(slotID) != null) throw new IllegalStateException("slot " + slotID + " already present in " + gui);

					//TODO: Add annotations that allow for creating itemstacks

					gui.getSlot(slotID).setActionMethod(method);
					gui.setSlot(slotID, ItemSlot.Builder.newInstance()
									.slotID(slotID)
									.guiParent(gui)
									.build()
					);

					this.methodMap.put(guiID + "@" + slotID, method);
				}
			}
		}
	}
	
	public Map<String,Method> getMethodMap() {
		return this.methodMap;
	}
	
	public boolean containsMethods(String id) {
		return this.methodMap.get(id) != null;
	}
	
	public CustomGUIBlueprintManager getManager() {
		return this.manager;
	}
}
