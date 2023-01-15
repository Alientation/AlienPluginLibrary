package me.alientation.doomboheadplugin.customcommand.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Command Name and ID
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface CommandAnnotation {
	String commandName();
	String commandID();
}
