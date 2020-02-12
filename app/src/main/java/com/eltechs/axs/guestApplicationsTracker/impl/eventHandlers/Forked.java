package com.eltechs.axs.guestApplicationsTracker.impl.eventHandlers;

import com.eltechs.axs.guestApplicationsTracker.impl.GuestApplicationsCollection;
import com.eltechs.axs.guestApplicationsTracker.impl.TranslatorConnection;
import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.io.IOException;

public class Forked extends RequestHandlerBase {
    public Forked(GuestApplicationsCollection guestApplicationsCollection) {
        super(guestApplicationsCollection);
    }

    public ProcessingResult handleRequest(TranslatorConnection translatorConnection, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        translatorConnection.getTranslator().forkDone(xInputStream.getInt());
        return ProcessingResult.PROCESSED;
    }
}
