package com.eltechs.axs.container;

import com.eltechs.axs.helpers.Assert;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AutowiringEnhancedObject<T> {
    private final Container container;
    private final T proxy;

    private AutowiringEnhancedObject(T t, Container container2) {
        this.proxy = t;
        this.container = container2;
    }

    public T getProxy() {
        return this.proxy;
    }

    public Container getContainer() {
        return this.container;
    }

    public static <T> AutowiringEnhancedObject<T> addAutowiring(Class<?> cls) {
        final Container container2 = new Container();
        return new AutowiringEnhancedObject<T>((T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
            @Override
            public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                String access$000 = AutowiringEnhancedObject.getPropertyNameOfSetter(method.getName());
                String access$100 = AutowiringEnhancedObject.getPropertyNameOfGetter(method.getName());
                if (access$000 != null) {
                    container2.setComponent(access$000, objArr[0]);
                    return null;
                } else if (access$100 != null) {
                    return container2.getComponent(access$100);
                } else {
                    Assert.unreachable();
                    return null;
                }
            }
        }), container2);
    }

    /* access modifiers changed from: private */
    public static String getPropertyNameOfSetter(String str) {
        if (str.startsWith("set")) {
            return str.substring(3);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static String getPropertyNameOfGetter(String str) {
        if (str.startsWith("get")) {
            return str.substring(3);
        }
        if (str.startsWith("is")) {
            return str.substring(2);
        }
        return null;
    }
}
