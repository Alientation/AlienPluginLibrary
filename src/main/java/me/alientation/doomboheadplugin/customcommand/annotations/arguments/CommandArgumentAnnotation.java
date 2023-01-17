package me.alientation.doomboheadplugin.customcommand.annotations.arguments;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Details the quantity and type of arguments of the command. Use in the same order as what the command arguments order is
 */
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(CommandArgumentAnnotations.class)
public @interface CommandArgumentAnnotation {
    String name();
    CommandArgumentConditionAnnotation condition() default @CommandArgumentConditionAnnotation();
    CommandArgumentCustomConditionAnnotation customCondition() default @CommandArgumentCustomConditionAnnotation();
    String description() default "";
    String usage() default "";

}

