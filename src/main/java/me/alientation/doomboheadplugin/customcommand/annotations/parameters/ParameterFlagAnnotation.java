package me.alientation.doomboheadplugin.customcommand.annotations.parameters;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** todo decide whether or not to have this because technically any and all data can be accessed through the command sender
 * Flags what kind of parameters are needed to forward to a supplied method
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ParameterFlagAnnotation {

}
