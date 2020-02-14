package com.eltechs.axs.xconnectors.epoll.impl;

import com.eltechs.axs.annotations.UsedByNativeCode;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.proto.input.ProcessingResult;
import com.eltechs.axs.xconnectors.RequestHandler;
import com.eltechs.axs.xconnectors.impl.XInputStreamImpl;
import com.eltechs.axs.xconnectors.impl.XOutputStreamImpl;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class EpollProcessorThread<Context> extends Thread {
    private final int batchSize;
    private final BufferSizeConfiguration bufferSizeConfiguration;
    private final ClientsWithUnprocessedMessagesQueue clientsWithUnprocessedMessages;
    private final ConnectionHandler<Context> connectionHandler;
    private final ConnectionListener connectionListener;
    @UsedByNativeCode
    private final int epollFd;
    @UsedByNativeCode
    private final long fdToClientMap;
    private final RequestHandler<Context> requestHandler;
    @UsedByNativeCode
    private final int shutdownRequestFd;
    private State threadState = State.NOT_STARTED;

    private class ClientsWithUnprocessedMessagesQueue {
        final Queue<Client<Context>> impl;

        private ClientsWithUnprocessedMessagesQueue() {
            this.impl = new ArrayDeque();
        }

        public void put(Client<Context> client) {
            if (!client.isQueuedForProcessingBufferedMessages()) {
                this.impl.add(client);
                client.setQueuedForProcessingBufferedMessages(true);
            }
        }

        public Client<Context> get() {
            Client<Context> client = (Client) this.impl.poll();
            client.setQueuedForProcessingBufferedMessages(false);
            return client;
        }

        public int size() {
            return this.impl.size();
        }

        public boolean isEmpty() {
            return this.impl.isEmpty();
        }

        public void remove(Client<Context> client) {
            this.impl.remove(client);
        }
    }

    private enum State {
        NOT_STARTED,
        RUNNING,
        DESTROYED
    }

    private native int addServerSocketToEpoll(int i);

    private native int addShutdownRequestFdToEpoll(int i);

    private native void closeEpollFd();

    private native void closeShutdownRequestFd();

    private native int createEpollFd();

    private native long createFdToClientMap();

    private native int createShutdownRequestFd();

    private native void destroyFdToClientMapAndKillConnections();

    private native int doEpoll(int i);

    private static native boolean initialiseNativeParts();

    private native int pollForRead(int i, Client<Context> client);

    private native int pollForWrite(int i, Client<Context> client, boolean z);

    private native int removeFromPoll(int i);

    private native void requestShutdown();

    static {
        System.loadLibrary("xconnector-fairepoll");
        Assert.state(initialiseNativeParts(), "Managed and native parts of EpollProcessorThread do not match one another.");
    }

    public EpollProcessorThread(ConnectionListener connectionListener2, ConnectionHandler<Context> connectionHandler2, RequestHandler<Context> requestHandler2, BufferSizeConfiguration bufferSizeConfiguration2, int i) throws IOException {
        this.clientsWithUnprocessedMessages = new ClientsWithUnprocessedMessagesQueue();
        this.connectionListener = connectionListener2;
        this.connectionHandler = connectionHandler2;
        this.requestHandler = requestHandler2;
        this.bufferSizeConfiguration = bufferSizeConfiguration2;
        this.batchSize = i;
        this.epollFd = createEpollFd();
        if (this.epollFd < 0) {
            connectionListener2.close();
            throw new IOException(String.format("epoll() has failed; errno = %d", new Object[]{Integer.valueOf(-this.epollFd)}));
        }
        this.shutdownRequestFd = createShutdownRequestFd();
        if (this.shutdownRequestFd < 0) {
            connectionListener2.close();
            closeEpollFd();
            throw new IOException(String.format("Failed to create the shutdown request notifier; errno = %d", new Object[]{Integer.valueOf(-this.shutdownRequestFd)}));
        }
        this.fdToClientMap = createFdToClientMap();
        if (this.fdToClientMap == 0) {
            connectionListener2.close();
            closeEpollFd();
            closeShutdownRequestFd();
            throw new IOException(String.format("Failed to allocate the list of connected clients.", new Object[0]));
        }
        int addServerSocketToEpoll = addServerSocketToEpoll(connectionListener2.getFd());
        if (addServerSocketToEpoll < 0) {
            connectionListener2.close();
            closeEpollFd();
            closeShutdownRequestFd();
            throw new IOException(String.format("Failed to start polling for incoming connections; errno = %d", new Object[]{Integer.valueOf(-addServerSocketToEpoll)}));
        }
        int addShutdownRequestFdToEpoll = addShutdownRequestFdToEpoll(this.shutdownRequestFd);
        if (addShutdownRequestFdToEpoll < 0) {
            connectionListener2.close();
            closeEpollFd();
            closeShutdownRequestFd();
            throw new IOException(String.format("Failed to start polling for shutdown requests; errno = %d", new Object[]{Integer.valueOf(-addShutdownRequestFdToEpoll)}));
        }
    }

    public void run() {
        do {
        } while (runOnce());
        shutdown();
    }

    public synchronized void startProcessing() {
        Assert.state(this.threadState == State.NOT_STARTED, "Processing thread already started.");
        this.threadState = State.RUNNING;
        start();
    }

    public synchronized void stopProcessing() throws IOException {
        Assert.state(this.threadState == State.RUNNING, "Processing thread is not running.");
        this.threadState = State.DESTROYED;
        requestShutdown();
    }

    private boolean runOnce() {
        Assert.state(this.clientsWithUnprocessedMessages.isEmpty(), "Client messages have not been fully processed by a previous invocation of runOnce().");
        if (epollIndefinitely() < 0) {
            return false;
        }
        while (!this.clientsWithUnprocessedMessages.isEmpty() && !processAvailableMessages()) {
            if (epollCheckStateNow() < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean processAvailableMessages() {
        int size = this.clientsWithUnprocessedMessages.size();
        for (int i = 0; i < size; i++) {
            processReceivedClientMessages(this.clientsWithUnprocessedMessages.get());
        }
        return this.clientsWithUnprocessedMessages.isEmpty();
    }

    @UsedByNativeCode
    private void processNewConnection() {
        int accept = this.connectionListener.accept();
        if (accept >= 0) {
            SocketWrapper socketWrapper = new SocketWrapper(accept);
            XInputStreamImpl xInputStreamImpl = new XInputStreamImpl(socketWrapper, this.bufferSizeConfiguration.getInitialInputBufferCapacity());
            XOutputStreamImpl xOutputStreamImpl = new XOutputStreamImpl(socketWrapper, this.bufferSizeConfiguration.getInitialOutputBufferCapacity());
            xInputStreamImpl.setBufferSizeHardLimit(this.bufferSizeConfiguration.getInputBufferSizeHardLimit());
            xOutputStreamImpl.setBufferSizeSoftLimit(this.bufferSizeConfiguration.getOutputBufferSizeLimit());
            xOutputStreamImpl.setBufferSizeHardLimit(this.bufferSizeConfiguration.getOutputBufferSizeHardLimit());
            Client client = new Client(this.connectionHandler.handleNewConnection(xInputStreamImpl, xOutputStreamImpl), socketWrapper, xInputStreamImpl, xOutputStreamImpl);
            if (pollForRead(accept, client) < 0) {
                client.closeConnection();
            }
        }
    }

    @UsedByNativeCode
    private void processClientMessage(Client<Context> client) {
        try {
            if (client.getInputStream().readMoreData() < 0) {
                processHangup(client);
                return;
            }
        } catch (IOException unused) {
            killConnection(client);
        }
        processReceivedClientMessages(client);
    }

    private void processReceivedClientMessages(Client<Context> client) {
        try {
            XInputStreamImpl inputStream = client.getInputStream();
            XOutputStreamImpl outputStream = client.getOutputStream();
            inputStream.prepareForReading();
            ProcessingResult processingResult = null;
            int i = 0;
            int i2 = 0;
            boolean z = false;
            while (true) {
                if (i >= this.batchSize) {
                    break;
                }
                processingResult = this.requestHandler.handleRequest(client.getContext(), inputStream, outputStream);
                if (processingResult != ProcessingResult.PROCESSED) {
                    break;
                }
                i2 = inputStream.getActiveRegionPosition();
                z = inputStream.getAvailableBytesCount() > 0;
                i++;
            }
            switch (processingResult) {
                case PROCESSED_KILL_CONNECTION:
                    killConnection(client);
                    return;
                case PROCESSED:
                case INCOMPLETE_BUFFER:
                    inputStream.doneWithReading(i2);
                    break;
                default:
                    Assert.state(false, "Request handler returned an unhandled processing result.");
                    break;
            }
            if (outputStream.hasBufferedData()) {
                Assert.notImplementedYet("Non-blocking writes are not implemented yet.");
            }
            if (z) {
                this.clientsWithUnprocessedMessages.put(client);
            }
        } catch (IOException unused) {
            killConnection(client);
        }
    }

    @UsedByNativeCode
    private void writeBufferedResponse(Client<Context> client) {
        Assert.unreachable("No client is passed to pollForWrite() yet.");
    }

    @UsedByNativeCode
    private void killConnection(Client<Context> client) {
        this.connectionHandler.handleConnectionShutdown(client.getContext());
        removeFromPoll(client.getFd());
        client.closeConnection();
        this.clientsWithUnprocessedMessages.remove(client);
    }

    private void processHangup(Client<Context> client) {
        if (!client.isQueuedForProcessingBufferedMessages()) {
            killConnection(client);
        }
    }

    private void shutdown() {
        this.connectionListener.close();
        destroyFdToClientMapAndKillConnections();
        closeEpollFd();
        closeShutdownRequestFd();
    }

    private int epollIndefinitely() {
        return doEpoll(-1);
    }

    private int epollCheckStateNow() {
        return doEpoll(0);
    }
}
