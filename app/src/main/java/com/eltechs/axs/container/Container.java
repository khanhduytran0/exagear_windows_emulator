package com.eltechs.axs.container;

import com.eltechs.axs.container.impl.AutowiredPropertiesScanner;
import com.eltechs.axs.container.impl.AutowiringRequest;
import com.eltechs.axs.container.impl.LifecycleHandlerMethod;
import com.eltechs.axs.container.impl.LifecycleHandlersScanner;
import com.eltechs.axs.helpers.Assert;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Container {
    private final Map<Class, List<AutowiringRequest>> autowiringInformation = new HashMap();
    private final Map<String, Object> components = Collections.synchronizedMap(new LinkedHashMap());
    private boolean iterating;
    private final Thread mainThread = Thread.currentThread();
    private final Map<Class, List<LifecycleHandlerMethod>> postAddActions = new HashMap();
    private final Map<Class, List<LifecycleHandlerMethod>> preRemoveActions = new HashMap();

    public void addComponent(String str, Object obj) {
        Assert.state(Thread.currentThread() == this.mainThread);
        Assert.isFalse(this.components.containsKey(str), String.format("The component '%s' is already present in the container.", new Object[]{str}));
        Assert.state(!this.iterating);
        this.components.put(str, obj);
        updateNewComponentConfiguration(obj);
        callPostAdd(obj);
        updateExistingComponentsConfigurationUponAddition(obj);
    }

    public void removeComponent(String str) {
        Assert.state(Thread.currentThread() == this.mainThread);
        Object remove = this.components.remove(str);
        Assert.notNull(remove, String.format("The component '%s' is not present in the container.", new Object[]{str}));
        Assert.state(!this.iterating);
        callPreRemove(remove);
        updateExistingComponentsConfigurationUponRemoval(remove);
    }

    public void setComponent(String str, Object obj) {
        Assert.state(Thread.currentThread() == this.mainThread);
        Assert.state(!this.iterating);
        if (this.components.containsKey(str)) {
            removeComponent(str);
        }
        if (obj != null) {
            addComponent(str, obj);
        }
    }

    public void removeAllComponents() {
        Assert.state(Thread.currentThread() == this.mainThread);
        LinkedList linkedList = new LinkedList();
        for (Entry key : this.components.entrySet()) {
            linkedList.addFirst(key.getKey());
        }
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            removeComponent((String) it.next());
        }
    }

    public Object getComponent(String str) {
        return this.components.get(str);
    }

    private void updateNewComponentConfiguration(Object obj) {
        Assert.state(!this.iterating);
        this.iterating = true;
        for (Entry value : this.components.entrySet()) {
            Object value2 = value.getValue();
            callSetter(obj, value2.getClass(), value2);
        }
        Assert.state(this.iterating);
        this.iterating = false;
    }

    private void updateExistingComponentsConfigurationUponAddition(Object obj) {
        Assert.state(!this.iterating);
        this.iterating = true;
        for (Entry value : this.components.entrySet()) {
            callSetter(value.getValue(), obj.getClass(), obj);
        }
        Assert.state(this.iterating);
        this.iterating = false;
    }

    private void updateExistingComponentsConfigurationUponRemoval(Object obj) {
        Assert.state(!this.iterating);
        this.iterating = true;
        for (Entry value : this.components.entrySet()) {
            callSetter(value.getValue(), obj.getClass(), null);
        }
        Assert.state(this.iterating);
        this.iterating = false;
    }

    private void callPostAdd(Object obj) {
        for (LifecycleHandlerMethod apply : getPostAddActions(obj)) {
            apply.apply(obj);
        }
    }

    private void callPreRemove(Object obj) {
        for (LifecycleHandlerMethod apply : getPreRemoveActions(obj)) {
            apply.apply(obj);
        }
    }

    private List<LifecycleHandlerMethod> getPostAddActions(Object obj) {
        Class cls = obj.getClass();
        List<LifecycleHandlerMethod> list = (List) this.postAddActions.get(cls);
        if (list != null) {
            return list;
        }
        List<LifecycleHandlerMethod> listPostAddActions = LifecycleHandlersScanner.listPostAddActions(cls);
        this.postAddActions.put(cls, listPostAddActions);
        return listPostAddActions;
    }

    private List<LifecycleHandlerMethod> getPreRemoveActions(Object obj) {
        Class cls = obj.getClass();
        List<LifecycleHandlerMethod> list = (List) this.preRemoveActions.get(cls);
        if (list != null) {
            return list;
        }
        List<LifecycleHandlerMethod> listPreRemoveActions = LifecycleHandlersScanner.listPreRemoveActions(cls);
        this.preRemoveActions.put(cls, listPreRemoveActions);
        return listPreRemoveActions;
    }

    private void callSetter(Object obj, Class<?> cls, Object obj2) {
        for (AutowiringRequest autowiringRequest : getAutowiringRequestsForComponent(obj)) {
            if (autowiringRequest.isInterestedIn(cls)) {
                autowiringRequest.inject(obj, obj2);
                return;
            }
        }
    }

    private List<AutowiringRequest> getAutowiringRequestsForComponent(Object obj) {
        Class cls = obj.getClass();
        List<AutowiringRequest> list = (List) this.autowiringInformation.get(cls);
        if (list != null) {
            return list;
        }
        List<AutowiringRequest> listAutowiringRequests = AutowiredPropertiesScanner.listAutowiringRequests(cls);
        this.autowiringInformation.put(cls, listAutowiringRequests);
        return listAutowiringRequests;
    }
}
