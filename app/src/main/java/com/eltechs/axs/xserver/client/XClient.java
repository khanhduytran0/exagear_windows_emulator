package com.eltechs.axs.xserver.client;

import com.eltechs.axs.proto.output.XEventSender;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.Colormap;
import com.eltechs.axs.xserver.ColormapLifecycleListener;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.CursorLifecycleListener;
import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.GraphicsContextLifecycleListener;
import com.eltechs.axs.xserver.IdInterval;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.PixmapLifecycleListener;
import com.eltechs.axs.xserver.ShmSegment;
import com.eltechs.axs.xserver.ShmSegmentLifecycleListener;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowLifecycleAdapter;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import com.eltechs.axs.xserver.WindowListener;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class XClient {
    private boolean authenticated;
    /* access modifiers changed from: private */
    public final Collection<Colormap> clientColormaps = new ArrayList();
    /* access modifiers changed from: private */
    public final Collection<Cursor> clientCursors = new ArrayList();
    /* access modifiers changed from: private */
    public final Collection<GraphicsContext> clientGraphicsContexts = new ArrayList();
    /* access modifiers changed from: private */
    public final Collection<Pixmap> clientPixmaps = new ArrayList();
    /* access modifiers changed from: private */
    public final Collection<ShmSegment> clientShmSegments = new ArrayList();
    /* access modifiers changed from: private */
    public final Collection<Window> clientWindows = new ArrayList();
    private final ColormapLifecycleListener colormapLifecycleListener;
    private final CursorLifecycleListener cursorLifecycleListener;
    private final GraphicsContextLifecycleListener graphicsContextLifecycleListener;
    private final IdInterval idInterval;
    private final XOutputStream outputStream;
    private final PixmapLifecycleListener pixmapLifecycleListener;
    private int sequenceNumber = 0;
    private final ShmSegmentLifecycleListener shmSegmentLifecycleListener;
    private final WindowLifecycleListener windowLifecycleListener;
    /* access modifiers changed from: private */
    public final Map<Window, XClientWindowListener> windowListeners = new HashMap();
    private final XServer xServer;

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x00c8, code lost:
        if (r4 != null) goto L_0x00ca;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x00ce, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x00cf, code lost:
        r4.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x00d3, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x00c2, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x00c6, code lost:
        if (r3 != null) goto L_0x00c8;
     */
    public XClient(XServer xServer2, XOutputStream xOutputStream) {
        this.outputStream = xOutputStream;
        this.xServer = xServer2;
        XLock lockAll = xServer2.getLocksManager().lockAll();
        this.windowLifecycleListener = new WindowLifecycleAdapter() {
            public void windowDestroyed(Window window) {
                XClient.this.windowListeners.remove(window);
                XClient.this.clientWindows.remove(window);
            }
        };
        this.xServer.getWindowsManager().addWindowLifecycleListener(this.windowLifecycleListener);
        this.pixmapLifecycleListener = new PixmapLifecycleListener() {
            public void pixmapCreated(Pixmap pixmap) {
            }

            public void pixmapFreed(Pixmap pixmap) {
                XClient.this.clientPixmaps.remove(pixmap);
            }
        };
        this.xServer.getPixmapsManager().addPixmapLifecycleListener(this.pixmapLifecycleListener);
        this.cursorLifecycleListener = new CursorLifecycleListener() {
            public void cursorCreated(Cursor cursor) {
            }

            public void cursorFreed(Cursor cursor) {
                XClient.this.clientCursors.remove(cursor);
            }
        };
        this.xServer.getCursorsManager().addCursorLifecycleListener(this.cursorLifecycleListener);
        this.graphicsContextLifecycleListener = new GraphicsContextLifecycleListener() {
            public void graphicsContextCreated(GraphicsContext graphicsContext) {
            }

            public void graphicsContextFreed(GraphicsContext graphicsContext) {
                XClient.this.clientGraphicsContexts.remove(graphicsContext);
            }
        };
        this.xServer.getGraphicsContextsManager().addGraphicsContextsLifecycleListener(this.graphicsContextLifecycleListener);
        this.colormapLifecycleListener = new ColormapLifecycleListener() {
            public void colormapCreated(Colormap colormap) {
            }

            public void colormapFreed(Colormap colormap) {
                XClient.this.clientColormaps.remove(colormap);
            }
        };
        this.xServer.getColormapsManager().addColormapLifecycleListener(this.colormapLifecycleListener);
        this.shmSegmentLifecycleListener = new ShmSegmentLifecycleListener() {
            public void segmentAttached(ShmSegment shmSegment) {
            }

            public void segmentDetached(ShmSegment shmSegment) {
                XClient.this.clientShmSegments.remove(shmSegment);
            }
        };
        this.xServer.getShmSegmentsManager().addShmSegmentLifecycleListener(this.shmSegmentLifecycleListener);
        this.idInterval = this.xServer.getIdIntervalsManager().getInterval();
        if (lockAll != null) {
            lockAll.close();
            return;
        }
        return;
        // throw th;
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public void setAuthenticated(boolean z) {
        this.authenticated = z;
    }

    public int getLastSequenceNumber() {
        return this.sequenceNumber;
    }

    public int generateSequenceNumber() {
        int i = this.sequenceNumber + 1;
        this.sequenceNumber = i;
        return i;
    }

    public IdInterval getIdInterval() {
        return this.idInterval;
    }

    public void installEventListener(Window window, Mask<EventName> mask) {
        XClientWindowListener xClientWindowListener = (XClientWindowListener) this.windowListeners.get(window);
        if (xClientWindowListener != null) {
            window.getEventListenersList().removeListener(xClientWindowListener);
        }
        if (!mask.isEmpty()) {
            XClientWindowListener xClientWindowListener2 = new XClientWindowListener(this, mask);
            this.windowListeners.put(window, xClientWindowListener2);
            window.getEventListenersList().addListener(xClientWindowListener2);
        }
    }

    public boolean isInterestedIn(Window window, EventName eventName) {
        XClientWindowListener xClientWindowListener = (XClientWindowListener) this.windowListeners.get(window);
        if (xClientWindowListener != null) {
            return xClientWindowListener.isInterestedIn(eventName);
        }
        return false;
    }

    public Mask<EventName> getEventMask(Window window) {
        XClientWindowListener xClientWindowListener = (XClientWindowListener) this.windowListeners.get(window);
        if (xClientWindowListener != null) {
            return xClientWindowListener.getMask();
        }
        return Mask.emptyMask(EventName.class);
    }

    public XEventSender createEventSender() {
        return new XEventSender(new XResponse(this.sequenceNumber, this.outputStream));
    }

    public void registerAsOwnerOfWindow(Window window) {
        this.clientWindows.add(window);
    }

    public void registerAsOwnerOfPixmap(Pixmap pixmap) {
        this.clientPixmaps.add(pixmap);
    }

    public void registerAsOwnerOfGraphicsContext(GraphicsContext graphicsContext) {
        this.clientGraphicsContexts.add(graphicsContext);
    }

    public void registerAsOwnerOfCursor(Cursor cursor) {
        this.clientCursors.add(cursor);
    }

    public void registerAsOwnerOfColormap(Colormap colormap) {
        this.clientColormaps.add(colormap);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x013c, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0140, code lost:
        if (r0 != null) goto L_0x0142;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0142, code lost:
        if (r1 != null) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0148, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0149, code lost:
        r1.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x014d, code lost:
        r0.close();
     */
    public void freeAssociatedResources() {
        XLock lockAll = this.xServer.getLocksManager().lockAll();
        while (!this.clientWindows.isEmpty()) {
            this.xServer.getWindowsManager().destroyWindow((Window) this.clientWindows.iterator().next());
        }
        while (!this.clientPixmaps.isEmpty()) {
            this.xServer.getPixmapsManager().freePixmap((Pixmap) this.clientPixmaps.iterator().next());
        }
        while (!this.clientGraphicsContexts.isEmpty()) {
            this.xServer.getGraphicsContextsManager().removeGraphicsContext((GraphicsContext) this.clientGraphicsContexts.iterator().next());
        }
        while (!this.clientCursors.isEmpty()) {
            this.xServer.getCursorsManager().freeCursor((Cursor) this.clientCursors.iterator().next());
        }
        while (!this.clientColormaps.isEmpty()) {
            this.xServer.getColormapsManager().freeColormap((Colormap) this.clientColormaps.iterator().next());
        }
        while (!this.clientShmSegments.isEmpty()) {
            this.xServer.getShmSegmentsManager().detachSegment((ShmSegment) this.clientShmSegments.iterator().next());
        }
        for (Entry entry : this.windowListeners.entrySet()) {
            ((Window) entry.getKey()).getEventListenersList().removeListener((WindowListener) entry.getValue());
        }
        this.xServer.getWindowsManager().removeWindowLifecycleListener(this.windowLifecycleListener);
        this.xServer.getPixmapsManager().removePixmapLifecycleListener(this.pixmapLifecycleListener);
        this.xServer.getGraphicsContextsManager().removeGraphicsContextLifecycleListener(this.graphicsContextLifecycleListener);
        this.xServer.getCursorsManager().removeCursorLifecycleListener(this.cursorLifecycleListener);
        this.xServer.getColormapsManager().removeColormapLifecycleListener(this.colormapLifecycleListener);
        this.xServer.getShmSegmentsManager().removeShmSegmentLifecycleListener(this.shmSegmentLifecycleListener);
        this.xServer.getIdIntervalsManager().freeInterval(this.idInterval);
        if (lockAll != null) {
            lockAll.close();
            return;
        }
        return;
        // throw th;
    }
}
