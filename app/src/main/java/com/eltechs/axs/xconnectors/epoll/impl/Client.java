package com.eltechs.axs.xconnectors.epoll.impl;

import com.eltechs.axs.annotations.UsedByNativeCode;
import com.eltechs.axs.xconnectors.impl.XInputStreamImpl;
import com.eltechs.axs.xconnectors.impl.XOutputStreamImpl;

@UsedByNativeCode
public class Client<Context> {
    private final SocketWrapper clientSocket;
    private final Context context;
    private final XInputStreamImpl inputStream;
    private boolean isQueuedForProcessingBufferedMessages;
    private final XOutputStreamImpl outputStream;

    public Client(Context context2, SocketWrapper socketWrapper, XInputStreamImpl xInputStreamImpl, XOutputStreamImpl xOutputStreamImpl) {
        this.context = context2;
        this.clientSocket = socketWrapper;
        this.inputStream = xInputStreamImpl;
        this.outputStream = xOutputStreamImpl;
    }

    public int getFd() {
        return this.clientSocket.getFd();
    }

    public void closeConnection() {
        this.clientSocket.close();
    }

    public Context getContext() {
        return this.context;
    }

    public XInputStreamImpl getInputStream() {
        return this.inputStream;
    }

    public XOutputStreamImpl getOutputStream() {
        return this.outputStream;
    }

    public boolean isQueuedForProcessingBufferedMessages() {
        return this.isQueuedForProcessingBufferedMessages;
    }

    public void setQueuedForProcessingBufferedMessages(boolean z) {
        this.isQueuedForProcessingBufferedMessages = z;
    }
}
