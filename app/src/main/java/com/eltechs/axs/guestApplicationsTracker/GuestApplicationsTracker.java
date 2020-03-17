package com.eltechs.axs.guestApplicationsTracker;

import com.eltechs.axs.configuration.UBTLaunchConfiguration;
import com.eltechs.axs.environmentService.AXSEnvironment;
import com.eltechs.axs.guestApplicationsTracker.impl.GuestApplicationsCollection;
import com.eltechs.axs.guestApplicationsTracker.impl.Killswitch;
import com.eltechs.axs.guestApplicationsTracker.impl.TranslatorConnection;
import com.eltechs.axs.guestApplicationsTracker.impl.TranslatorConnectionHandler;
import com.eltechs.axs.guestApplicationsTracker.impl.TranslatorRequestsDispatcher;
import com.eltechs.axs.xconnectors.epoll.FairEpollConnector;
import com.eltechs.axs.xconnectors.epoll.UnixSocketConfiguration;
import java.io.IOException;

public class GuestApplicationsTracker {
    private final FairEpollConnector<TranslatorConnection> connector;
    private final GuestApplicationsCollection guestApplicationsCollection = new GuestApplicationsCollection();
    private final Killswitch killswitch;
    private final String libubtPath;

    public GuestApplicationsTracker(UnixSocketConfiguration unixSocketConfiguration, String str, String str2, String str3) throws IOException {
        this.connector = FairEpollConnector.listenOnSpecifiedUnixSocket(unixSocketConfiguration, new TranslatorConnectionHandler(this.guestApplicationsCollection), new TranslatorRequestsDispatcher(this.guestApplicationsCollection));
        this.connector.start();
        this.killswitch = new Killswitch(str, str3, str2);
        this.libubtPath = str2;
    }

    public void stop() throws IOException {
        freezeGuestApplications();
        killGuestApplications();
        this.connector.stop();
        this.killswitch.stop();
    }

    public void addListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        this.guestApplicationsCollection.addListener(guestApplicationsLifecycleListener);
    }

    public void removeListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        this.guestApplicationsCollection.removeListener(guestApplicationsLifecycleListener);
    }

    public boolean startGuestApplication(UBTLaunchConfiguration uBTLaunchConfiguration, AXSEnvironment aXSEnvironment) {
        int runUbt = UBT.runUbt(uBTLaunchConfiguration, aXSEnvironment, this.libubtPath);
        if (runUbt < 0) {
            return false;
        }
        this.guestApplicationsCollection.registerTranslator(runUbt);
        return true;
    }

    public void freezeGuestApplications() {
        this.guestApplicationsCollection.freezeGuestApplications();
    }

    public void resumeGuestApplications() {
        this.guestApplicationsCollection.resumeGuestApplications();
    }

    public void killGuestApplications() {
        this.guestApplicationsCollection.killGuestApplications();
    }

    public boolean haveGuestApplications() {
        return this.guestApplicationsCollection.haveGuestApplications();
    }

    public String getLibubtPath() {
        return this.libubtPath;
    }
}
