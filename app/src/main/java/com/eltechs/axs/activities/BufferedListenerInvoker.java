package com.eltechs.axs.activities;

import com.eltechs.axs.helpers.Assert;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class BufferedListenerInvoker<ListenerType> {
    /* access modifiers changed from: private */
    public final List<InvocationRequest> enqueuedInvocations = new ArrayList();
    private final ListenerType proxy;
    /* access modifiers changed from: private */
    public ListenerType realListener;

    private static class InvocationRequest {
        private final Object[] arguments;
        private final Method method;

        InvocationRequest(Method method2, Object[] objArr) {
            this.method = method2;
            this.arguments = objArr;
        }

        /* access modifiers changed from: 0000 */
        public void invoke(Object obj) throws InvocationTargetException, IllegalAccessException {
            this.method.invoke(obj, this.arguments);
        }
    }

    public BufferedListenerInvoker(Class<ListenerType> cls) {
        this.proxy = (ListenerType) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() {
            public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                synchronized (BufferedListenerInvoker.this) {
                    if (BufferedListenerInvoker.this.realListener != null) {
                        Object invoke = method.invoke(BufferedListenerInvoker.this.realListener, objArr);
                        return invoke;
                    }
                    BufferedListenerInvoker.this.enqueuedInvocations.add(new InvocationRequest(method, objArr));
                    return null;
                }
            }
        });
    }

    public ListenerType getProxy() {
        return this.proxy;
    }

    public synchronized void clearRealListener() {
        this.realListener = null;
    }

    public synchronized void setRealListener(ListenerType listenertype) {
        this.realListener = listenertype;
        if (listenertype != null) {
            try {
                for (InvocationRequest invoke : this.enqueuedInvocations) {
                    invoke.invoke(listenertype);
                }
            } catch (Exception e) {
                Assert.abort("Listener methods must throw no exceptions.", e);
            }
            this.enqueuedInvocations.clear();
        }
    }
}
