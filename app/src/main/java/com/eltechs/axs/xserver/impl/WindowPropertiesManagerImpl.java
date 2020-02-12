package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowPropertiesManager;
import com.eltechs.axs.xserver.WindowPropertiesManager.PropertyModification;
import com.eltechs.axs.xserver.WindowProperty;
import com.eltechs.axs.xserver.WindowProperty.Format;
import com.eltechs.axs.xserver.events.PropertyNotify;
import com.eltechs.axs.xserver.impl.windowProperties.ArrayOfBytesWindowProperty;
import com.eltechs.axs.xserver.impl.windowProperties.ArrayOfIntsWindowProperty;
import com.eltechs.axs.xserver.impl.windowProperties.ArrayOfShortsWindowProperty;
import com.eltechs.axs.xserver.impl.windowProperties.MutableWindowProperty;
import java.util.HashMap;
import java.util.Map;

public class WindowPropertiesManagerImpl implements WindowPropertiesManager {
    private final Window host;
    private final Map<Atom, MutableWindowProperty<?>> properties = new HashMap();

    public WindowPropertiesManagerImpl(Window window) {
        this.host = window;
    }

    public WindowProperty<?> getProperty(Atom atom) {
        return (WindowProperty) this.properties.get(atom);
    }

    public <T> WindowProperty<T> getProperty(Atom atom, Format<T> format) {
        WindowProperty<T> windowProperty = (WindowProperty) this.properties.get(atom);
        if (windowProperty == null) {
            return null;
        }
        if (windowProperty.getFormat() != format) {
            windowProperty = null;
        }
        return windowProperty;
    }

    public <T> boolean modifyProperty(Atom atom, Atom atom2, Format<T> format, PropertyModification propertyModification, T t) {
        if (!doModifyProperty(atom, atom2, format, propertyModification, t)) {
            return false;
        }
        this.host.getEventListenersList().sendEventForEventName(new PropertyNotify(this.host, atom, (int) System.currentTimeMillis(), false), EventName.PROPERTY_CHANGE);
        return true;
    }

    public <T> boolean doModifyProperty(Atom atom, Atom atom2, Format<T> format, PropertyModification propertyModification, T t) {
        MutableWindowProperty mutableWindowProperty = (MutableWindowProperty) this.properties.get(atom);
        if (mutableWindowProperty == null) {
            this.properties.put(atom, createProperty(format, atom2, t));
            return true;
        } else if (propertyModification == PropertyModification.REPLACE) {
            if (mutableWindowProperty.getFormat() == format) {
                mutableWindowProperty.replaceValues(t);
            } else {
                this.properties.put(atom, createProperty(format, atom2, t));
            }
            return true;
        } else if (mutableWindowProperty.getFormat() != format) {
            return false;
        } else {
            if (propertyModification == PropertyModification.PREPEND) {
                mutableWindowProperty.prependValues(t);
            } else if (propertyModification == PropertyModification.APPEND) {
                mutableWindowProperty.appendValues(t);
            } else {
                Assert.state(false, String.format("Unsupported PropertyModification %s.", new Object[]{propertyModification}));
            }
            return true;
        }
    }

    public void deleteProperty(Atom atom) {
        if (this.properties.remove(atom) != null) {
            this.host.getEventListenersList().sendEventForEventName(new PropertyNotify(this.host, atom, (int) System.currentTimeMillis(), true), EventName.PROPERTY_CHANGE);
        }
    }

    private <T> MutableWindowProperty<T> createProperty(Format<T> format, Atom atom, T t) {
        if (format == WindowProperty.ARRAY_OF_BYTES) {
            return (MutableWindowProperty) new ArrayOfBytesWindowProperty(atom, (byte[]) t);
        }
        if (format == WindowProperty.ARRAY_OF_SHORTS) {
            return (MutableWindowProperty) new ArrayOfShortsWindowProperty(atom, (short[]) t);
        }
        if (format == WindowProperty.ARRAY_OF_INTS) {
            return (MutableWindowProperty) new ArrayOfIntsWindowProperty(atom, (int[]) t);
        }
        Assert.state(false, String.format("Invalid property format marker %s.", new Object[]{format}));
        return null;
    }
}
