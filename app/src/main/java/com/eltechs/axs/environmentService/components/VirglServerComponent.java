package com.eltechs.axs.environmentService.components;

import com.eltechs.axs.environmentService.EnvironmentComponent;
import com.eltechs.axs.rendering.impl.virglRenderer.VirglServer;
import com.eltechs.axs.xconnectors.epoll.UnixSocketConfiguration;
import java.io.IOException;

public class VirglServerComponent extends EnvironmentComponent {
    /* access modifiers changed from: private */
    public final UnixSocketConfiguration socketConf;
    private Thread t = null;
    /* access modifiers changed from: private */
    public final VirglServer virglServer;

    public VirglServerComponent(UnixSocketConfiguration unixSocketConfiguration) {
        this.socketConf = unixSocketConfiguration;
        this.virglServer = new VirglServer();
    }

    public void start() throws IOException {
        this.t = new Thread() {
            public void run() {
                VirglServerComponent.this.virglServer.startServer(VirglServerComponent.this.socketConf.getHostPath());
            }
        };
        this.t.start();
    }

    public void stop() {
        this.virglServer.stopServer();
    }

    public String getAddress() {
        return this.socketConf.getGuestPath();
    }
}
