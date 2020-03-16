package com.eltechs.axs.environmentService.components;

import android.content.Context;
import com.eltechs.axs.environmentService.EnvironmentComponent;

public class NativeLibsConfiguration extends EnvironmentComponent {
    private final String nativeLibsDir;

    public void start() {
    }

    public void stop() {
    }

    public NativeLibsConfiguration(Context context) {
        this.nativeLibsDir = context.getApplicationInfo().nativeLibraryDir;
    }

    public String getLibubtPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nativeLibsDir);
        sb.append("/libubt.so");
        return sb.toString();
    }

    public String getLibubt2GPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nativeLibsDir);
        sb.append("/libubt2g.so");
        return sb.toString();
    }

    public String getElfLoaderPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nativeLibsDir);
        sb.append("/libelfloader.so");
        return sb.toString();
    }

    public String getKillswitchPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nativeLibsDir);
        sb.append("/libkillswitch.so");
        return sb.toString();
    }

    public String getSysVIPCEmulatorPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nativeLibsDir);
        sb.append("/libipc-emulation.so");
        return sb.toString();
    }

    public String getIsMemSplit3g1gPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nativeLibsDir);
        sb.append("/libismemsplit3g1g.so");
        return sb.toString();
    }
}
