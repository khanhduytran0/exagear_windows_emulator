package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WindowChildrenList {
    private final List<Window> children = new ArrayList<Window>();
    private final Window host;
    private final List<Window> immutableChildren = Collections.unmodifiableList(this.children);

    public WindowChildrenList(Window window) {
        this.host = window;
    }

    public void add(Window window) {
        Assert.state(window.getParent() == null, String.format("The window %s already has a parent.", new Object[]{window}));
        window.setParent(this.host);
        this.children.add(window);
    }

    public void remove(Window window) {
        Assert.state(window.getParent() == this.host, String.format("The window %s is not a child of %s.", new Object[]{window, this.host}));
        window.setParent(null);
        this.children.remove(window);
    }

    public Window getPrevSibling(Window window) {
        Assert.isTrue(this.children.contains(window));
        int indexOf = this.children.indexOf(window);
        if (indexOf == 0) {
            return null;
        }
        return (Window) this.children.get(indexOf - 1);
    }

    public void moveAbove(Window window, Window window2) {
        Assert.isTrue(this.children.contains(window));
        this.children.remove(window);
        if (window2 != null) {
            Assert.isTrue(this.children.contains(window2));
            this.children.add(this.children.indexOf(window2) + 1, window);
            return;
        }
        this.children.add(window);
    }

    public void moveBelow(Window window, Window window2) {
        Assert.isTrue(this.children.contains(window));
        this.children.remove(window);
        if (window2 != null) {
            Assert.isTrue(this.children.contains(window2));
            this.children.add(this.children.indexOf(window2), window);
            return;
        }
        this.children.add(0, window);
    }

    public List<Window> getChildren() {
        return this.immutableChildren;
    }
}
