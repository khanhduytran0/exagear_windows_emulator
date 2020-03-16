package com.eltechs.axs.environmentService.components;

import com.eltechs.axs.environmentService.EnvironmentComponent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.sysvipc.Emulator;
import com.eltechs.axs.sysvipc.SHMEngine;
import com.eltechs.axs.sysvipc.SHMEngineImpl;
import java.io.IOException;

public class SysVIPCEmulatorComponent extends EnvironmentComponent {
    private final String domainName;
    private Emulator emulator;
    private SHMEngineImpl shmEngine;

    public SysVIPCEmulatorComponent(String str) {
        this.domainName = str;
    }

    public void start() throws IOException {
        Assert.state(this.emulator == null, "Sys V IPC emulator component is already started.");
        NativeLibsConfiguration nativeLibsConfiguration = getEnvironment().getNativeLibsConfiguration();
        this.emulator = new Emulator(this.domainName, nativeLibsConfiguration.getElfLoaderPath(), nativeLibsConfiguration.getSysVIPCEmulatorPath());
        this.shmEngine = new SHMEngineImpl(this.domainName);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0011 */
    public void stop() {
        Assert.state(this.emulator != null, "Sys V IPC emulator is not yet started.");
        try {
			this.shmEngine.stop();
            this.emulator.stop();
        } catch (Exception unused) {
        }
        this.emulator = null;
        this.shmEngine = null;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public SHMEngine getShmEngine() {
        return this.shmEngine;
    }
}
