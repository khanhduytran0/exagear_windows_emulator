package com.eltechs.axs.xserver.impl.windowProperties;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.WindowProperty;

public abstract class MutableWindowProperty<T> implements WindowProperty<T> {
    private final Atom type;

    public abstract void appendValues(T t);

    public abstract void prependValues(T t);

    public abstract void replaceValues(T t);

    protected MutableWindowProperty(Atom atom) {
        this.type = atom;
    }

    public Atom getType() {
        return this.type;
    }
}
