package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.WindowProperty.Format;

public interface WindowPropertiesManager {

    public enum PropertyModification {
        REPLACE,
        PREPEND,
        APPEND
    }

    void deleteProperty(Atom atom);

    WindowProperty<?> getProperty(Atom atom);

    <T> WindowProperty<T> getProperty(Atom atom, Format<T> format);

    <T> boolean modifyProperty(Atom atom, Atom atom2, Format<T> format, PropertyModification propertyModification, T t);
}
