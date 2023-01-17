package me.alientation.doomboheadplugin.customcommand.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Details the conditions for the argument
 */
@Retention(RUNTIME)
@Target({ METHOD})
public @interface CommandArgumentConditionAnnotation {

    String matchType() default "str"; //argument's type

    String[] matchCondition() default {}; //custom condition checking
}
