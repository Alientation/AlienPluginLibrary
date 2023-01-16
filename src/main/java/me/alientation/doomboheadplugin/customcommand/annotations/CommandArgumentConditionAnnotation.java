package me.alientation.doomboheadplugin.customcommand.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ METHOD})
public @interface CommandArgumentConditionAnnotation {

    Type type() default Type.STRING;

    enum Type {
        INTEGER, STRING, CHARACTER, FLOAT;

        public boolean isValid(String arg) {
            return true;
        }
    }
}
