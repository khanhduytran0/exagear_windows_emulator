package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Drawable.ModificationListener;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowAttributes;
import com.eltechs.axs.xserver.WindowAttributes.WindowClass;
import com.eltechs.axs.xserver.WindowChangeListenersList;
import com.eltechs.axs.xserver.WindowContentModificationListenersList;
import com.eltechs.axs.xserver.WindowListenersList;
import com.eltechs.axs.xserver.WindowPropertiesManager;
import com.eltechs.axs.xserver.client.XClient;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class WindowImpl implements Window {
    private final WindowAttributes attributes;
    private Drawable backBuffer;
    private Rectangle boundingRectangle;
    /* access modifiers changed from: private */
    public final WindowChildrenList children;
    /* access modifiers changed from: private */
    public final WindowContentModificationListenersList contentModificationListenersList;
    private final XClient creator;
    private final WindowListenersList eventListenersList;
    private Drawable frontBuffer;
    private final int id;
    private Window parent;
    private final WindowPropertiesManager propertiesManager;

    public WindowImpl(int i, Drawable drawable, Drawable drawable2, WindowContentModificationListenersList windowContentModificationListenersList, WindowChangeListenersList windowChangeListenersList, XClient xClient) {
        this.id = i;
        if (drawable != null) {
            this.frontBuffer = drawable;
            this.backBuffer = drawable2;
            installFrontBufferModificationListener();
        } else {
            Assert.isTrue(drawable2 == null, "Can't create a window with a back buffer only.");
            this.frontBuffer = null;
            this.backBuffer = null;
        }
        this.children = new WindowChildrenList(this);
        this.propertiesManager = new WindowPropertiesManagerImpl(this);
        this.attributes = new WindowAttributes(WindowClass.INPUT_OUTPUT, windowChangeListenersList, this);
        this.eventListenersList = new WindowListenersList(this);
        this.contentModificationListenersList = windowContentModificationListenersList;
        this.creator = xClient;
    }

    public int getId() {
        return this.id;
    }

    public Window getParent() {
        return this.parent;
    }

    public Iterable<Window> getChildrenBottomToTop() {
        return this.children.getChildren();
    }

    public Iterable<Window> getChildrenTopToBottom() {
        return new Iterable<Window>() {
            public Iterator<Window> iterator() {
                List children = WindowImpl.this.children.getChildren();
                final ListIterator listIterator = children.listIterator(children.size());
                return new Iterator<Window>() {
                    public boolean hasNext() {
                        return listIterator.hasPrevious();
                    }

                    public Window next() {
                        return (Window) listIterator.previous();
                    }

                    public void remove() {
                        listIterator.remove();
                    }
                };
            }
        };
    }

    public void setParent(Window window) {
        if (window != null) {
            Assert.state(this.parent == null, String.format("The window %s already has a parent.", new Object[]{this}));
        }
        this.parent = window;
    }

    public boolean isInputOutput() {
        return this.frontBuffer != null;
    }

    public Rectangle getBoundingRectangle() {
        return this.boundingRectangle;
    }

    public void setBoundingRectangle(Rectangle rectangle) {
        this.boundingRectangle = rectangle;
    }

    public WindowPropertiesManager getPropertiesManager() {
        return this.propertiesManager;
    }

    public WindowAttributes getWindowAttributes() {
        return this.attributes;
    }

    public WindowChildrenList getChildrenList() {
        return this.children;
    }

    public WindowListenersList getEventListenersList() {
        return this.eventListenersList;
    }

    public Drawable getActiveBackingStore() {
        return this.frontBuffer;
    }

    public XClient getCreator() {
        return this.creator;
    }

    public Drawable getFrontBuffer() {
        return this.frontBuffer;
    }

    public Drawable getBackBuffer() {
        return this.backBuffer;
    }

    public void replaceBackingStores(Drawable drawable, Drawable drawable2) {
        boolean z = true;
        Assert.state(isInputOutput(), String.format("replaceBackingStores has been called for the window %d which is input-only.", new Object[]{Integer.valueOf(this.id)}));
        Assert.notNull(drawable, "replaceBackingStores() can't be used to turn a window into an input-only one.");
        if (drawable.getVisual() != this.frontBuffer.getVisual()) {
            z = false;
        }
        Assert.isTrue(z, "replaceBackingStores() can't be used to change the image format of a window.");
        this.frontBuffer = drawable;
        this.backBuffer = drawable2;
        installFrontBufferModificationListener();
        this.contentModificationListenersList.sendFrontBufferReplaced(this);
    }

    private void installFrontBufferModificationListener() {
        this.frontBuffer.installModificationListener(new ModificationListener() {
            public void changed(int i, int i2, int i3, int i4) {
                WindowImpl.this.contentModificationListenersList.sendWindowContentChanged(WindowImpl.this, i, i2, i3, i4);
            }
        });
    }
}
