package me.alientation.doomboheadplugin.customgui;

import me.alientation.doomboheadplugin.customgui.annotations.GUIMarkerAnnotation;
import me.alientation.doomboheadplugin.customgui.annotations.SlotIDAnnotation;
import me.alientation.doomboheadplugin.customgui.exceptions.UnknownGUIException;
import me.alientation.doomboheadplugin.customgui.exceptions.UnknownManagerException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomGUIAPI {
	
	private Map<String,Method> methodMap;
	private CustomGUIManager manager;
	
	public CustomGUIAPI() {
		this.methodMap = new HashMap<>();
	}
	
	public CustomGUIAPI(CustomGUIManager manager) {
		this.methodMap = new HashMap<>();
		registerManager(manager);
	}
	
	public void registerManager(CustomGUIManager manager) {
		this.manager = manager;
	}
	
	public void registerMethods() {
		if (this.manager == null)
			throw new UnknownManagerException();
		
		for (Method method : this.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(GUIMarkerAnnotation.class) && method.isAnnotationPresent(SlotIDAnnotation.class)) {
				String guiID = method.getAnnotation(GUIMarkerAnnotation.class).value();
				CustomGUI gui = this.manager.getGUI(guiID);
				if (gui == null)
					throw new UnknownGUIException();
				List<Integer> slotIDs = new ArrayList<>();
				for (SlotIDAnnotation slotIDAnnotation : method.getAnnotationsByType(SlotIDAnnotation.class)) {
					int slotID = slotIDAnnotation.value();
					if (gui.isOutOfBounds(slotID))
						throw new IndexOutOfBoundsException();
					slotIDs.add(slotID);
					
					/*
					 * TODO: Add annotations that allow for creating itemstacks
					 */
					
					
					gui.getSlot(slotID).setActionMethod(method);
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
	
	public CustomGUIManager getManager() {
		return this.manager;
	}
}
