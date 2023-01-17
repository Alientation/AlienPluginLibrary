package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Details the quantity and type of arguments of the command. Use in the same order as what the command arguments order is
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface CommandArgumentAnnotations {
    CommandArgumentAnnotation[] value();
}
