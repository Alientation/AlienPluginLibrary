package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Details the custom conditions for the argument with supplied class and method names
 * <p>
 * supplied method must have 1 parameter string -> the argument being checked
 */
@Retention(RUNTIME)
@Target({ METHOD})
public @interface CommandArgumentCustomConditionAnnotation {
    Class<?> BASE_CLASS = Object.class;

    Class<?> matchConditionClass() default Object.class;
    String matchConditionMethod() default "";
}

