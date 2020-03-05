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
import com.eltechs.axs.xserver.LocksManager;
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
import java.util.Iterator;
import java.util.Map;

public class XClient {
    private boolean authenticated;

    private final Collection<Colormap> clientColormaps;

    private final Collection<Cursor> clientCursors;

    private final Collection<GraphicsContext> clientGraphicsContexts;

    private final Collection<Pixmap> clientPixmaps;

    private final Collection<ShmSegment> clientShmSegments;

    private final Collection<Window> clientWindows;

    private final ColormapLifecycleListener colormapLifecycleListener;

    private final CursorLifecycleListener cursorLifecycleListener;

    private final GraphicsContextLifecycleListener graphicsContextLifecycleListener;

    private final IdInterval idInterval;

    private final XOutputStream outputStream;

    private final PixmapLifecycleListener pixmapLifecycleListener;

    private int sequenceNumber;

    private final ShmSegmentLifecycleListener shmSegmentLifecycleListener;

    private final WindowLifecycleListener windowLifecycleListener;

    private final Map<Window, XClientWindowListener> windowListeners;

    private final XServer xServer;

    public XClient(XServer paramXServer, XOutputStream paramXOutputStream) {
        this.sequenceNumber = 0;
        this.windowListeners = new HashMap<Window, XClientWindowListener>();
        this.clientWindows = new ArrayList<Window>();
        this.clientPixmaps = new ArrayList<Pixmap>();
        this.clientGraphicsContexts = new ArrayList<GraphicsContext>();
        this.clientCursors = new ArrayList<Cursor>();
        this.clientColormaps = new ArrayList<Colormap>();
        this.clientShmSegments = new ArrayList<ShmSegment>();
        this.outputStream = paramXOutputStream;
        this.xServer = paramXServer;
        LocksManager.XLock xLock = paramXServer.getLocksManager().lockAll();
        paramXOutputStream = null;
        try {
            this.windowLifecycleListener = (WindowLifecycleListener)new WindowLifecycleAdapter() {
                public void windowDestroyed(Window param1Window) {
                    XClient.this.windowListeners.remove(param1Window);
                    XClient.this.clientWindows.remove(param1Window);
                }
            };
            this.xServer.getWindowsManager().addWindowLifecycleListener(this.windowLifecycleListener);
            this.pixmapLifecycleListener = new PixmapLifecycleListener() {
                public void pixmapCreated(Pixmap param1Pixmap) {}

                public void pixmapFreed(Pixmap param1Pixmap) {
                    XClient.this.clientPixmaps.remove(param1Pixmap);
                }
            };
            this.xServer.getPixmapsManager().addPixmapLifecycleListener(this.pixmapLifecycleListener);
            this.cursorLifecycleListener = new CursorLifecycleListener() {
                public void cursorCreated(Cursor param1Cursor) {}

                public void cursorFreed(Cursor param1Cursor) {
                    XClient.this.clientCursors.remove(param1Cursor);
                }
            };
            this.xServer.getCursorsManager().addCursorLifecycleListener(this.cursorLifecycleListener);
            this.graphicsContextLifecycleListener = new GraphicsContextLifecycleListener() {
                public void graphicsContextCreated(GraphicsContext param1GraphicsContext) {}

                public void graphicsContextFreed(GraphicsContext param1GraphicsContext) {
                    XClient.this.clientGraphicsContexts.remove(param1GraphicsContext);
                }
            };
            this.xServer.getGraphicsContextsManager().addGraphicsContextsLifecycleListener(this.graphicsContextLifecycleListener);
            this.colormapLifecycleListener = new ColormapLifecycleListener() {
                public void colormapCreated(Colormap param1Colormap) {}

                public void colormapFreed(Colormap param1Colormap) {
                    XClient.this.clientColormaps.remove(param1Colormap);
                }
            };
            this.xServer.getColormapsManager().addColormapLifecycleListener(this.colormapLifecycleListener);
            this.shmSegmentLifecycleListener = new ShmSegmentLifecycleListener() {
                public void segmentAttached(ShmSegment param1ShmSegment) {}

                public void segmentDetached(ShmSegment param1ShmSegment) {
                    XClient.this.clientShmSegments.remove(param1ShmSegment);
                }
            };
            this.xServer.getShmSegmentsManager().addShmSegmentLifecycleListener(this.shmSegmentLifecycleListener);
            this.idInterval = this.xServer.getIdIntervalsManager().getInterval();
            if (xLock != null)
                xLock.close();
            return;
        } catch (Throwable throwable) {
            try {
                xLock.close();
            } catch (Throwable throwable2) {
                throwable.addSuppressed(throwable2);
            }
            throw new RuntimeException(throwable);
        }
    }

    public XEventSender createEventSender() {
        return new XEventSender(new XResponse(this.sequenceNumber, this.outputStream));
    }

    public void freeAssociatedResources() {
        LocksManager.XLock xLock = this.xServer.getLocksManager().lockAll();
        while (true) {
            Throwable throwable1 = null;
            try {
                if (!this.clientWindows.isEmpty()) {
                    Window window = this.clientWindows.iterator().next();
                    this.xServer.getWindowsManager().destroyWindow(window);
                    continue;
                }
                while (true) {
                    if (!this.clientPixmaps.isEmpty()) {
                        Pixmap pixmap = this.clientPixmaps.iterator().next();
                        this.xServer.getPixmapsManager().freePixmap(pixmap);
                        continue;
                    }
                    break;
                }
                while (true) {
                    if (!this.clientGraphicsContexts.isEmpty()) {
                        GraphicsContext graphicsContext = this.clientGraphicsContexts.iterator().next();
                        this.xServer.getGraphicsContextsManager().removeGraphicsContext(graphicsContext);
                        continue;
                    }
                    break;
                }
                while (true) {
                    if (!this.clientCursors.isEmpty()) {
                        Cursor cursor = this.clientCursors.iterator().next();
                        this.xServer.getCursorsManager().freeCursor(cursor);
                        continue;
                    }
                    break;
                }
                while (true) {
                    if (!this.clientColormaps.isEmpty()) {
                        Colormap colormap = this.clientColormaps.iterator().next();
                        this.xServer.getColormapsManager().freeColormap(colormap);
                        continue;
                    }
                    break;
                }
                while (true) {
                    if (!this.clientShmSegments.isEmpty()) {
                        ShmSegment shmSegment = this.clientShmSegments.iterator().next();
                        this.xServer.getShmSegmentsManager().detachSegment(shmSegment);
                        continue;
                    }
                    Iterator<Map.Entry<Window, XClientWindowListener>> iterator = this.windowListeners.entrySet().iterator();
                    while (true) {
                        if (iterator.hasNext()) {
                            Map.Entry entry = iterator.next();
                            ((Window)entry.getKey()).getEventListenersList().removeListener((WindowListener)entry.getValue());
                            continue;
                        }
                        this.xServer.getWindowsManager().removeWindowLifecycleListener(this.windowLifecycleListener);
                        this.xServer.getPixmapsManager().removePixmapLifecycleListener(this.pixmapLifecycleListener);
                        this.xServer.getGraphicsContextsManager().removeGraphicsContextLifecycleListener(this.graphicsContextLifecycleListener);
                        this.xServer.getCursorsManager().removeCursorLifecycleListener(this.cursorLifecycleListener);
                        this.xServer.getColormapsManager().removeColormapLifecycleListener(this.colormapLifecycleListener);
                        this.xServer.getShmSegmentsManager().removeShmSegmentLifecycleListener(this.shmSegmentLifecycleListener);
                        this.xServer.getIdIntervalsManager().freeInterval(this.idInterval);
                        if (xLock != null)
                            xLock.close();
                        return;
                    }
                    // break;
                }
                // break;
            } catch (Throwable throwable) {
                throwable1 = throwable;
                throw throwable;
            } finally {
                if (xLock != null)
                    if (throwable1 != null) {
                        try {
                            xLock.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }
                        throw new RuntimeException(throwable1);
                    } else {
                        xLock.close();
                    }
            }
        }
    }

    public int generateSequenceNumber() {
        int i = this.sequenceNumber + 1;
        this.sequenceNumber = i;
        return i;
    }

    public Mask<EventName> getEventMask(Window paramWindow) {
        XClientWindowListener xClientWindowListener = this.windowListeners.get(paramWindow);
        return (xClientWindowListener != null) ? xClientWindowListener.getMask() : Mask.emptyMask(EventName.class);
    }

    public IdInterval getIdInterval() {
        return this.idInterval;
    }

    public int getLastSequenceNumber() {
        return this.sequenceNumber;
    }

    public void installEventListener(Window paramWindow, Mask<EventName> paramMask) {
        XClientWindowListener xClientWindowListener = this.windowListeners.get(paramWindow);
        if (xClientWindowListener != null)
            paramWindow.getEventListenersList().removeListener(xClientWindowListener);
        if (!paramMask.isEmpty()) {
            XClientWindowListener xClientWindowListener1 = new XClientWindowListener(this, paramMask);
            this.windowListeners.put(paramWindow, xClientWindowListener1);
            paramWindow.getEventListenersList().addListener(xClientWindowListener1);
        }
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public boolean isInterestedIn(Window paramWindow, EventName paramEventName) {
        XClientWindowListener xClientWindowListener = this.windowListeners.get(paramWindow);
        return (xClientWindowListener != null) ? xClientWindowListener.isInterestedIn(paramEventName) : false;
    }

    public void registerAsOwnerOfColormap(Colormap paramColormap) {
        this.clientColormaps.add(paramColormap);
    }

    public void registerAsOwnerOfCursor(Cursor paramCursor) {
        this.clientCursors.add(paramCursor);
    }

    public void registerAsOwnerOfGraphicsContext(GraphicsContext paramGraphicsContext) {
        this.clientGraphicsContexts.add(paramGraphicsContext);
    }

    public void registerAsOwnerOfPixmap(Pixmap paramPixmap) {
        this.clientPixmaps.add(paramPixmap);
    }

    public void registerAsOwnerOfWindow(Window paramWindow) {
        this.clientWindows.add(paramWindow);
    }

    public void setAuthenticated(boolean paramBoolean) {
        this.authenticated = paramBoolean;
    }
}
