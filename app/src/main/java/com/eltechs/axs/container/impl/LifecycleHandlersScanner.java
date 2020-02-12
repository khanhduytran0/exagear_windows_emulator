package com.eltechs.axs.container.impl;

import com.eltechs.axs.container.annotations.PostAdd;
import com.eltechs.axs.container.annotations.PreRemove;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.ReflectionHelpers;
import com.eltechs.axs.helpers.ReflectionHelpers.Filters;
import com.eltechs.axs.helpers.ReflectionHelpers.MethodCallback;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class LifecycleHandlersScanner {
    private LifecycleHandlersScanner() {
    }

    public static List<LifecycleHandlerMethod> listPostAddActions(Class<?> cls) {
        final ArrayList arrayList = new ArrayList();
        try {
            ReflectionHelpers.doWithMethods(cls, new MethodCallback() {
                public void apply(Method method) throws IllegalArgumentException, IllegalAccessException {
                    boolean z = method.getParameterTypes().length != 0;
                    Assert.isFalse(ReflectionHelpers.isStatic(method), String.format("The method %s is marked with @PostAdd annotation and must be a member method.", new Object[]{method}));
                    Assert.isFalse(z, String.format("The method %s is marked with @PostAdd annotation and must have no parameters.", new Object[]{method}));
                    method.setAccessible(true);
                    arrayList.add(new LifecycleHandlerMethod(method));
                }
            }, Filters.instanceMethodsBearingAnnotation(PostAdd.class));
        } catch (IllegalAccessException unused) {
            Assert.unreachable();
        }
        arrayList.trimToSize();
        return arrayList;
    }

    public static List<LifecycleHandlerMethod> listPreRemoveActions(Class<?> cls) {
        final ArrayList arrayList = new ArrayList();
        try {
            ReflectionHelpers.doWithMethods(cls, new MethodCallback() {
                public void apply(Method method) throws IllegalArgumentException, IllegalAccessException {
                    boolean z = method.getParameterTypes().length != 0;
                    Assert.isFalse(ReflectionHelpers.isStatic(method), String.format("The method %s is marked with @PreRemove annotation and must be a member method.", new Object[]{method}));
                    Assert.isFalse(z, String.format("The method %s is marked with @PreRemove annotation and must have no parameters.", new Object[]{method}));
                    method.setAccessible(true);
                    arrayList.add(new LifecycleHandlerMethod(method));
                }
            }, Filters.instanceMethodsBearingAnnotation(PreRemove.class));
        } catch (IllegalAccessException unused) {
            Assert.unreachable();
        }
        arrayList.trimToSize();
        return arrayList;
    }
}
