package com.eltechs.axs.guestApplicationVFSTracker;

import com.eltechs.axs.guestApplicationVFSTracker.impl.VFSTrackerConnection;
import com.eltechs.axs.guestApplicationVFSTracker.impl.VFSTrackerConnectionHandler;
import com.eltechs.axs.guestApplicationVFSTracker.impl.VFSTrackerRequestsDispatcher;
import com.eltechs.axs.xconnectors.epoll.FairEpollConnector;
import com.eltechs.axs.xconnectors.epoll.UnixSocketConfiguration;
import java.io.IOException;

public class VFSTracker {
    private final FairEpollConnector<VFSTrackerConnection> connector;

    public VFSTracker(UnixSocketConfiguration unixSocketConfiguration, SyscallReportHandler syscallReportHandler) throws IOException {
        this.connector = FairEpollConnector.listenOnSpecifiedUnixSocket(unixSocketConfiguration, new VFSTrackerConnectionHandler(), new VFSTrackerRequestsDispatcher(syscallReportHandler));
        this.connector.start();
    }

    public void stop() throws IOException {
        this.connector.stop();
    }
}
