package me.alientation.doomboheadplugin.customcommand.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ METHOD})
public @interface CommandArgumentConditionAnnotation {

    String matchType() default "str";

    String matchCondition() default ""; //probably just use regex matching

    /**
     * Type identifiers
     */
    enum Type {
        INTEGER("int"), STRING("str"), CHARACTER("ch"), FLOAT("f");

        final String type;
        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
}
