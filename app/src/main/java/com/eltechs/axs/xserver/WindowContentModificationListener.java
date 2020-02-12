package com.eltechs.axs.xserver;

public interface WindowContentModificationListener {
    void contentChanged(Window window, int i, int i2, int i3, int i4);

    void frontBufferReplaced(Window window);
}
