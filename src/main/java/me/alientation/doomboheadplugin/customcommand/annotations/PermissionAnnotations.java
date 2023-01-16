package me.alientation.doomboheadplugin.customcommand.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Sets the permissions for the command todo incorporate with CustomPermissions
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface PermissionAnnotations {
    PermissionAnnotation[] value();
}