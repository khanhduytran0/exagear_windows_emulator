package com.eltechs.axs.environmentService.components;

import com.eltechs.axs.alsaServer.ALSAClient;
import com.eltechs.axs.alsaServer.ALSAClientConnectionHandler;
import com.eltechs.axs.alsaServer.ALSARequestHandler;
import com.eltechs.axs.alsaServer.PCMPlayersManager;
import com.eltechs.axs.alsaServer.impl.audioTrackBacked.AudioTrackBackedPCMPlayersFactory;
import com.eltechs.axs.environmentService.EnvironmentComponent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xconnectors.epoll.FairEpollConnector;
import com.eltechs.axs.xconnectors.epoll.UnixSocketConfiguration;
import java.io.IOException;

public class ALSAServerComponent extends EnvironmentComponent {
    private FairEpollConnector<ALSAClient> connector;
    private final UnixSocketConfiguration socketConf;

    public ALSAServerComponent(UnixSocketConfiguration unixSocketConfiguration) {
        this.socketConf = unixSocketConfiguration;
    }

    public void start() throws IOException {
        Assert.state(this.connector == null, "ALSA server component already started.");
        this.connector = FairEpollConnector.listenOnSpecifiedUnixSocket(this.socketConf, new ALSAClientConnectionHandler(new PCMPlayersManager(new AudioTrackBackedPCMPlayersFactory())), new ALSARequestHandler());
        this.connector.start();
    }

    public void stop() {
        Assert.state(this.connector != null, "ALSA server component not yet started.");
        try {
            this.connector.stop();
        } catch (IOException unused) {
        }
        this.connector = null;
    }

    public String getAddress() {
        return this.socketConf.getGuestPath();
    }
}
