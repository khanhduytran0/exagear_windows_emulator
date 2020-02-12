package com.eltechs.axs.rendering.impl.virglRenderer;

public class VirglServer {
    public native void startServer(String str);

    public native void stopServer();

    static {
        System.loadLibrary("virgl-server");
    }
}
