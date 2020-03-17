package com.eltechs.axs.guestApplicationsTracker.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.XOutputStream;
import com.eltechs.axs.xconnectors.XStreamLock;
import java.io.IOException;

public class TranslatorConnection {
    private final XOutputStream outputStream;
    private Translator translator;

    public TranslatorConnection(XOutputStream xOutputStream) {
        this.outputStream = xOutputStream;
    }

    public void associate(Translator translator2) {
        Assert.state(this.translator == null, "Already associated with a translator.");
        this.translator = translator2;
    }

    public void processShutdown() {
        if (this.translator != null) {
            this.translator.connectionLost(this);
            this.translator = null;
        }
    }

    public Translator getTranslator() {
        Assert.state(this.translator != null, "Must be associated with a translator.");
        return this.translator;
    }

    public boolean isAssociatedWithTranslator() {
        return this.translator != null;
    }

    public void sendEmptyPacket() throws IOException {
        XStreamLock lock = this.outputStream.lock();
        try {
            this.outputStream.writeInt(TranslatorRequestsDispatcher.MAGIC);
            this.outputStream.writeShort((short) 6);
        } finally {
            lock.close();
        }
    }
}
