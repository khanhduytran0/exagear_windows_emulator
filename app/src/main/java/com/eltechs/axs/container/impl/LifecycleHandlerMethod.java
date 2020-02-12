package com.eltechs.axs.container.impl;

import com.eltechs.axs.helpers.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LifecycleHandlerMethod {
    private final Method impl;

    public LifecycleHandlerMethod(Method method) {
        this.impl = method;
    }

    public void apply(Object obj) {
        try {
            this.impl.invoke(obj, new Object[0]);
        } catch (IllegalAccessException unused) {
            Assert.state(false, "Lifecycle handler methods are always marked accessible; can't get IllegalAccessException.");
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            Assert.state(targetException instanceof RuntimeException);
            throw new RuntimeException("A @PostAdd or @PreRemove method has thrown a runtime exception.", targetException);
        }
    }
}
