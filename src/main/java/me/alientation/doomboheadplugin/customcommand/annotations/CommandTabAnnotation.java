package me.alientation.doomboheadplugin.customcommand.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Details the command method is responsible for tab completion
 */
@Retention(RUNTIME)
@Target({ METHOD})
public @interface CommandTabAnnotation {
	String id();
	String name();

}
