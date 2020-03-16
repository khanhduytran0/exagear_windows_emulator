package com.eltechs.axs.container.impl;

import com.eltechs.axs.helpers.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AutowiringRequest {
    private final Class<?> argumentType;
    private final Method setter;

    public AutowiringRequest(Class<?> cls, Method method) {
        this.argumentType = cls;
        this.setter = method;
    }

    public boolean isInterestedIn(Class<?> cls) {
        return this.argumentType.isAssignableFrom(cls);
    }

    public void inject(Object obj, Object obj2) {
        try {
            this.setter.invoke(obj, new Object[]{obj2});
        } catch (IllegalAccessException unused) {
            Assert.state(false, "@Autowired setter methods are always marked accessible; can't get IllegalAccessException.");
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            Assert.state(targetException instanceof RuntimeException);
            throw new RuntimeException("An @Autowired setter has thrown a runtime exception.", targetException);
        }
    }
}
