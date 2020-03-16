package com.eltechs.axs.container.impl;

import com.eltechs.axs.container.annotations.Autowired;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.ReflectionHelpers;
import com.eltechs.axs.helpers.ReflectionHelpers.Filters;
import com.eltechs.axs.helpers.ReflectionHelpers.MethodCallback;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AutowiredPropertiesScanner {
    private AutowiredPropertiesScanner() {
    }

    public static List<AutowiringRequest> listAutowiringRequests(Class<?> cls) {
        final ArrayList arrayList = new ArrayList();
        try {
            ReflectionHelpers.doWithMethods(cls, new MethodCallback() {
                public void apply(Method method) throws IllegalArgumentException, IllegalAccessException {
                    Class[] parameterTypes = method.getParameterTypes();
                    boolean z = 1 == parameterTypes.length;
                    Assert.isFalse(ReflectionHelpers.isStatic(method), String.format("The method %s is marked with @Autowired annotation and must be a member method.", new Object[]{method}));
                    Assert.isTrue(z, String.format("The method %s is marked with @Autowired annotation and must be a setter taking one argument.", new Object[]{method}));
                    Assert.isFalse(ReflectionHelpers.canThrowCheckedExceptions(method), String.format("The method %s is marked with @Autowired annotation and must not throw checked exceptions.", new Object[]{method}));
                    method.setAccessible(true);
                    arrayList.add(new AutowiringRequest(parameterTypes[0], method));
                }
            }, Filters.instanceMethodsBearingAnnotation(Autowired.class));
        } catch (IllegalAccessException unused) {
            Assert.unreachable();
        }
        arrayList.trimToSize();
        return arrayList;
    }
}
