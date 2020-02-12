package com.eltechs.axs.xserver.events;

public abstract class ExtensionEventCodes {
    public static final int DRI2_BUFFER_SWAP_COMPLETE = 65;
    public static final int DRI2_FIRST_EVENT = 65;
    public static final int DRI2_INVALIDATE_BUFFERS = 66;
    public static final int DRI2_LAST_ERROR = 66;
    public static final int FIRST_EXTENSION_EVENT = 64;
    public static final int GLX_BUFFER_SWAP_COMPLETE = 68;
    public static final int GLX_FIRST_EVENT = 67;
    public static final int GLX_LAST_ERROR = 68;
    public static final int GLX_PBUFFER_CLOBBER = 67;
    public static final int MIT_SHM_COMPLETION = 64;
    public static final int MIT_SHM_FIRST_EVENT = 64;
    public static final int MIT_SHM_LAST_EVENT = 64;

    private ExtensionEventCodes() {
    }
}
