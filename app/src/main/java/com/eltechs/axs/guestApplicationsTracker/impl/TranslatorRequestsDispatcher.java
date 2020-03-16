package com.eltechs.axs.guestApplicationsTracker.impl;

import com.eltechs.axs.guestApplicationsTracker.impl.eventHandlers.AboutToFork;
import com.eltechs.axs.guestApplicationsTracker.impl.eventHandlers.Forked;
import com.eltechs.axs.guestApplicationsTracker.impl.eventHandlers.TranslatorStarted;
import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.xconnectors.RequestHandler;
import com.eltechs.axs.xconnectors.XInputStream;
import com.eltechs.axs.xconnectors.XOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class TranslatorRequestsDispatcher implements RequestHandler<TranslatorConnection> {
    public static final int MAGIC = 1263685446;
    public static final int MINIMUM_REQUEST_LENGTH = 8;
    public static final int MINIMUM_RESPONSE_LENGTH = 6;
    private static final int SIZE_OF_INT = 4;
    private static final int SIZE_OF_SHORT = 2;
    private final GuestApplicationsCollection guestApplicationsCollection;
    private final Map<RequestCodes, RequestHandler<TranslatorConnection>> requestHandlers = new EnumMap(RequestCodes.class);

    public TranslatorRequestsDispatcher(GuestApplicationsCollection guestApplicationsCollection2) {
        this.guestApplicationsCollection = guestApplicationsCollection2;
        this.requestHandlers.put(RequestCodes.TRANSLATOR_STARTED, new TranslatorStarted(guestApplicationsCollection2));
        this.requestHandlers.put(RequestCodes.ABOUT_TO_FORK, new AboutToFork(guestApplicationsCollection2));
        this.requestHandlers.put(RequestCodes.FORKED, new Forked(guestApplicationsCollection2));
    }

    public ProcessingResult handleRequest(TranslatorConnection translatorConnection, XInputStream xInputStream, XOutputStream xOutputStream) throws IOException {
        if (xInputStream.getAvailableBytesCount() < 8) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        int i = xInputStream.getInt();
        int extendAsUnsigned = ArithHelpers.extendAsUnsigned(xInputStream.getShort()) - 8;
        int extendAsUnsigned2 = ArithHelpers.extendAsUnsigned(xInputStream.getShort());
        if (i != 1263685446 || extendAsUnsigned2 < 0 || extendAsUnsigned2 >= this.requestHandlers.size()) {
            return ProcessingResult.PROCESSED_KILL_CONNECTION;
        }
        if (xInputStream.getAvailableBytesCount() < extendAsUnsigned) {
            return ProcessingResult.INCOMPLETE_BUFFER;
        }
        return ((RequestHandler) this.requestHandlers.get(RequestCodes.values()[extendAsUnsigned2])).handleRequest(translatorConnection, xInputStream, xOutputStream);
    }
}
