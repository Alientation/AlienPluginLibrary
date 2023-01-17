package me.alientation.doomboheadplugin.customcommand.annotations.commands;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Base annotation for a command executor method
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface CommandAnnotation {
	String name();
	String id();
	String[] aliases() default {};
	String description() default "";
	String usage() default "";
}
