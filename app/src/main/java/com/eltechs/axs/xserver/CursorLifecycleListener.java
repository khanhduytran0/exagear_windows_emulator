package com.eltechs.axs.xserver;

public interface CursorLifecycleListener {
    void cursorCreated(Cursor cursor);

    void cursorFreed(Cursor cursor);
}
