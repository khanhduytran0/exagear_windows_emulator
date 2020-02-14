package com.eltechs.axs.xconnectors.epoll;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ConnectionHandler;
import com.eltechs.axs.xconnectors.RequestHandler;
import com.eltechs.axs.xconnectors.epoll.impl.BufferSizeConfiguration;
import com.eltechs.axs.xconnectors.epoll.impl.ConnectionListener;
import com.eltechs.axs.xconnectors.epoll.impl.EpollProcessorThread;
import java.io.IOException;

public class FairEpollConnector<Context> {
    private int batchSize = 1;
    private final BufferSizeConfiguration bufferSizeConfiguration;
    private final ConnectionHandler<Context> connectionHandler;
    private final ConnectionListenerFactory connectionListenerFactory;
    private transient EpollProcessorThread processorThread;
    private final RequestHandler<Context> requestHandler;

    public interface ConnectionListenerFactory {
        ConnectionListener createConnectionListener() throws IOException;
    }

    private FairEpollConnector(ConnectionListenerFactory connectionListenerFactory2, ConnectionHandler<Context> connectionHandler2, RequestHandler<Context> requestHandler2, BufferSizeConfiguration bufferSizeConfiguration2) {
        this.connectionListenerFactory = connectionListenerFactory2;
        this.connectionHandler = connectionHandler2;
        this.requestHandler = requestHandler2;
        this.bufferSizeConfiguration = bufferSizeConfiguration2;
    }

    public static <Context> FairEpollConnector<Context> listenOnSpecifiedUnixSocket(final UnixSocketConfiguration unixSocketConfiguration, ConnectionHandler<Context> connectionHandler2, RequestHandler<Context> requestHandler2) throws IOException {
        ConnectionListenerFactory connectionListenerFactory2;
        if (unixSocketConfiguration.isAbstract()) {
            connectionListenerFactory2 = new ConnectionListenerFactory() {
                public ConnectionListener createConnectionListener() throws IOException {
                    return ConnectionListener.forAbstractAfUnixAddress(unixSocketConfiguration.getGuestPath());
                }
            };
        } else {
            connectionListenerFactory2 = new ConnectionListenerFactory() {
                public ConnectionListener createConnectionListener() throws IOException {
                    return ConnectionListener.forAfUnixAddress(unixSocketConfiguration.getHostPath());
                }
            };
        }
        return new FairEpollConnector<>(connectionListenerFactory2, connectionHandler2, requestHandler2, BufferSizeConfiguration.createDefaultConfiguration());
    }

    public static <Context> FairEpollConnector<Context> listenOnLoopbackInetAddress(final int i, ConnectionHandler<Context> connectionHandler2, RequestHandler<Context> requestHandler2) throws IOException {
        return new FairEpollConnector<>(new ConnectionListenerFactory() {
            public ConnectionListener createConnectionListener() throws IOException {
                return ConnectionListener.forLoopbackInetAddress(i);
            }
        }, connectionHandler2, requestHandler2, BufferSizeConfiguration.createDefaultConfiguration());
    }

    public void start() throws IOException {
        Assert.state(this.processorThread == null, "The connector is already running.");
        this.processorThread = new EpollProcessorThread(this.connectionListenerFactory.createConnectionListener(), this.connectionHandler, this.requestHandler, this.bufferSizeConfiguration, this.batchSize);
        this.processorThread.startProcessing();
    }

    public void stop() throws IOException {
        Assert.state(this.processorThread != null, "The connector is not yet running.");
        this.processorThread.stopProcessing();
        while (this.processorThread.isAlive()) {
            try {
                this.processorThread.join();
            } catch (InterruptedException unused) {
            }
        }
        this.processorThread = null;
    }

    public void setInitialInputBufferCapacity(int i) {
        this.bufferSizeConfiguration.setInitialInputBufferCapacity(i);
    }

    public void setInitialOutputBufferCapacity(int i) {
        this.bufferSizeConfiguration.setInitialOutputBufferCapacity(i);
    }

    public void setOutputBufferSizeLimit(int i) {
        this.bufferSizeConfiguration.setOutputBufferSizeLimit(i);
    }

    public void setInputBufferSizeHardLimit(int i) {
        this.bufferSizeConfiguration.setInputBufferSizeHardLimit(i);
    }

    public void setOutputBufferSizeHardLimit(int i) {
        this.bufferSizeConfiguration.setOutputBufferSizeHardLimit(i);
    }

    public void setBatchSize(int i) {
        Assert.isTrue(i > 0, "Batch size must a positive integer.");
        this.batchSize = i;
    }
}
