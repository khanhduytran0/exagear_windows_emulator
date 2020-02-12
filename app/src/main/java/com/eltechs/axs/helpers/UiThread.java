package com.eltechs.axs.helpers;

import android.os.Handler;
import android.os.Looper;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class UiThread {
    public static boolean post(Runnable runnable) {
        return new Handler(Looper.getMainLooper()).post(runnable);
    }

    public static boolean postDelayed(long j, Runnable runnable) {
        return new Handler(Looper.getMainLooper()).postDelayed(runnable, j);
    }

    public static void check() {
        Assert.state(Thread.currentThread() == Looper.getMainLooper().getThread());
    }

    public static <ListenerType> ListenerType wrap(final ListenerType listenertype) {
        ClassLoader classLoader = listenertype.getClass().getClassLoader();
        Class inferListenerClass = inferListenerClass(listenertype);
        final Handler handler = new Handler();
        check();
        return (ListenerType) Proxy.newProxyInstance(classLoader, new Class[]{inferListenerClass}, new InvocationHandler() {
            public Object invoke(Object obj, final Method method, final Object[] objArr) throws Throwable {
                if (method.getName().equals("equals")) {
                    boolean z = true;
                    if (objArr.length == 1) {
                        if (this != objArr[0]) {
                            z = false;
                        }
                        return Boolean.valueOf(z);
                    }
                }
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            method.invoke(listenertype, objArr);
                        } catch (IllegalAccessException unused) {
                            Assert.unreachable(String.format("Listener method %s is expected to be public; can't get IllegalAccessException when calling it.", new Object[]{method}));
                        } catch (InvocationTargetException e) {
                            Assert.state(false, String.format("Listener method %s has thrown an exception %s.", new Object[]{method, e.getTargetException()}));
                        }
                    }
                });
                return null;
            }
        });
    }

    private static <ListenerType> Class<ListenerType> inferListenerClass(ListenerType listenertype) {
        Class<ListenerType> cls = null;
        for (Class cls2 = listenertype.getClass(); cls2 != null; cls2 = cls2.getSuperclass()) {
            Class<ListenerType>[] interfaces = cls2.getInterfaces();
            if (interfaces.length != 0) {
                Assert.isTrue(cls == null && interfaces.length <= 1, String.format("The class %s is used as a listener and must implement exactly one interface.", new Object[]{listenertype.getClass()}));
                if (interfaces.length == 1) {
                    cls = interfaces[0];
                }
            }
        }
        return cls;
    }
}
