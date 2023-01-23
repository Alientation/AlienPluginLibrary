package me.alientation.doomboheadplugin.customgui.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface ItemSlotAnnotation {
	Class<Object> BASE_CLASS = Object.class;
	String blueprintID();
	int slotID();

	//todo ItemStack annotation

	String actionMethod() default "";
	Class<?> actionClass() default BASE_CLASS;
}
