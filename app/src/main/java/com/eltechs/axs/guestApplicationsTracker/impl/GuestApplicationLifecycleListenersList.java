package com.eltechs.axs.guestApplicationsTracker.impl;

import com.eltechs.axs.guestApplicationsTracker.GuestApplicationsLifecycleListener;
import java.util.ArrayList;
import java.util.List;

public class GuestApplicationLifecycleListenersList {
    private final List<GuestApplicationsLifecycleListener> listeners = new ArrayList();

    public void addListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        this.listeners.add(guestApplicationsLifecycleListener);
    }

    public void removeListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        this.listeners.remove(guestApplicationsLifecycleListener);
    }

    public void sendTranslatorStarted(Translator translator) {
        for (GuestApplicationsLifecycleListener translatorStarted : this.listeners) {
            translatorStarted.translatorStarted(translator);
        }
    }

    public void sendTranslatorExited(Translator translator) {
        for (GuestApplicationsLifecycleListener translatorExited : this.listeners) {
            translatorExited.translatorExited(translator);
        }
    }
}
