package me.alientation.doomboheadplugin.customgui.annotations;

import org.bukkit.event.inventory.InventoryType;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface CustomGUIBlueprintAnnotation {
	Class<Object> BASE_CLASS = Object.class;
	String blueprintID();
	String blueprintTitle() default "";
	InventoryType blueprintInventoryType() default InventoryType.CHEST;
	int size() default 0;

	//perhaps abstract this out to blueprint listener
	String guiListenerMethod() default "";
	Class<?> guiListenerClass() default Object.class;

	//perhaps abstract this out to blueprint settings?
	boolean copiesUniqueToPlayer() default false;
	boolean allowOverridingCopies() default false;
}
