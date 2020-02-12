package com.eltechs.axs.environmentService;

import android.content.Context;
import android.content.Intent;
import com.eltechs.axs.Globals;
import com.eltechs.axs.applicationState.EnvironmentAware;
import com.eltechs.axs.environmentService.components.DirectSoundServerComponent;
import com.eltechs.axs.environmentService.components.GuestApplicationsTrackerComponent;
import com.eltechs.axs.environmentService.components.NativeLibsConfiguration;
import com.eltechs.axs.helpers.Assert;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AXSEnvironment implements Iterable<EnvironmentComponent> {
    private final Context applicationContext;
    private final List<EnvironmentComponent> components = new ArrayList<EnvironmentComponent>();
    StartupCallback startupCallback = null;
    TrayConfiguration trayConfiguration = null;

    public interface StartupCallback {
        void serviceFailed(Throwable th);

        void serviceStarted();
    }

    public AXSEnvironment(Context context) {
        this.applicationContext = context;
        addComponent(new NativeLibsConfiguration(context));
    }

    public void addComponent(EnvironmentComponent environmentComponent) {
        boolean z = false;
        Assert.state(getComponent(environmentComponent.getClass()) == null, String.format("A component of type '%s' is already registered within the guest environment.", new Object[]{environmentComponent.getClass().getName()}));
        if (getService() == null) {
            z = true;
        }
        Assert.state(z, "It seems useless to add component after service has been already started.");
        this.components.add(environmentComponent);
        environmentComponent.addedToEnvironment(this);
    }

    public <T extends EnvironmentComponent> T getComponent(Class<T> cls) {
        Iterator it = this.components.iterator();
        while (it.hasNext()) {
            T t = (T) it.next();
            if (t.getClass() == cls) {
                return t;
            }
        }
        return null;
    }

    public NativeLibsConfiguration getNativeLibsConfiguration() {
        return (NativeLibsConfiguration) getComponent(NativeLibsConfiguration.class);
    }

    public Iterator<EnvironmentComponent> iterator() {
        return this.components.iterator();
    }

    public void startEnvironmentService(StartupCallback startupCallback2, TrayConfiguration trayConfiguration2) {
        this.startupCallback = startupCallback2;
        this.trayConfiguration = trayConfiguration2;
        this.applicationContext.startService(new Intent(this.applicationContext, AXSEnvironmentService.class));
    }

    private AXSEnvironmentService getService() {
        return ((EnvironmentAware) Globals.getApplicationState()).getEnvironmentServiceInstance();
    }

    public void freezeEnvironment() {
        Assert.state(getService() != null);
        GuestApplicationsTrackerComponent guestApplicationsTrackerComponent = (GuestApplicationsTrackerComponent) getComponent(GuestApplicationsTrackerComponent.class);
        DirectSoundServerComponent directSoundServerComponent = (DirectSoundServerComponent) getComponent(DirectSoundServerComponent.class);
        if (directSoundServerComponent != null) {
            directSoundServerComponent.suspendPlayback();
        }
        if (guestApplicationsTrackerComponent != null) {
            guestApplicationsTrackerComponent.freezeGuestApplications();
        }
    }

    public void resumeEnvironment() {
        Assert.state(getService() != null);
        GuestApplicationsTrackerComponent guestApplicationsTrackerComponent = (GuestApplicationsTrackerComponent) getComponent(GuestApplicationsTrackerComponent.class);
        DirectSoundServerComponent directSoundServerComponent = (DirectSoundServerComponent) getComponent(DirectSoundServerComponent.class);
        if (guestApplicationsTrackerComponent != null) {
            guestApplicationsTrackerComponent.resumeGuestApplications();
        }
        if (directSoundServerComponent != null) {
            directSoundServerComponent.resumePlayback();
        }
    }
}
