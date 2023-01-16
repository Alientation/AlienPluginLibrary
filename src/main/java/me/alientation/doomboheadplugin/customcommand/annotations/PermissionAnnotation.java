package me.alientation.doomboheadplugin.customcommand.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ METHOD})
@Repeatable(PermissionAnnotations.class)
public @interface PermissionAnnotation {
	String permission();

	boolean required();
}
