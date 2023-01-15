package me.alientation.doomboheadplugin.customcommand.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Command name aliases
 */
@Retention(RUNTIME)
@Target({ METHOD})
@Repeatable(CommandAliasAnnotations.class)
public @interface CommandAliasAnnotation {
	String value();
}

@Retention(RUNTIME)
@Target({METHOD})
@interface CommandAliasAnnotations {
	CommandAliasAnnotation[] value();
}
