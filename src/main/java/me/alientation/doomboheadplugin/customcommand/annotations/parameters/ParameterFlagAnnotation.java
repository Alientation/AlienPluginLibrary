package me.alientation.doomboheadplugin.customcommand.annotations.parameters;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Flags what kind of parameters is required to forward to a supplied method
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ParameterFlagAnnotation {

}
