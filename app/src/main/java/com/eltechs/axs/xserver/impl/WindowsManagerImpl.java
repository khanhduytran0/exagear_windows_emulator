package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.DrawablesManager;
import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.PlacedDrawable;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.axs.xserver.StackMode;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowAttributes;
import com.eltechs.axs.xserver.WindowChangeListener;
import com.eltechs.axs.xserver.WindowChangeListenersList;
import com.eltechs.axs.xserver.WindowContentModificationListener;
import com.eltechs.axs.xserver.WindowContentModificationListenersList;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import com.eltechs.axs.xserver.WindowLifecycleListenersList;
import com.eltechs.axs.xserver.WindowListenersList;
import com.eltechs.axs.xserver.WindowsManager;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.events.DestroyNotify;
import com.eltechs.axs.xserver.events.Expose;
import com.eltechs.axs.xserver.events.MapNotify;
import com.eltechs.axs.xserver.events.MapRequest;
import com.eltechs.axs.xserver.events.ResizeRequest;
import com.eltechs.axs.xserver.events.UnmapNotify;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowsManagerImpl implements WindowsManager {
    private final DrawablesManager drawablesManager;
    private final Window rootWindow;
    private final WindowChangeListenersList windowChangeListenersList;
    private final WindowContentModificationListenersList windowContentModificationListenersList;
    private final WindowLifecycleListenersList windowLifecycleListenersList;
    private final Map<Integer, Window> windows = new HashMap();

    public WindowsManagerImpl(ScreenInfo screenInfo, DrawablesManager drawablesManager2) {
        this.drawablesManager = drawablesManager2;
        this.windowContentModificationListenersList = new WindowContentModificationListenersList();
        this.windowLifecycleListenersList = new WindowLifecycleListenersList();
        this.windowChangeListenersList = new WindowChangeListenersList();
        int generateId = SmallIdsGenerator.generateId();
        DrawablesManager drawablesManager3 = drawablesManager2;
        int i = generateId;
        Drawable createDrawable = drawablesManager3.createDrawable(i, (Window) null, screenInfo.widthInPixels, screenInfo.heightInPixels, drawablesManager2.getPreferredVisual());
        WindowImpl windowImpl = new WindowImpl(i, createDrawable, null, this.windowContentModificationListenersList, this.windowChangeListenersList, null);
        this.rootWindow = windowImpl;
        this.rootWindow.setBoundingRectangle(new Rectangle(0, 0, screenInfo.widthInPixels, screenInfo.heightInPixels));
        this.rootWindow.getWindowAttributes().setMapped(true);
        this.windows.put(Integer.valueOf(createDrawable.getId()), this.rootWindow);
    }

    public Window getRootWindow() {
        return this.rootWindow;
    }

    public Window getWindow(int i) {
        return (Window) this.windows.get(Integer.valueOf(i));
    }

    public Window createWindow(int i, Window window, int i2, int i3, int i4, int i5, Visual visual, boolean z, XClient xClient) {
        Drawable drawable;
        if (this.windows.containsKey(Integer.valueOf(i))) {
            return null;
        }
        if (!z) {
            drawable = null;
        } else {
            Drawable createDrawable = this.drawablesManager.createDrawable(i, WindowHelpers.getRootWindowOf(window), i4, i5, visual);
            if (createDrawable == null) {
                return null;
            }
            drawable = createDrawable;
        }
        WindowImpl windowImpl = new WindowImpl(i, drawable, null, this.windowContentModificationListenersList, this.windowChangeListenersList, xClient);
        this.windows.put(Integer.valueOf(i), windowImpl);
        window.getChildrenList().add(windowImpl);
        windowImpl.setBoundingRectangle(new Rectangle(i2, i3, i4, i5));
        this.windowLifecycleListenersList.sendWindowCreated(windowImpl);
        return windowImpl;
    }

    public void destroyWindow(Window window) {
        if (getRootWindow().getId() != window.getId()) {
            unmapWindow(window);
            deleteSubtreeAndWindow(window);
        }
    }

    public void destroySubwindows(Window window) {
        unmapSubwindows(window);
        for (Window deleteSubtreeAndWindow : window.getChildrenBottomToTop()) {
            deleteSubtreeAndWindow(deleteSubtreeAndWindow);
        }
    }

    private void deleteSubtreeAndWindow(Window window) {
        Assert.isFalse(getRootWindow().getId() == window.getId(), "Root window can't be destroyed.");
        ArrayList<Window> arrayList = new ArrayList<>();
        for (Window add : window.getChildrenBottomToTop()) {
            arrayList.add(add);
        }
        for (Window deleteSubtreeAndWindow : arrayList) {
            deleteSubtreeAndWindow(deleteSubtreeAndWindow);
        }
        Window parent = window.getParent();
        WindowListenersList eventListenersList = window.getEventListenersList();
        WindowListenersList eventListenersList2 = parent.getEventListenersList();
        eventListenersList.sendEventForEventName(new DestroyNotify(window, window), EventName.STRUCTURE_NOTIFY);
        eventListenersList2.sendEventForEventName(new DestroyNotify(parent, window), EventName.SUBSTRUCTURE_NOTIFY);
        this.windows.remove(Integer.valueOf(window.getId()));
        if (window.isInputOutput()) {
            this.drawablesManager.removeDrawable(window.getFrontBuffer());
        }
        this.windowLifecycleListenersList.sendWindowDestroyed(window);
        parent.getChildrenList().remove(window);
    }

    public void mapWindow(Window window) {
        WindowAttributes windowAttributes = window.getWindowAttributes();
        if (!windowAttributes.isMapped()) {
            Window parent = window.getParent();
            if (!parent.getEventListenersList().isListenerInstalledForEvent(EventName.SUBSTRUCTURE_REDIRECT) || window.getWindowAttributes().isOverrideRedirect()) {
                WindowListenersList eventListenersList = window.getEventListenersList();
                WindowListenersList eventListenersList2 = parent.getEventListenersList();
                windowAttributes.setMapped(true);
                eventListenersList.sendEventForEventName(new MapNotify(window, window), EventName.STRUCTURE_NOTIFY);
                eventListenersList2.sendEventForEventName(new MapNotify(parent, window), EventName.SUBSTRUCTURE_NOTIFY);
                eventListenersList.sendEventForEventName(new Expose(window), EventName.EXPOSURE);
                this.windowLifecycleListenersList.sendWindowMapped(window);
                return;
            }
            parent.getEventListenersList().sendEventForEventName(new MapRequest(window.getParent(), window), EventName.SUBSTRUCTURE_REDIRECT);
        }
    }

    public void mapSubwindows(Window window) {
        for (Window mapWindow : window.getChildrenTopToBottom()) {
            mapWindow(mapWindow);
        }
    }

    public void unmapWindow(Window window) {
        WindowAttributes windowAttributes = window.getWindowAttributes();
        if (getRootWindow().getId() != window.getId() && windowAttributes.isMapped()) {
            windowAttributes.setMapped(false);
            Window parent = window.getParent();
            WindowListenersList eventListenersList = window.getEventListenersList();
            WindowListenersList eventListenersList2 = parent.getEventListenersList();
            eventListenersList.sendEventForEventName(new UnmapNotify(window, window, false), EventName.STRUCTURE_NOTIFY);
            eventListenersList2.sendEventForEventName(new UnmapNotify(parent, window, false), EventName.SUBSTRUCTURE_NOTIFY);
            this.windowLifecycleListenersList.sendWindowUnmapped(window);
        }
    }

    public void unmapSubwindows(Window window) {
        for (Window unmapWindow : window.getChildrenBottomToTop()) {
            unmapWindow(unmapWindow);
        }
    }

    public void changeWindowZOrder(Window window, Window window2, StackMode stackMode) {
        switch (stackMode) {
            case ABOVE:
                window.getParent().getChildrenList().moveAbove(window, window2);
                break;
            case BELOW:
                window.getParent().getChildrenList().moveBelow(window, window2);
                break;
        }
        this.windowLifecycleListenersList.sendWindowZOrderChange(window);
    }

    public void changeRelativeWindowGeometry(Window window, int i, int i2, int i3, int i4) {
        if (window.getParent() != null) {
            Rectangle boundingRectangle = window.getBoundingRectangle();
            WindowAttributes windowAttributes = window.getWindowAttributes();
            boolean z = (boundingRectangle.width == i3 && boundingRectangle.height == i4) ? false : true;
            if (window.getEventListenersList().isListenerInstalledForEvent(EventName.RESIZE_REDIRECT) && z) {
                window.getEventListenersList().sendEventForEventName(new ResizeRequest(window, i3, i4), EventName.SUBSTRUCTURE_REDIRECT);
                i3 = boundingRectangle.width;
                i4 = boundingRectangle.height;
                z = false;
            }
            if (z && window.isInputOutput()) {
                Drawable frontBuffer = window.getFrontBuffer();
                this.drawablesManager.removeDrawable(frontBuffer);
                window.replaceBackingStores(this.drawablesManager.createDrawable(frontBuffer.getId(), frontBuffer.getRoot(), i3, i4, frontBuffer.getVisual()), null);
            }
            if (!(!z && i == boundingRectangle.x && i2 == boundingRectangle.y)) {
                window.setBoundingRectangle(new Rectangle(i, i2, i3, i4));
                this.windowChangeListenersList.sendWindowGeometryChanged(window);
            }
            if (z && window.isInputOutput() && windowAttributes.isMapped()) {
                window.getEventListenersList().sendEvent(new Expose(window));
            }
        }
    }

    public void addWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener) {
        this.windowLifecycleListenersList.addListener(windowLifecycleListener);
    }

    public void removeWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener) {
        this.windowLifecycleListenersList.removeListener(windowLifecycleListener);
    }

    public void addWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener) {
        this.windowContentModificationListenersList.addListener(windowContentModificationListener);
    }

    public void removeWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener) {
        this.windowContentModificationListenersList.removeListener(windowContentModificationListener);
    }

    public void addWindowChangeListener(WindowChangeListener windowChangeListener) {
        this.windowChangeListenersList.addListener(windowChangeListener);
    }

    public void removeWindowChangeListener(WindowChangeListener windowChangeListener) {
        this.windowChangeListenersList.removeListener(windowChangeListener);
    }

    public List<PlacedDrawable> getDrawablesForOutput() {
        ArrayList arrayList = new ArrayList();
        addDrawablesInWindow(this.rootWindow, 0, 0, arrayList);
        return arrayList;
    }

    private void addDrawablesInWindow(Window window, int i, int i2, List<PlacedDrawable> list) {
        if (window.getWindowAttributes().isMapped() && window.isInputOutput()) {
            for (Window window2 : window.getChildrenBottomToTop()) {
                Rectangle boundingRectangle = window2.getBoundingRectangle();
                addDrawablesInWindow(window2, boundingRectangle.x + i, boundingRectangle.y + i2, list);
            }
            if (window != this.rootWindow) {
                Drawable activeBackingStore = window.getActiveBackingStore();
                list.add(new PlacedDrawable(activeBackingStore, new Rectangle(i, i2, activeBackingStore.getWidth(), activeBackingStore.getHeight())));
            }
        }
    }
}
