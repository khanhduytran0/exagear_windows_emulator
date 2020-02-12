package com.eltechs.axs.helpers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class ReflectionHelpers {

    public interface FieldCallback {
        void apply(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    public interface FieldFilter {
        boolean matches(Field field);
    }

    public static abstract class Filters {
        public static <T extends Annotation> MethodFilter instanceMethodsBearingAnnotation(final Class<T> cls) {
            return new MethodFilter() {
                public boolean matches(Method method) {
                    boolean z = false;
                    if ((method.getModifiers() & 8) != 0) {
                        return false;
                    }
                    if (method.getAnnotation(cls) != null) {
                        z = true;
                    }
                    return z;
                }
            };
        }

        public static FieldFilter publicStaticFields(final Class<?> cls) {
            return new FieldFilter() {
                public boolean matches(Field field) {
                    int modifiers = field.getModifiers();
                    if ((modifiers & 1) == 0 || (modifiers & 8) == 0) {
                        return false;
                    }
                    return cls.isAssignableFrom(field.getType());
                }
            };
        }
    }

    public interface MethodCallback {
        void apply(Method method) throws IllegalArgumentException, IllegalAccessException;
    }

    public interface MethodFilter {
        boolean matches(Method method);
    }

    private ReflectionHelpers() {
    }

    public static boolean canThrowCheckedExceptions(Method method) {
        for (Class isAssignableFrom : method.getExceptionTypes()) {
            if (!RuntimeException.class.isAssignableFrom(isAssignableFrom)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPublic(Method method) {
        return (method.getModifiers() & 1) != 0;
    }

    public static boolean isPublic(Field field) {
        return (field.getModifiers() & 1) != 0;
    }

    public static boolean isStatic(Method method) {
        return (method.getModifiers() & 8) != 0;
    }

    public static boolean isStatic(Field field) {
        return (field.getModifiers() & 8) != 0;
    }

    public static void doWithMethods(Class<?> cls, MethodCallback methodCallback) throws IllegalAccessException {
        while (cls != null) {
            for (Method apply : cls.getDeclaredMethods()) {
                methodCallback.apply(apply);
            }
            cls = cls.getSuperclass();
        }
    }

    public static void doWithMethods(Class<?> cls, MethodCallback methodCallback, MethodFilter methodFilter) throws IllegalAccessException {
        Method[] declaredMethods;
        while (cls != null) {
            for (Method method : cls.getDeclaredMethods()) {
                if (methodFilter.matches(method)) {
                    methodCallback.apply(method);
                }
            }
            cls = cls.getSuperclass();
        }
    }

    public static void doWithFields(Class<?> cls, FieldCallback fieldCallback) throws IllegalAccessException {
        while (cls != null) {
            for (Field apply : cls.getDeclaredFields()) {
                fieldCallback.apply(apply);
            }
            cls = cls.getSuperclass();
        }
    }

    public static void doWithFields(Class<?> cls, FieldCallback fieldCallback, FieldFilter fieldFilter) throws IllegalAccessException {
        Field[] declaredFields;
        while (cls != null) {
            for (Field field : cls.getDeclaredFields()) {
                if (fieldFilter.matches(field)) {
                    fieldCallback.apply(field);
                }
            }
            cls = cls.getSuperclass();
        }
    }
}
