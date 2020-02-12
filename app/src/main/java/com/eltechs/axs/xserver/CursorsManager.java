package com.eltechs.axs.xserver;

public interface CursorsManager {
    void addCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener);

    Cursor createCursor(int i, int i2, int i3, Pixmap pixmap, Pixmap pixmap2);

    Cursor createFakeCursor(int i);

    void freeCursor(Cursor cursor);

    Cursor getCursor(int i);

    void recolorCursor(Cursor cursor, int i, int i2, int i3, int i4, int i5, int i6);

    void removeCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener);
}
