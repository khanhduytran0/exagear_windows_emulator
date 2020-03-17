package com.eltechs.axs.configuration.startup.actions;

import com.eltechs.axs.AppConfig;
import com.eltechs.axs.applicationState.EnvironmentAware;
import com.eltechs.axs.configuration.startup.StartupActionInfo;
import com.eltechs.axs.environmentService.AXSEnvironment;
import com.eltechs.axs.environmentService.components.GuestApplicationsTrackerComponent;
import com.eltechs.axs.environmentService.components.XServerComponent;
// import com.eltechs.axs.firebase.FAHelper;
import com.eltechs.axs.guestApplicationsTracker.GuestApplicationsLifecycleAdapter;
import com.eltechs.axs.guestApplicationsTracker.GuestApplicationsLifecycleListener;
import com.eltechs.axs.guestApplicationsTracker.impl.Translator;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowContentModificationListener;

public class WaitForXClientConnection<StateClass extends EnvironmentAware> extends AbstractStartupAction<StateClass> {
    private GuestApplicationsLifecycleListener guestApplicationTerminationListener;
    private final boolean hideXServerImage;
    private final String progressFileName;
    private WindowContentModificationListener putImageListener;
    private boolean receivedEvent;

    public WaitForXClientConnection(String str, boolean z) {
        this.progressFileName = str;
        this.hideXServerImage = z;
    }

    public WaitForXClientConnection(String str) {
        this.progressFileName = str;
        this.hideXServerImage = false;
    }

    public WaitForXClientConnection() {
        this.progressFileName = null;
        this.hideXServerImage = false;
    }

    public StartupActionInfo getInfo() {
        return new StartupActionInfo("", this.progressFileName);
    }

    /* JADX INFO: finally extract failed */
    public void execute() {
        AXSEnvironment environment = ((EnvironmentAware) getApplicationState()).getEnvironment();
        XServerComponent xServerComponent = (XServerComponent) environment.getComponent(XServerComponent.class);
        final GuestApplicationsTrackerComponent guestApplicationsTrackerComponent = (GuestApplicationsTrackerComponent) environment.getComponent(GuestApplicationsTrackerComponent.class);
        this.putImageListener = new WindowContentModificationListener() {
            public void frontBufferReplaced(Window window) {
            }

            public void contentChanged(Window window, int i, int i2, int i3, int i4) {
                WaitForXClientConnection.this.startedDrawing();
            }
        };
        this.guestApplicationTerminationListener = new GuestApplicationsLifecycleAdapter() {
            public void translatorExited(Translator translator) {
                if (!guestApplicationsTrackerComponent.haveGuestApplications()) {
                    WaitForXClientConnection.this.guestApplicationsTerminated();
                }
            }
        };
        XLock lock = xServerComponent.getXServer().getLocksManager().lock(Subsystem.WINDOWS_MANAGER);
        try {
            if (!this.hideXServerImage) {
                xServerComponent.getXServer().getWindowsManager().addWindowContentModificationListner(this.putImageListener);
            }
            guestApplicationsTrackerComponent.addListener(this.guestApplicationTerminationListener);
            lock.close();
            if (!guestApplicationsTrackerComponent.haveGuestApplications()) {
                guestApplicationsTerminated();
            }
        } catch (Throwable th) {
            lock.close();
            throw new RuntimeException(th);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void startedDrawing() {
        if (!this.receivedEvent) {
            AppConfig instance = AppConfig.getInstance(getAppContext());
            if (!instance.isXServerFirstConnectDone()) {
                instance.setXServerFirstConnectDone(true);
            }
            instance.setGuestLaunchesCount(instance.getGuestLaunchesCount() + 1);
            this.receivedEvent = true;
            ((GuestApplicationsTrackerComponent) ((EnvironmentAware) getApplicationState()).getEnvironment().getComponent(GuestApplicationsTrackerComponent.class)).freezeGuestApplications();
            sendDone();
            removeListeners();
        }
    }

    /* access modifiers changed from: private */
    public synchronized void guestApplicationsTerminated() {
        if (!this.receivedEvent) {
            this.receivedEvent = true;
            sendError("Guest applications died before showing anything.");
            removeListeners();
        }
    }

    /* JADX INFO: finally extract failed */
    private void removeListeners() {
        AXSEnvironment environment = ((EnvironmentAware) getApplicationState()).getEnvironment();
        XServerComponent xServerComponent = (XServerComponent) environment.getComponent(XServerComponent.class);
        GuestApplicationsTrackerComponent guestApplicationsTrackerComponent = (GuestApplicationsTrackerComponent) environment.getComponent(GuestApplicationsTrackerComponent.class);
        XLock lock = xServerComponent.getXServer().getLocksManager().lock(Subsystem.WINDOWS_MANAGER);
        try {
            xServerComponent.getXServer().getWindowsManager().removeWindowContentModificationListner(this.putImageListener);
            guestApplicationsTrackerComponent.removeListener(this.guestApplicationTerminationListener);
            lock.close();
            this.putImageListener = null;
            this.guestApplicationTerminationListener = null;
        } catch (Throwable th) {
            lock.close();
            throw new RuntimeException(th);
        }
    }
}
