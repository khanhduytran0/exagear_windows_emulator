package com.eltechs.axs.environmentService.components;

import com.eltechs.axs.Globals;
import com.eltechs.axs.applicationState.MemsplitConfigurationAware;
import com.eltechs.axs.configuration.UBTLaunchConfiguration;
import com.eltechs.axs.environmentService.EnvironmentComponent;
import com.eltechs.axs.guestApplicationsTracker.GuestApplicationsLifecycleListener;
import com.eltechs.axs.guestApplicationsTracker.GuestApplicationsTracker;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.epoll.UnixSocketConfiguration;
import java.io.IOException;

public class GuestApplicationsTrackerComponent extends EnvironmentComponent {
    private final UnixSocketConfiguration socketConf;
    private GuestApplicationsTracker tracker;

    public GuestApplicationsTrackerComponent(UnixSocketConfiguration unixSocketConfiguration) {
        this.socketConf = unixSocketConfiguration;
    }

    public void start() throws IOException {
        String str;
        Assert.state(this.tracker == null, "Guest applications tracker is already started.");
        NativeLibsConfiguration nativeLibsConfiguration = getEnvironment().getNativeLibsConfiguration();
        if (((MemsplitConfigurationAware) Globals.getApplicationState()).getMemsplitConfiguration().isMemsplit3g()) {
            str = nativeLibsConfiguration.getLibubtPath();
        } else {
            str = nativeLibsConfiguration.getLibubt2GPath();
        }
        this.tracker = new GuestApplicationsTracker(this.socketConf, nativeLibsConfiguration.getElfLoaderPath(), str, nativeLibsConfiguration.getKillswitchPath());
    }

    public void stop() {
        Assert.state(this.tracker != null, "Guest applications tracker is not yet started.");
        this.tracker.killGuestApplications();
        try {
            this.tracker.stop();
        } catch (IOException unused) {
        }
        this.tracker = null;
    }

    public String getSocket() {
        return this.socketConf.getGuestPath();
    }

    public void startGuestApplication(UBTLaunchConfiguration uBTLaunchConfiguration) {
        this.tracker.startGuestApplication(uBTLaunchConfiguration, getEnvironment());
    }

    public void addListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        Assert.state(this.tracker != null, "Guest applications tracker is not yet started.");
        this.tracker.addListener(guestApplicationsLifecycleListener);
    }

    public void removeListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        Assert.state(this.tracker != null, "Guest applications tracker is not yet started.");
        this.tracker.removeListener(guestApplicationsLifecycleListener);
    }

    public void freezeGuestApplications() {
        Assert.state(this.tracker != null, "Guest applications tracker is not yet started.");
        this.tracker.freezeGuestApplications();
    }

    public void resumeGuestApplications() {
        Assert.state(this.tracker != null, "Guest applications tracker is not yet started.");
        this.tracker.resumeGuestApplications();
    }

    public boolean haveGuestApplications() {
        Assert.state(this.tracker != null, "Guest applications tracker is not yet started.");
        return this.tracker.haveGuestApplications();
    }
}
