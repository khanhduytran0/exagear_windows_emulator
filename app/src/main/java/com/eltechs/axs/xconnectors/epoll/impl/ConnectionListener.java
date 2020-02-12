package com.eltechs.axs.xconnectors.epoll.impl;

import com.eltechs.axs.annotations.UsedByNativeCode;
import com.eltechs.axs.helpers.Assert;
import java.io.IOException;

public class ConnectionListener {
    @UsedByNativeCode
    private final int fd;

    private native int acceptImpl();

    private native void closeImpl();

    private static native int createAbstractAfUnixSocket(String str);

    private static native int createAfUnixSocket(String str);

    private static native int createLoopbackInetSocket(int i);

    private static native boolean initialiseNativeParts();

    static {
        System.loadLibrary("xconnector-fairepoll");
        Assert.state(initialiseNativeParts(), "Managed and native parts of EpollProcessorThread do not match one another.");
    }

    private ConnectionListener(int i) {
        this.fd = i;
    }

    public static ConnectionListener forLoopbackInetAddress(int i) throws IOException {
        int createLoopbackInetSocket = createLoopbackInetSocket(i);
        if (createLoopbackInetSocket >= 0) {
            return new ConnectionListener(createLoopbackInetSocket);
        }
        throw new IOException(String.format("Failed to create an AF_INET socket listening on 127.0.0.1:%d; errno = %d.", new Object[]{Integer.valueOf(i), Integer.valueOf(-createLoopbackInetSocket)}));
    }

    public static ConnectionListener forAbstractAfUnixAddress(String str) throws IOException {
        int createAbstractAfUnixSocket = createAbstractAfUnixSocket(str);
        if (createAbstractAfUnixSocket >= 0) {
            return new ConnectionListener(createAbstractAfUnixSocket);
        }
        throw new IOException(String.format("Failed to create an AF_UNIX socket listening on '\\0%s'; errno = %d.", new Object[]{str, Integer.valueOf(-createAbstractAfUnixSocket)}));
    }

    public static ConnectionListener forAfUnixAddress(String str) throws IOException {
        int createAfUnixSocket = createAfUnixSocket(str);
        if (createAfUnixSocket >= 0) {
            return new ConnectionListener(createAfUnixSocket);
        }
        throw new IOException(String.format("Failed to create an AF_UNIX socket listening on %s; errno = %d.", new Object[]{str, Integer.valueOf(-createAfUnixSocket)}));
    }

    public int getFd() {
        return this.fd;
    }

    public int accept() {
        return acceptImpl();
    }

    public void close() {
        closeImpl();
    }
}
