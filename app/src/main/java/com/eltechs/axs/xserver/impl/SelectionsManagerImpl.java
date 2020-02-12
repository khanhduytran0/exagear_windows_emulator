package com.eltechs.axs.xserver.impl;

import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowLifecycleAdapter;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.events.SelectionClear;
import com.eltechs.axs.xserver.events.SelectionNotify;
import com.eltechs.axs.xserver.events.SelectionRequest;
import java.util.HashMap;
import java.util.Map;

public class SelectionsManagerImpl {
    /* access modifiers changed from: private */
    public final Map<Atom, SelectionInfo> selections = new HashMap();
    private final WindowLifecycleListener windowDestroyListener = new WindowLifecycleAdapter() {
        public void windowDestroyed(Window window) {
            for (SelectionInfo selectionInfo : SelectionsManagerImpl.this.selections.values()) {
                if (selectionInfo.owner == window) {
                    selectionInfo.owner = null;
                    selectionInfo.client = null;
                }
            }
        }
    };

    private class SelectionInfo {
        public XClient client;
        public int lastChangeTime;
        public Window owner;

        private SelectionInfo() {
        }
    }

    private SelectionInfo getSelectionInfo(Atom atom) {
        SelectionInfo selectionInfo = (SelectionInfo) this.selections.get(atom);
        if (selectionInfo != null) {
            return selectionInfo;
        }
        SelectionInfo selectionInfo2 = new SelectionInfo();
        this.selections.put(atom, selectionInfo2);
        return selectionInfo2;
    }

    public SelectionsManagerImpl(XServer xServer) {
        xServer.getWindowsManager().addWindowLifecycleListener(this.windowDestroyListener);
    }

    public void setSelectionOwner(Atom atom, Window window, XClient xClient, int i) {
        if (i == 0) {
            i = (int) System.currentTimeMillis();
        }
        SelectionInfo selectionInfo = getSelectionInfo(atom);
        if (selectionInfo.owner != null && (window == null || selectionInfo.client != xClient)) {
            selectionInfo.client.createEventSender().sendEvent(new SelectionClear(i, selectionInfo.owner, atom));
        }
        selectionInfo.owner = window;
        selectionInfo.client = xClient;
        selectionInfo.lastChangeTime = i;
    }

    public Window getSelectionOwner(Atom atom) {
        return getSelectionInfo(atom).owner;
    }

    public void convertSelection(Window window, XClient xClient, Atom atom, Atom atom2, Atom atom3, int i) {
        SelectionInfo selectionInfo = getSelectionInfo(atom);
        if (selectionInfo.owner != null) {
            SelectionRequest selectionRequest = new SelectionRequest(i, selectionInfo.owner, window, atom, atom2, atom3);
            selectionInfo.client.createEventSender().sendEvent(selectionRequest);
            return;
        }
        SelectionNotify selectionNotify = new SelectionNotify(i, window, atom, atom2, null);
        xClient.createEventSender().sendEvent(selectionNotify);
    }
}
