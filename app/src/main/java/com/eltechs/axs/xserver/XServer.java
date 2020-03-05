package com.eltechs.axs.xserver;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.rendering.RenderingEngine;
import com.eltechs.axs.sysvipc.SHMEngine;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.impl.AtomsManagerImpl;
import com.eltechs.axs.xserver.impl.ColormapsManagerImpl;
import com.eltechs.axs.xserver.impl.CursorsManagerImpl;
import com.eltechs.axs.xserver.impl.DrawablesManagerImpl;
import com.eltechs.axs.xserver.impl.EventsInjectorImpl;
import com.eltechs.axs.xserver.impl.FocusManagerImpl;
import com.eltechs.axs.xserver.impl.GrabsManagerImpl;
import com.eltechs.axs.xserver.impl.GraphicsContextsManagerImpl;
import com.eltechs.axs.xserver.impl.IdIntervalsManagerImpl;
import com.eltechs.axs.xserver.impl.KeyboardModelManagerImpl;
import com.eltechs.axs.xserver.impl.LocksManagerImpl;
import com.eltechs.axs.xserver.impl.PixmapsManagerImpl;
import com.eltechs.axs.xserver.impl.PredefinedAtoms;
import com.eltechs.axs.xserver.impl.SelectionsManagerImpl;
import com.eltechs.axs.xserver.impl.ShmSegmentsManagerImpl;
import com.eltechs.axs.xserver.impl.WindowsManagerImpl;
import com.eltechs.axs.xserver.impl.drawables.DrawablesFactory;

public class XServer {
    private final AtomsManager atomsManager;
    private final ColormapsManager colormapsManager;
    private final CursorsManager cursorsManager;
    private DesktopExperience desktopExperience = null;
    private final DrawablesManager drawablesManager;
    private final EventsInjector eventsInjector;
    private final FocusManager focusManager;
    private final GrabsManager grabsManager;
    private final GraphicsContextsManager graphicsContextsManager;
    private final IdIntervalsManager idIntervalsManager;
    private final Keyboard keyboard;
    private final KeyboardEventSender keyboardEventSender;
    private final KeyboardModelManager keyboardModelManager;
    private final LocksManagerImpl locksManager;
    private final PixmapsManager pixmapsManager;
    private final Pointer pointer;
    private final PointerEventSender pointerEventSender;
    private final RenderingEngine renderingEngine;
    private final ScreenInfo screenInfo;
    private final SelectionsManagerImpl selectionsManager;
    private final ShmSegmentsManagerImpl shmSegmentsManager;
    private final WindowsManager windowsManager;
// NEVER USED:
    public interface Lock extends AutoCloseable {
    }

    public XServer(ScreenInfo screenInfo2, KeyboardModel keyboardModel, DrawablesFactory drawablesFactory, SHMEngine sHMEngine, RenderingEngine renderingEngine2, int i) {
        // Throwable th;
        this.screenInfo = screenInfo2;
        this.renderingEngine = renderingEngine2;
        this.locksManager = new LocksManagerImpl();
        this.atomsManager = new AtomsManagerImpl();
        PredefinedAtoms.configurePredefinedAtoms(this.atomsManager);
        this.drawablesManager = new DrawablesManagerImpl(drawablesFactory);
        this.graphicsContextsManager = new GraphicsContextsManagerImpl();
        this.windowsManager = new WindowsManagerImpl(screenInfo2, this.drawablesManager);
        this.pixmapsManager = new PixmapsManagerImpl(this.drawablesManager);
        this.cursorsManager = new CursorsManagerImpl(drawablesFactory);
        this.colormapsManager = new ColormapsManagerImpl();
        this.shmSegmentsManager = new ShmSegmentsManagerImpl(sHMEngine);
        this.keyboardModelManager = new KeyboardModelManagerImpl(keyboardModel);
        this.focusManager = new FocusManagerImpl(null, this);
        this.eventsInjector = new EventsInjectorImpl(this);
        this.pointer = new Pointer(this);
        this.keyboard = new Keyboard(this);
        this.idIntervalsManager = new IdIntervalsManagerImpl(i);
        XLock lock = this.locksManager.lock(new Subsystem[]{Subsystem.WINDOWS_MANAGER, Subsystem.INPUT_DEVICES});
        try {
            this.grabsManager = new GrabsManagerImpl(this);
            this.pointerEventSender = new PointerEventSender(this);
            this.keyboardEventSender = new KeyboardEventSender(this);
            this.selectionsManager = new SelectionsManagerImpl(this);
            if (lock != null) {
                lock.close();
            }
        } catch (Throwable th2) {
            // th.addSuppressed(th2);
			throw new RuntimeException(th2);
        }
        // throw th;
    }

    public ScreenInfo getScreenInfo() {
        return this.screenInfo;
    }

    public AtomsManager getAtomsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.ATOMS_MANAGER), "Access to the atoms manager must be protected with a lock of type ATOMS_MANAGER.");
        return this.atomsManager;
    }

    public DrawablesManager getDrawablesManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.DRAWABLES_MANAGER), "Access to the drawables manager must be protected with a lock of type DRAWABLES_MANAGER.");
        return this.drawablesManager;
    }

    public GraphicsContextsManager getGraphicsContextsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.GRAPHICS_CONTEXTS_MANAGER), "Access to the graphics contexts manager must be protected with a lock of type GRAPHICS_CONTEXTS_MANAGER.");
        return this.graphicsContextsManager;
    }

    public WindowsManager getWindowsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.WINDOWS_MANAGER), "Access to the windows manager must be protected with a lock of type WINDOWS_MANAGER.");
        return this.windowsManager;
    }

    public PixmapsManager getPixmapsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.PIXMAPS_MANAGER), "Access to the pixmaps manager must be protected with a lock of type PIXMAPS_MANAGER.");
        return this.pixmapsManager;
    }

    public CursorsManager getCursorsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.CURSORS_MANAGER), "Access to the cursors manager must be protected with a lock of type CURSORS_MANAGER.");
        return this.cursorsManager;
    }

    public KeyboardModelManager getKeyboardModelManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.KEYBOARD_MODEL_MANAGER), "Access to the keyboard model manager must be protected with a lock of type KEYBOARD_MODEL_MANAGER.");
        return this.keyboardModelManager;
    }

    public FocusManager getFocusManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.FOCUS_MANAGER), "Access to the focus manager must be protected with a lock of type FOCUS_MANAGER.");
        return this.focusManager;
    }

    public EventsInjector getEventsInjector() {
        return this.eventsInjector;
    }

    public PointerEventSender getPointerEventSender() {
        return this.pointerEventSender;
    }

    public KeyboardEventSender getKeyboardEventSender() {
        return this.keyboardEventSender;
    }

    public GrabsManager getGrabsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.INPUT_DEVICES), "Access to the grabs must be protected with a lock of type INPUT_DEVICES");
        return this.grabsManager;
    }

    public Pointer getPointer() {
        Assert.state(this.locksManager.isLocked(Subsystem.INPUT_DEVICES), "Access to the pointer must be protected with a lock of type INPUT_DEVICES");
        return this.pointer;
    }

    public Keyboard getKeyboard() {
        Assert.state(this.locksManager.isLocked(Subsystem.INPUT_DEVICES), "Access to the keyboard must be protected with a lock of type INPUT_DEVICES");
        return this.keyboard;
    }

    public IdIntervalsManager getIdIntervalsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.ID_INTERVALS_MANAGER), "Access to the id intervals manager must be protected with a lock of type ID_INTERVALS_MANAGER.");
        return this.idIntervalsManager;
    }

    public ColormapsManager getColormapsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.COLORMAPS_MANAGER), "Access to the colormaps manager must be protected with a lock of type COLORMAPS_MANAGER.");
        return this.colormapsManager;
    }

    public ShmSegmentsManager getShmSegmentsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.SHM_SEGMENTS_MANAGER), "Access to the shared memory segments manager must be protected with a lock of type SHM_SEGMENTS_MANAGER.");
        return this.shmSegmentsManager;
    }

    public SelectionsManagerImpl getSelectionsManager() {
        Assert.state(this.locksManager.isLocked(Subsystem.SELECTIONS_MANAGER), "Access to the selections manager must be protected with a lock of type SELECTIONS_MANAGER.");
        return this.selectionsManager;
    }

    public RenderingEngine getRenderingEngine() {
        Assert.state(this.locksManager.isLocked(Subsystem.RENDERING_ENGINE), "Access to the rendering engine must be protected with a lock of type RENDERING_ENGINE.");
        return this.renderingEngine;
    }

    public LocksManager getLocksManager() {
        return this.locksManager;
    }

    public boolean isShmAvailable() {
        return this.shmSegmentsManager.getShmEngine() != null;
    }

    public boolean isHWRenderingAvailable() {
        return this.renderingEngine != null && this.renderingEngine.isRenderingAvailable();
    }

    public void desktopExperienceAttached(DesktopExperience desktopExperience2) {
        Assert.state(this.locksManager.isLocked(Subsystem.DESKTOP_EXPERIENCE), "Access to the desktop experience plugin must be protected with a lock of type DESKTOP_EXPERIENCE.");
        Assert.state(this.desktopExperience == null, "Can have no more that 1 desktop experience module attached.");
        this.desktopExperience = desktopExperience2;
    }

    public void desktopExperienceDetached(DesktopExperience desktopExperience2) {
        Assert.state(this.locksManager.isLocked(Subsystem.DESKTOP_EXPERIENCE), "Access to the desktop experience plugin must be protected with a lock of type DESKTOP_EXPERIENCE.");
        Assert.isTrue(desktopExperience2 == desktopExperience2);
        this.desktopExperience = null;
    }
}
