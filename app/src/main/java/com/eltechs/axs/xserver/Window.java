package com.eltechs.axs.xserver;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.impl.WindowChildrenList;

public interface Window {
    Drawable getActiveBackingStore();

    Rectangle getBoundingRectangle();

    Iterable<Window> getChildrenBottomToTop();

    WindowChildrenList getChildrenList();

    Iterable<Window> getChildrenTopToBottom();

    XClient getCreator();

    WindowListenersList getEventListenersList();

    Drawable getFrontBuffer();

    int getId();

    Window getParent();

    WindowPropertiesManager getPropertiesManager();

    WindowAttributes getWindowAttributes();

    boolean isInputOutput();

    void replaceBackingStores(Drawable drawable, Drawable drawable2);

    void setBoundingRectangle(Rectangle rectangle);

    void setParent(Window window);
}
