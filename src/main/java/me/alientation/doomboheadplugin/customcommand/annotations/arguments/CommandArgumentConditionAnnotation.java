package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Details the conditions for the argument
 */
@Retention(RUNTIME)
@Target({ METHOD})
public @interface CommandArgumentConditionAnnotation {//load these attributes into CustomCommand
    String[] matchCondition() default {}; //custom condition checking
}
