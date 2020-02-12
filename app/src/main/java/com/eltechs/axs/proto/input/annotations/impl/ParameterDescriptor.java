package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.helpers.Assert;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterDescriptor {
    private final Annotation[] annotations;
    private final int index;
    private final Type type;
	private final Method method;

    private ParameterDescriptor(int i, Method mtd, Class<?> cls, Type type2, Annotation[] annotationArr) {
        this.index = i;
		this.method = mtd;
        this.type = mapPrimitiveType(cls, type2);
        this.annotations = annotationArr;
		/*
		System.out.println("ExagearX11: Param " + i + " (" + cls.getSimpleName() + ")");
		for (Annotation a : annotationArr) {
			System.out.println("ExagearX11: - " + a.annotationType().getSimpleName());
		}
		*/
    }

    public static ParameterDescriptor[] getMethodParameters(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int length = parameterTypes.length;
        ParameterDescriptor[] parameterDescriptorArr = new ParameterDescriptor[length];
        for (int i = 0; i < length; i++) {
            parameterDescriptorArr[i] = new ParameterDescriptor(i, method, parameterTypes[i], genericParameterTypes[i], parameterAnnotations[i]);
        }
        return parameterDescriptorArr;
    }

    public int getIndex() {
        return this.index;
    }

	public Method getOwnerMethod() {
		return this.method;
	}

    public Class<?> getRawType() {
        if (this.type instanceof Class) {
            return (Class<?>) this.type;
        }
        if (this.type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) this.type).getRawType();
        }
        Assert.notImplementedYet(String.format("Requests parser does not support handler parameters of type %s.", new Object[]{this.type}));
        return null;
    }

    public Type getType() {
        return this.type;
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        // T[] tArr;
        for (Annotation t : this.annotations) {
            if (t.annotationType() == cls || t.getClass().getName().equals(cls.getName())) {
                return (T) t;
            }
        }
        return null;
    }

    private Type mapPrimitiveType(Class<?> cls, Type type2) {
        if (cls == Boolean.TYPE) {
            return Boolean.class;
        }
        if (cls == Byte.TYPE) {
            return Byte.class;
        }
        if (cls == Short.TYPE) {
            return Short.class;
        }
        if (cls == Integer.TYPE) {
            return Integer.class;
        }
        return cls == Long.TYPE ? Long.class : type2;
    }
}
