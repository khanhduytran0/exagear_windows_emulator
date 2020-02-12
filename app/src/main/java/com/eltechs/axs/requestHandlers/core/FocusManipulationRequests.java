package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.errors.BadValue;
import com.eltechs.axs.proto.input.errors.BadWindow;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.FocusManager;
import com.eltechs.axs.xserver.FocusManager.FocusReversionPolicy;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FocusManipulationRequests extends HandlerObjectBase {
    private final int FOCUS_WINDOW_NONE = 0;
    private final int FOCUS_WINDOW_POINTER_ROOT = 1;

    public FocusManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 43)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER"})
    public void GetInputFocus(XResponse xResponse) throws IOException {
        FocusManager focusManager = this.xServer.getFocusManager();
        Window focusedWindow = focusManager.getFocusedWindow();
        FocusReversionPolicy focusReversionPolicy = focusManager.getFocusReversionPolicy();
        final int i = focusedWindow != null ? focusedWindow == this.xServer.getWindowsManager().getRootWindow() ? 1 : focusedWindow.getId() : 0;
        xResponse.sendSimpleSuccessReply((byte) focusReversionPolicy.ordinal(), (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(i);
            }
        });
    }

    @RequestHandler(opcode = 42)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER"})
    public void SetInputFocus(@OOBParam @RequestParam FocusReversionPolicy focusReversionPolicy, @RequestParam int i, @RequestParam int i2) throws BadValue, BadWindow {
        Window window;
        this.xServer.getFocusManager();
        switch (i) {
            case 0:
                window = null;
                break;
            case 1:
                window = this.xServer.getWindowsManager().getRootWindow();
                break;
            default:
                Window window2 = this.xServer.getWindowsManager().getWindow(i);
                if (window2 != null) {
                    window = window2;
                    break;
                } else {
                    throw new BadWindow(i);
                }
        }
        this.xServer.getFocusManager().setFocus(window, focusReversionPolicy);
    }
}
