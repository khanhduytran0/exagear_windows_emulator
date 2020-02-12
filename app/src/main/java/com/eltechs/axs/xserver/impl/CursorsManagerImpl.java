package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.CursorLifecycleListener;
import com.eltechs.axs.xserver.CursorLifecycleListenerList;
import com.eltechs.axs.xserver.CursorsManager;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.impl.drawables.DrawablesFactory;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class CursorsManagerImpl implements CursorsManager {
    private final CursorLifecycleListenerList cursorLifecycleListenersList;
    private final Map<Integer, Cursor> cursors = new HashMap();
    private final DrawablesFactory drawablesFactory;

    public CursorsManagerImpl(DrawablesFactory drawablesFactory2) {
        this.drawablesFactory = drawablesFactory2;
        this.cursorLifecycleListenersList = new CursorLifecycleListenerList();
    }

    public Cursor createCursor(int i, int i2, int i3, Pixmap pixmap, Pixmap pixmap2) {
        Drawable drawable = null;
        if (this.cursors.containsKey(Integer.valueOf(i))) {
            return null;
        }
        if (pixmap2 != null) {
            drawable = pixmap2.getBackingStore();
        }
        Drawable drawable2 = drawable;
        Drawable backingStore = pixmap.getBackingStore();
        CursorImpl cursorImpl = new CursorImpl(i, i2, i3, this.drawablesFactory.create(0, null, backingStore.getWidth(), backingStore.getHeight(), backingStore.getVisual()), backingStore, drawable2);
        this.cursors.put(Integer.valueOf(i), cursorImpl);
        this.cursorLifecycleListenersList.sendCursorCreated(cursorImpl);
        return cursorImpl;
    }

    public Cursor createFakeCursor(int i) {
        if (this.cursors.containsKey(Integer.valueOf(i))) {
            return null;
        }
        Drawable create = this.drawablesFactory.create(0, null, 1, 1, this.drawablesFactory.getPreferredVisualForDepth(1));
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(4);
        allocateDirect.put(new byte[4]);
        allocateDirect.rewind();
        create.getPainter().drawBitmap(0, 0, 1, 1, allocateDirect);
        CursorImpl cursorImpl = new CursorImpl(i, 0, 0, this.drawablesFactory.create(0, null, 1, 1, this.drawablesFactory.getPreferredVisualForDepth(1)), create, create);
        this.cursors.put(Integer.valueOf(i), cursorImpl);
        this.cursorLifecycleListenersList.sendCursorCreated(cursorImpl);
        return cursorImpl;
    }

    public Cursor getCursor(int i) {
        return (Cursor) this.cursors.get(Integer.valueOf(i));
    }

    public void freeCursor(Cursor cursor) {
        this.cursors.remove(Integer.valueOf(cursor.getId()));
        this.cursorLifecycleListenersList.sendCursorFreed(cursor);
    }

    public void recolorCursor(Cursor cursor, int i, int i2, int i3, int i4, int i5, int i6) {
        ((CursorImpl) cursor).recolorCursor(i, i2, i3, i4, i5, i6);
    }

    public void addCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener) {
        this.cursorLifecycleListenersList.addListener(cursorLifecycleListener);
    }

    public void removeCursorLifecycleListener(CursorLifecycleListener cursorLifecycleListener) {
        this.cursorLifecycleListenersList.removeListener(cursorLifecycleListener);
    }
}
