package me.alientation.doomboheadplugin.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AnnotationUtil {

    /**
     * Helper grabs methods matching search criteria
     *
     * @param clazz     class containing the methods
     * @param requiredAnnotationClasses     required annotations present on method
     * @param annotationClasses             optional annotations
     * @param disallowedAnnotationClasses   blacklisted annotations
     * @param assignableFrom    to be honest, I don't know what these are for
     * @param assignableTo      to be honest, I don't know what these are for
     * @return      List of methods matching the search criteria
     */
    public static List<Method> getMethodsWithAnnotation(Class<?> clazz, //class containing the methods
                                                        List<Class<? extends Annotation>> requiredAnnotationClasses,    //required annotations present on method
                                                        List<Class<? extends Annotation>> annotationClasses,            //optional annotations
                                                        List<Class<? extends Annotation>> disallowedAnnotationClasses,  //blacklisted annotations
                                                        Class<?> assignableFrom,    //to be honest, I don't know what these are for
                                                        Class<?> assignableTo)      //to be honest, I don't know what these are for
    {
        //stores list of methods matching the query
        List<Method> methods = new ArrayList<>();


        while (clazz != null && assignableFrom.isAssignableFrom(clazz) && clazz.isAssignableFrom(assignableTo)) {
            outerLoop:
            for (Method method : clazz.getMethods()) {
                if (disallowedAnnotationClasses != null)
                    for (Class<? extends Annotation> disallowedAnnotationClass : disallowedAnnotationClasses)
                        if (method.isAnnotationPresent(disallowedAnnotationClass))
                            continue outerLoop;
                if (requiredAnnotationClasses != null)
                    for (Class<? extends Annotation> requiredAnnotationClass : requiredAnnotationClasses)
                        if (method.isAnnotationPresent(requiredAnnotationClass))
                            continue outerLoop;
                if (annotationClasses != null)
                    for (Class<? extends Annotation> annotationClass : annotationClasses)
                        if (method.isAnnotationPresent(annotationClass)) {
                            methods.add(method);
                            continue outerLoop;
                        }
            }
        }
        return methods;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass, Class<?> assignableFrom, Class<?> assignableTo) {
        while (clazz != null && assignableFrom.isAssignableFrom(clazz) && (clazz.isAssignableFrom(assignableTo) || assignableTo == null)) {
            T annotation = clazz.getAnnotation(annotationClass);
            if (annotation != null)
                return annotation;
            Class<?> superClass = clazz.getSuperclass();
            while (superClass != null && assignableFrom.isAssignableFrom(superClass) && (superClass.isAssignableFrom(assignableTo) || assignableTo == null)) {
                annotation = superClass.getAnnotation(annotationClass);
                if (annotation != null)
                    return annotation;
                superClass = superClass.getSuperclass();
            }
            clazz = clazz.getEnclosingClass();
        }
        return null;
    }

    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        return getAnnotation(clazz,annotationClass,null,null);
    }

    public static <T extends Annotation> T getAnnotationAssignableFrom(Class<?> clazz, Class<T> annotationClass, Class<T> assignableFrom) {
        return getAnnotation(clazz,annotationClass,assignableFrom,null);
    }

    public static <T extends Annotation> T getAnnotationAssignableTo(Class<?> clazz, Class<T> annotationClass, Class<T> assignableTo) {
        return getAnnotation(clazz,annotationClass,null,assignableTo);
    }

    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        return getAnnotation(clazz,annotationClass) != null;
    }

    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass, Class<?> assignableFrom, Class<?> assignableTo) {
        return getAnnotation(clazz,annotationClass,assignableFrom,assignableTo) != null;
    }

    public static boolean hasAnnotationAssignableFrom(Class<?> clazz, Class<? extends Annotation> annotationClass, Class<?> assignableFrom) {
        return getAnnotation(clazz,annotationClass,assignableFrom,null) != null;
    }

    public static boolean hasAnnotationAssignableTo(Class<?> clazz, Class<? extends Annotation> annotationClass, Class<?> assignableTo) {
        return getAnnotation(clazz,annotationClass,null,assignableTo) != null;
    }
}
