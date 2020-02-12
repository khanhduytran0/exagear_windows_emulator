package com.eltechs.axs.xserver;

import java.util.ArrayList;
import java.util.Collection;

public class CursorLifecycleListenerList {
    private final Collection<CursorLifecycleListener> listeners = new ArrayList();

    public void addListener(CursorLifecycleListener cursorLifecycleListener) {
        this.listeners.add(cursorLifecycleListener);
    }

    public void removeListener(CursorLifecycleListener cursorLifecycleListener) {
        this.listeners.remove(cursorLifecycleListener);
    }

    public void sendCursorCreated(Cursor cursor) {
        for (CursorLifecycleListener cursorCreated : this.listeners) {
            cursorCreated.cursorCreated(cursor);
        }
    }

    public void sendCursorFreed(Cursor cursor) {
        for (CursorLifecycleListener cursorFreed : this.listeners) {
            cursorFreed.cursorFreed(cursor);
        }
    }
}
