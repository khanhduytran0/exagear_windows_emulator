package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.DeviceGrabMode;
import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.GrabsManager;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import com.eltechs.axs.xserver.helpers.WindowHelpers.MapState;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.io.IOException;

public class GrabManipulationRequests extends HandlerObjectBase {

    private enum GrabReplyTypes {
        SUCCESS,
        ALREADY_GRABBED,
        INVALID_TIME,
        NOT_VIEWABLE,
        FROZEN
    }

    @RequestHandler(opcode = 32)
    @Locks({"WINDOWS_MANAGER", "CURSORS_MANAGER", "FOCUS_MANAGER"})
    public void UngrabKeyboard(@RequestParam int i) {
    }

    public GrabManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 26)
    @Locks({"WINDOWS_MANAGER", "CURSORS_MANAGER", "INPUT_DEVICES", "FOCUS_MANAGER"})
    public void GrabPointer(XClient xClient, XResponse xResponse, @OOBParam @RequestParam boolean z, @RequestParam Window window, @RequestParam @Width(2) Mask<EventName> mask, @RequestParam DeviceGrabMode deviceGrabMode, @RequestParam DeviceGrabMode deviceGrabMode2, @RequestParam @SpecialNullValue(0) Window window2, @RequestParam @SpecialNullValue(0) Cursor cursor, @RequestParam int i) throws IOException {
        GrabReplyTypes grabReplyTypes;
        XClient xClient2;
        GrabsManager grabsManager = this.xServer.getGrabsManager();
        if (i != 0) {
            Assert.notImplementedYet();
        }
        if (grabsManager.getPointerGrabWindow() != null) {
            xClient2 = xClient;
            if (grabsManager.getPointerGrabListener().getClient() != xClient2) {
                grabReplyTypes = GrabReplyTypes.ALREADY_GRABBED;
                xResponse.sendSimpleSuccessReply((byte) grabReplyTypes.ordinal(), new Object[0]);
            }
        } else {
            xClient2 = xClient;
        }
        if (WindowHelpers.getWindowMapState(window) != MapState.VIEWABLE) {
            grabReplyTypes = GrabReplyTypes.NOT_VIEWABLE;
        } else {
            GrabReplyTypes grabReplyTypes2 = GrabReplyTypes.SUCCESS;
            grabsManager.initiateActivePointerGrab(window, z, mask, cursor, window2, deviceGrabMode, deviceGrabMode2, i, xClient2);
            grabReplyTypes = grabReplyTypes2;
        }
        xResponse.sendSimpleSuccessReply((byte) grabReplyTypes.ordinal(), new Object[0]);
    }

    @RequestHandler(opcode = 27)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES"})
    public void UngrabPointer(@RequestParam int i) {
        if (i != 0) {
            Assert.notImplementedYet();
        }
        this.xServer.getGrabsManager().disablePointerGrab();
    }

    @RequestHandler(opcode = 31)
    @Locks({"WINDOWS_MANAGER", "CURSORS_MANAGER", "FOCUS_MANAGER"})
    public void GrabKeyboard(XResponse xResponse, @OOBParam @RequestParam boolean z, @RequestParam Window window, @RequestParam int i, @RequestParam boolean z2, @RequestParam boolean z3, @RequestParam short s) throws IOException {
        xResponse.sendSimpleSuccessReply((byte) 0, new Object[0]);
    }
}
