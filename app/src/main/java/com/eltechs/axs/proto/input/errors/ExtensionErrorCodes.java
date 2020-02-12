package com.eltechs.axs.proto.input.errors;

public abstract class ExtensionErrorCodes {
    public static final int FIRST_EXTENSION_ERROR = 128;
    public static final int GLX_BAD_CONTEXT = 129;
    public static final int GLX_BAD_CONTEXT_STATE = 130;
    public static final int GLX_BAD_CONTEXT_TAG = 133;
    public static final int GLX_BAD_CURRENT_DRAWABLE = 140;
    public static final int GLX_BAD_CURRENT_WINDOW = 134;
    public static final int GLX_BAD_DRAWABLE = 131;
    public static final int GLX_BAD_FB_CONFIG = 138;
    public static final int GLX_BAD_LARGE_REQUEST = 136;
    public static final int GLX_BAD_PBUFFER = 139;
    public static final int GLX_BAD_PIXMAP = 132;
    public static final int GLX_BAD_RENDER_REQUEST = 135;
    public static final int GLX_BAD_WINDOW = 141;
    public static final int GLX_FIRST_ERROR = 129;
    public static final int GLX_LAST_ERROR = 141;
    public static final int GLX_UNSUPPORTED_PRIVATE_REQUEST = 137;
    public static final int MIT_SHM_BAD_SEG = 128;
    public static final int MIT_SHM_FIRST_ERROR = 128;
    public static final int MIT_SHM_LAST_ERROR = 128;

    private ExtensionErrorCodes() {
    }
}
