package com.eltechs.axs.xserver;

import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.util.List;

public interface WindowsManager {
    void addWindowChangeListener(WindowChangeListener windowChangeListener);

    void addWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener);

    void addWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener);

    void changeRelativeWindowGeometry(Window window, int i, int i2, int i3, int i4);

    void changeWindowZOrder(Window window, Window window2, StackMode stackMode);

    Window createWindow(int i, Window window, int i2, int i3, int i4, int i5, Visual visual, boolean z, XClient xClient);

    void destroySubwindows(Window window);

    void destroyWindow(Window window);

    List<PlacedDrawable> getDrawablesForOutput();

    Window getRootWindow();

    Window getWindow(int i);

    void mapSubwindows(Window window);

    void mapWindow(Window window);

    void removeWindowChangeListener(WindowChangeListener windowChangeListener);

    void removeWindowContentModificationListner(WindowContentModificationListener windowContentModificationListener);

    void removeWindowLifecycleListener(WindowLifecycleListener windowLifecycleListener);

    void unmapSubwindows(Window window);

    void unmapWindow(Window window);
}
