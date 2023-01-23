package me.alientation.doomboheadplugin.customgui.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface ParameterFlagAnnotation {
	//this will be almost a duplicate of the CustomCommand parameter flag annotation
	String value();
}
