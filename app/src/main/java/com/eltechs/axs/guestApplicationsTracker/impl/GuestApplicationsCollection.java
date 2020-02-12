package com.eltechs.axs.guestApplicationsTracker.impl;

import com.eltechs.axs.guestApplicationsTracker.GuestApplicationsLifecycleListener;
import java.util.ArrayList;
import java.util.Collection;

public class GuestApplicationsCollection {
    private boolean guestApplicationsAreRunnable = true;
    private final GuestApplicationLifecycleListenersList listeners = new GuestApplicationLifecycleListenersList();
    private final Collection<Translator> translators = new ArrayList();

    public synchronized void addListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        this.listeners.addListener(guestApplicationsLifecycleListener);
    }

    public synchronized void removeListener(GuestApplicationsLifecycleListener guestApplicationsLifecycleListener) {
        this.listeners.removeListener(guestApplicationsLifecycleListener);
    }

    public synchronized Translator registerTranslator(int i) {
        Translator translator;
        translator = getTranslator(i);
        if (translator == null) {
            translator = new Translator(this, i);
            this.translators.add(translator);
            this.listeners.sendTranslatorStarted(translator);
        }
        return translator;
    }

    public synchronized void translatorStarted(int i, TranslatorConnection translatorConnection) {
        Translator registerTranslator = registerTranslator(i);
        registerTranslator.connectionEstablished(translatorConnection);
        registerTranslator.sendEmptyPacket();
    }

    public synchronized void killTranslator(Translator translator) {
        ProcessHelpers.killProcess(translator.getPid());
        if (this.translators.remove(translator)) {
            this.listeners.sendTranslatorExited(translator);
        }
    }

    private Translator getTranslator(int i) {
        for (Translator translator : this.translators) {
            if (translator.getPid() == i) {
                return translator;
            }
        }
        return null;
    }

    public synchronized boolean haveGuestApplications() {
        return !this.translators.isEmpty();
    }

    public void freezeGuestApplications() {
        while (true) {
            synchronized (this) {
                if (canFreeze()) {
                    for (Translator pid : this.translators) {
                        ProcessHelpers.suspendProcess(pid.getPid());
                    }
                    this.guestApplicationsAreRunnable = false;
                    return;
                }
            }
            yield();
        }
		/*
        while (true) {
        }
		*/
    }

    public synchronized void resumeGuestApplications() {
        if (!this.guestApplicationsAreRunnable) {
            for (Translator pid : this.translators) {
                ProcessHelpers.resumeProcess(pid.getPid());
            }
            this.guestApplicationsAreRunnable = true;
        }
    }

    public synchronized void killGuestApplications() {
        ArrayList<Translator> arrayList = new ArrayList<>(this.translators);
        this.translators.clear();
        for (Translator translator : arrayList) {
            ProcessHelpers.killProcess(translator.getPid());
            this.listeners.sendTranslatorExited(translator);
        }
    }

    private boolean canFreeze() {
        for (Translator isForking : this.translators) {
            if (isForking.isForking()) {
                return false;
            }
        }
        return true;
    }

    private void yield() {
        sleep(10);
    }

    private void sleep(int i) {
        try {
            Thread.sleep((long) i);
        } catch (InterruptedException unused) {
        }
    }
}
