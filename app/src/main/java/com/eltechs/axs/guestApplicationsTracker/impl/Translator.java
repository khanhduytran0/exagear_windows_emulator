package com.eltechs.axs.guestApplicationsTracker.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.helpers.SingleshotTimer;
import java.io.IOException;

public class Translator {
    private static final int TIME_TO_CONNECT = 750;
    private TranslatorConnection connection;
    private final SingleshotTimer connectionTimeoutTracker = new SingleshotTimer();
    private boolean isForking;
    /* access modifiers changed from: private */
    public final GuestApplicationsCollection owner;
    private final int pid;

    public Translator(GuestApplicationsCollection guestApplicationsCollection, int i) {
        this.owner = guestApplicationsCollection;
        this.pid = i;
        scheduleDestructionIfNoConnectionIsMade();
    }

    public int getPid() {
        return this.pid;
    }

    public void connectionEstablished(TranslatorConnection translatorConnection) {
        this.connectionTimeoutTracker.cancel();
        this.connection = translatorConnection;
        translatorConnection.associate(this);
    }

    public void connectionLost(TranslatorConnection translatorConnection) {
        Assert.state(translatorConnection != null);
        if (this.connection == translatorConnection) {
            this.connection = null;
            scheduleDestructionIfNoConnectionIsMade();
        }
    }

    private void scheduleDestructionIfNoConnectionIsMade() {
        this.connectionTimeoutTracker.schedule(TIME_TO_CONNECT, new Runnable() {
            public void run() {
                Translator.this.owner.killTranslator(Translator.this);
            }
        });
    }

    public void forkRequested() {
        synchronized (this.owner) {
            sendEmptyPacket();
            this.isForking = true;
        }
    }

    public void forkDone(int i) {
        synchronized (this.owner) {
            if (i > 0) {
                try {
                    this.owner.registerTranslator(i);
                } catch (Throwable th) {
                    throw new RuntimeException(th);
                }
            }
            sendEmptyPacket();
            this.isForking = false;
        }
    }

    public boolean isForking() {
        return this.isForking;
    }

    public void sendEmptyPacket() {
        try {
            this.connection.sendEmptyPacket();
        } catch (IOException unused) {
            this.owner.killTranslator(this);
        }
    }

    public String toString() {
        return String.format("ubt[%d]", new Object[]{Integer.valueOf(this.pid)});
    }
}
