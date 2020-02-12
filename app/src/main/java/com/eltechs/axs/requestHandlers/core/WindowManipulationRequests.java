package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.geom.Point;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.Optional;
import com.eltechs.axs.proto.input.annotations.ParamName;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.Signed;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.proto.input.annotations.Unsigned;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadAccess;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.proto.input.errors.BadLength;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.proto.input.errors.BadValue;
import com.eltechs.axs.proto.input.impl.ProtoHelpers;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.BitGravity;
import com.eltechs.axs.xserver.ConfigureWindowParts;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.EventName;
import com.eltechs.axs.xserver.StackMode;
import com.eltechs.axs.xserver.WinGravity;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowAttributeNames;
import com.eltechs.axs.xserver.WindowAttributes;
import com.eltechs.axs.xserver.WindowAttributes.BackingStore;
import com.eltechs.axs.xserver.WindowPropertiesManager.PropertyModification;
import com.eltechs.axs.xserver.WindowProperty;
import com.eltechs.axs.xserver.WindowProperty.Format;
import com.eltechs.axs.xserver.WindowsManager;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.events.ConfigureNotify;
import com.eltechs.axs.xserver.events.ConfigureRequest;
import com.eltechs.axs.xserver.events.CreateNotify;
import com.eltechs.axs.xserver.helpers.WindowHelpers;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.io.IOException;
import java.nio.ByteBuffer;

public class WindowManipulationRequests extends HandlerObjectBase {

    public enum WindowClass {
        COPY_FROM_PARENT,
        INPUT_OUTPUT,
        INPUT_ONLY
    }

    public WindowManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 8)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES", "KEYBOARD_MODEL_MANAGER", "ATOMS_MANAGER"})
    public void MapWindow(@RequestParam Window window) {
        this.xServer.getWindowsManager().mapWindow(window);
    }

    @RequestHandler(opcode = 9)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES", "KEYBOARD_MODEL_MANAGER"})
    public void MapSubwindows(@RequestParam Window window) {
        this.xServer.getWindowsManager().mapSubwindows(window);
    }

    @RequestHandler(opcode = 10)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES", "KEYBOARD_MODEL_MANAGER"})
    public void UnmapWindow(@RequestParam Window window) {
        this.xServer.getWindowsManager().unmapWindow(window);
    }

    @RequestHandler(opcode = 11)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES", "KEYBOARD_MODEL_MANAGER"})
    public void UnmapSubwindows(@RequestParam Window window) {
        this.xServer.getWindowsManager().unmapSubwindows(window);
    }

    @RequestHandler(opcode = 4)
    @Locks({"WINDOWS_MANAGER", "DRAWABLES_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES"})
    public void DestroyWindow(@RequestParam Window window) {
        this.xServer.getWindowsManager().destroyWindow(window);
    }

    @RequestHandler(opcode = 5)
    @Locks({"WINDOWS_MANAGER", "DRAWABLES_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES"})
    public void DestroySubwindows(@RequestParam Window window) {
        this.xServer.getWindowsManager().destroySubwindows(window);
    }

    @RequestHandler(opcode = 12)
    @Locks({"WINDOWS_MANAGER", "FOCUS_MANAGER", "INPUT_DEVICES"})
    public void ConfigureWindow(@RequestParam Window window, @RequestParam @Width(2) @ParamName("mask") Mask<ConfigureWindowParts> mask, @RequestParam short s, @RequestParam @Width(4) @Optional(bit = "X") Integer num, @RequestParam @Width(4) @Optional(bit = "Y") Integer num2, @RequestParam @Width(4) @Optional(bit = "WIDTH") Integer num3, @RequestParam @Width(4) @Optional(bit = "HEIGHT") Integer num4, @RequestParam @Width(4) @Optional(bit = "BORDER_WIDTH") Short sh, @RequestParam @Optional(bit = "SIBLING") Window window2, @RequestParam @Width(4) @Optional(bit = "STACK_MODE") StackMode stackMode) {
        int borderWidth;
        Window window3 = window;
        Mask<ConfigureWindowParts> mask2 = mask;
        if (!mask.isEmpty()) {
            WindowsManager windowsManager = this.xServer.getWindowsManager();
            Window parent = window.getParent();
            Rectangle boundingRectangle = window.getBoundingRectangle();
            Integer valueOf = num == null ? Integer.valueOf(boundingRectangle.x) : num;
            Integer valueOf2 = num2 == null ? Integer.valueOf(boundingRectangle.y) : num2;
            Integer valueOf3 = num3 == null ? Integer.valueOf(boundingRectangle.width) : num3;
            Integer valueOf4 = num4 == null ? Integer.valueOf(boundingRectangle.height) : num4;
            if (!parent.getEventListenersList().isListenerInstalledForEvent(EventName.SUBSTRUCTURE_REDIRECT) || window.getWindowAttributes().isOverrideRedirect()) {
                if (mask2.isSet(ConfigureWindowParts.X) || mask2.isSet(ConfigureWindowParts.Y) || mask2.isSet(ConfigureWindowParts.WIDTH) || mask2.isSet(ConfigureWindowParts.HEIGHT)) {
                    windowsManager.changeRelativeWindowGeometry(window3, valueOf.intValue(), valueOf2.intValue(), valueOf3.intValue(), valueOf4.intValue());
                }
                if (sh != null) {
                    window.getWindowAttributes().setBorderWidth(ArithHelpers.extendAsUnsigned(sh.shortValue()));
                }
                if (mask2.isSet(ConfigureWindowParts.STACK_MODE)) {
                    windowsManager.changeWindowZOrder(window3, window2, stackMode);
                }
                Window prevSibling = parent.getChildrenList().getPrevSibling(window3);
                Rectangle boundingRectangle2 = window.getBoundingRectangle();
                Window window4 = window3;
                Window window5 = prevSibling;
                ConfigureNotify configureNotify = new ConfigureNotify(window3, window4, window5, boundingRectangle2.x, boundingRectangle2.y, boundingRectangle2.width, boundingRectangle2.height, window.getWindowAttributes().getBorderWidth(), window.getWindowAttributes().isOverrideRedirect());
                window.getEventListenersList().sendEventForEventName(configureNotify, EventName.STRUCTURE_NOTIFY);
                ConfigureNotify configureNotify2 = new ConfigureNotify(parent, window4, window5, boundingRectangle2.x, boundingRectangle2.y, boundingRectangle2.width, boundingRectangle2.height, window.getWindowAttributes().getBorderWidth(), window.getWindowAttributes().isOverrideRedirect());
                parent.getEventListenersList().sendEventForEventName(configureNotify2, EventName.SUBSTRUCTURE_NOTIFY);
                return;
            }
            if (sh != null) {
                borderWidth = sh.shortValue();
            } else {
                borderWidth = window.getWindowAttributes().getBorderWidth();
            }
            ConfigureRequest configureRequest = new ConfigureRequest(parent, window3, window.getParent().getChildrenList().getPrevSibling(window3), valueOf.intValue(), valueOf2.intValue(), valueOf3.intValue(), valueOf4.intValue(), borderWidth, stackMode, mask2);
            parent.getEventListenersList().sendEventForEventName(configureRequest, EventName.SUBSTRUCTURE_REDIRECT);
        }
    }

    @RequestHandler(opcode = 20)
    @Locks({"WINDOWS_MANAGER", "ATOMS_MANAGER"})
    public void GetProperty(XResponse xResponse, @OOBParam @RequestParam boolean z, @RequestParam Window window, @RequestParam Atom atom, @RequestParam @SpecialNullValue(0) Atom atom2, @RequestParam int i, @RequestParam int i2) throws IOException, XProtocolError {
        XResponse xResponse2 = xResponse;
        Atom atom3 = atom;
        Atom atom4 = atom2;
        int i3 = i;
        WindowProperty property = window.getPropertiesManager().getProperty(atom3);
        if (property == null) {
            xResponse2.sendSimpleSuccessReply((byte) 0, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
            return;
        }
        Atom type = property.getType();
        byte formatValue = property.getFormat().getFormatValue();
        if (atom4 == null || type.equals(atom4)) {
            byte[] bArr = new byte[12];
            int sizeInBytes = property.getSizeInBytes();
            int i4 = i3 * 4;
            long min = Math.min((long) (sizeInBytes - i4), ArithHelpers.extendAsUnsigned(4 * i2));
            if (min < 0) {
                throw new BadValue(i3);
            }
            int i5 = (int) min;
            int i6 = sizeInBytes - (i4 + i5);
            if (formatValue == 8) {
                byte[] bArr2 = new byte[(ProtoHelpers.calculatePad(i5) + i5)];
                System.arraycopy((byte[]) property.getValues(), i4, bArr2, 0, i5);
                xResponse2.sendSimpleSuccessReply(formatValue, Integer.valueOf(type.getId()), Integer.valueOf(i6), Integer.valueOf(bArr2.length), bArr, bArr2);
            } else if (formatValue == 16) {
                int i7 = i5 / 2;
                short[] sArr = new short[i7];
                byte[] bArr3 = new byte[ProtoHelpers.calculatePad(i5)];
                System.arraycopy((short[]) property.getValues(), i4 / 2, sArr, 0, i7);
                xResponse2.sendSimpleSuccessReply(formatValue, Integer.valueOf(type.getId()), Integer.valueOf(i6), Integer.valueOf(sArr.length), bArr, sArr, bArr3);
            } else if (formatValue != 32) {
                Assert.state(false, String.format("Strange format value (%d) in GetProperty method.", new Object[]{Byte.valueOf(formatValue)}));
            } else {
                int i8 = i5 / 4;
                int[] iArr = new int[i8];
                System.arraycopy((int[]) property.getValues(), i4 / 4, iArr, 0, i8);
                xResponse2.sendSimpleSuccessReply(formatValue, Integer.valueOf(type.getId()), Integer.valueOf(i6), Integer.valueOf(iArr.length), bArr, iArr);
            }
            if (z && i6 == 0) {
                window.getPropertiesManager().deleteProperty(atom3);
            }
        } else {
            xResponse2.sendSimpleSuccessReply(formatValue, Integer.valueOf(type.getId()), Integer.valueOf(property.getSizeInBytes()), Integer.valueOf(0));
        }
    }

    @RequestHandler(opcode = 18)
    @Locks({"WINDOWS_MANAGER", "ATOMS_MANAGER"})
    public void ChangeProperty(@OOBParam @RequestParam PropertyModification propertyModification, @RequestParam Window window, @RequestParam Atom atom, @RequestParam Atom atom2, @RequestParam byte b, @RequestParam byte b2, @RequestParam byte b3, @RequestParam byte b4, @RequestParam int i, @RequestParam ByteBuffer byteBuffer) throws XProtocolError {
        Format format;
        Object obj;
        byte b5 = b;
        int i2 = i;
        if (b5 != 8) {
            int i3 = 0;
            if (b5 != 16) {
                if (b5 != 32) {
                    throw new BadValue(b5);
                } else if (4 * i2 > byteBuffer.limit()) {
                    throw new BadLength();
                } else {
                    int[] iArr = new int[i2];
                    while (i3 < i2) {
                        iArr[i3] = byteBuffer.getInt();
                        i3++;
                    }
                    format = WindowProperty.ARRAY_OF_INTS;
                    obj = iArr;
                }
            } else if (2 * i2 > byteBuffer.limit()) {
                throw new BadLength();
            } else {
                short[] sArr = new short[i2];
                while (i3 < i2) {
                    sArr[i3] = byteBuffer.getShort();
                    i3++;
                }
                format = WindowProperty.ARRAY_OF_SHORTS;
                obj = sArr;
            }
        } else if (i2 > byteBuffer.limit()) {
            throw new BadLength();
        } else {
            byte[] bArr = new byte[i2];
            byteBuffer.get(bArr);
            format = WindowProperty.ARRAY_OF_BYTES;
            obj = bArr;
        }
        if (!window.getPropertiesManager().modifyProperty(atom, atom2, format, propertyModification, obj)) {
            throw new BadMatch();
        }
    }

    @RequestHandler(opcode = 19)
    @Locks({"WINDOWS_MANAGER", "ATOMS_MANAGER"})
    public void DeleteProperty(@RequestParam Window window, @RequestParam Atom atom) throws XProtocolError {
        window.getPropertiesManager().deleteProperty(atom);
    }

    @RequestHandler(opcode = 1)
    @Locks({"WINDOWS_MANAGER", "DRAWABLES_MANAGER", "INPUT_DEVICES", "COLORMAPS_MANAGER", "CURSORS_MANAGER", "FOCUS_MANAGER"})
    public void CreateWindow(XClient xClient, @OOBParam @RequestParam byte b, @NewXId @RequestParam int i, @RequestParam Window window, @RequestParam @Width(2) @Signed int i2, @RequestParam @Width(2) @Signed int i3, @RequestParam @Width(2) @Unsigned int i4, @RequestParam @Width(2) @Unsigned int i5, @RequestParam @Width(2) @Unsigned int i6, @RequestParam @Width(2) WindowClass windowClass, @RequestParam @SpecialNullValue(0) Visual visual, @RequestParam @ParamName("mask") Mask<WindowAttributeNames> mask, @RequestParam @Optional(bit = "BACKGROUND_PIXMAP") Integer num, @RequestParam @Optional(bit = "BACKGROUND_PIXEL") Integer num2, @RequestParam @Optional(bit = "BORDER_PIXMAP") Integer num3, @RequestParam @Optional(bit = "BORDER_PIXEL") Integer num4, @RequestParam @Width(4) @Optional(bit = "BIT_GRAVITY") BitGravity bitGravity, @RequestParam @Width(4) @Optional(bit = "WIN_GRAVITY") WinGravity winGravity, @RequestParam @Width(4) @Optional(bit = "BACKING_STORE") BackingStore backingStore, @RequestParam @Optional(bit = "BACKING_PLANES") Integer num5, @RequestParam @Optional(bit = "BACKING_PIXEL") Integer num6, @RequestParam @Width(4) @Optional(bit = "OVERRIDE_REDIRECT") Boolean bool, @RequestParam @Width(4) @Optional(bit = "SAVE_UNDER") Boolean bool2, @RequestParam @Optional(bit = "EVENT_MASK") Mask<EventName> mask2, @RequestParam @Optional(bit = "DO_NOT_PROPAGATE_MASK") Mask<EventName> mask3, @RequestParam @Optional(bit = "COLORMAP") Integer num7, @RequestParam @SpecialNullValue(0) @Optional(bit = "CURSOR") Cursor cursor) throws XProtocolError {
        boolean z;
        byte b2;
        WindowManipulationRequests windowManipulationRequests;
        Visual visual2;
        XClient xClient2 = xClient;
        Mask<WindowAttributeNames> mask4 = mask;
        Mask<EventName> emptyMask = mask2 == null ? Mask.emptyMask(EventName.class) : mask2;
        switch (windowClass) {
            case COPY_FROM_PARENT:
                boolean isInputOutput = window.isInputOutput();
                b2 = (b != 0 || !window.isInputOutput()) ? b : (byte) window.getActiveBackingStore().getVisual().getDepth();
                z = isInputOutput;
                break;
            case INPUT_OUTPUT:
                if (window.isInputOutput()) {
                    b2 = b == 0 ? (byte) window.getActiveBackingStore().getVisual().getDepth() : b;
                    z = true;
                    break;
                } else {
                    throw new BadMatch();
                }
            case INPUT_ONLY:
                b2 = b;
                z = false;
                break;
            default:
                Assert.state(false, String.format("Unsupported value %s of WindowClass.", new Object[]{windowClass}));
                return;
        }
        if (z) {
            Visual visual3 = visual == null ? window.getActiveBackingStore().getVisual() : visual;
            if (b2 != ((byte) visual3.getDepth())) {
                throw new BadMatch();
            }
            windowManipulationRequests = this;
            visual2 = visual3;
        } else {
            windowManipulationRequests = this;
            visual2 = visual;
        }
        Window createWindow = windowManipulationRequests.xServer.getWindowsManager().createWindow(i, window, i2, i3, i4, i5, visual2, z, xClient2);
        if (createWindow == null) {
            throw new BadIdChoice(i);
        }
        xClient2.installEventListener(createWindow, emptyMask);
        WindowAttributes windowAttributes = createWindow.getWindowAttributes();
        windowAttributes.setBorderWidth(i6);
        Mask<WindowAttributeNames> mask5 = mask4;
        windowAttributes.update(mask4, num3, num4, bitGravity, winGravity, backingStore, num5, num6, bool, bool2, mask3, num7, cursor);
        mask5.isSet(WindowAttributeNames.BACKGROUND_PIXMAP);
        if (mask5.isSet(WindowAttributeNames.BACKGROUND_PIXEL)) {
            createWindow.getActiveBackingStore().getPainter().fillWithColor(num2.intValue());
        }
        xClient2.registerAsOwnerOfWindow(createWindow);
        window.getEventListenersList().sendEventForEventName(new CreateNotify(window, createWindow), EventName.SUBSTRUCTURE_NOTIFY);
    }

    @RequestHandler(opcode = 2)
    @Locks({"WINDOWS_MANAGER", "COLORMAPS_MANAGER", "CURSORS_MANAGER"})
    public void ChangeWindowAttributes(XClient xClient, @RequestParam Window window, @RequestParam @ParamName("mask") Mask<WindowAttributeNames> mask, @RequestParam @Optional(bit = "BACKGROUND_PIXMAP") Integer num, @RequestParam @Optional(bit = "BACKGROUND_PIXEL") Integer num2, @RequestParam @Optional(bit = "BORDER_PIXMAP") Integer num3, @RequestParam @Optional(bit = "BORDER_PIXEL") Integer num4, @RequestParam @Width(4) @Optional(bit = "BIT_GRAVITY") BitGravity bitGravity, @RequestParam @Width(4) @Optional(bit = "WIN_GRAVITY") WinGravity winGravity, @RequestParam @Width(4) @Optional(bit = "BACKING_STORE") BackingStore backingStore, @RequestParam @Optional(bit = "BACKING_PLANES") Integer num5, @RequestParam @Optional(bit = "BACKING_PIXEL") Integer num6, @RequestParam @Width(4) @Optional(bit = "OVERRIDE_REDIRECT") Boolean bool, @RequestParam @Width(4) @Optional(bit = "SAVE_UNDER") Boolean bool2, @RequestParam @Optional(bit = "EVENT_MASK") Mask<EventName> mask2, @RequestParam @Optional(bit = "DO_NOT_PROPAGATE_MASK") Mask<EventName> mask3, @RequestParam @Optional(bit = "COLORMAP") Integer num7, @RequestParam @SpecialNullValue(0) @Optional(bit = "CURSOR") Cursor cursor) throws XProtocolError {
        XClient xClient2 = xClient;
        Window window2 = window;
        Mask<WindowAttributeNames> mask4 = mask;
        Mask<EventName> mask5 = mask2;
        if (mask5 != null) {
            if ((!mask5.isSet(EventName.SUBSTRUCTURE_REDIRECT) || !willBeInConflict(xClient2, window2, EventName.SUBSTRUCTURE_REDIRECT)) && ((!mask5.isSet(EventName.RESIZE_REDIRECT) || !willBeInConflict(xClient2, window2, EventName.RESIZE_REDIRECT)) && (!mask5.isSet(EventName.BUTTON_PRESS) || !willBeInConflict(xClient2, window2, EventName.BUTTON_PRESS)))) {
                xClient2.installEventListener(window2, mask5);
            } else {
                throw new BadAccess();
            }
        }
        Mask<WindowAttributeNames> mask6 = mask4;
        window.getWindowAttributes().update(mask4, num3, num4, bitGravity, winGravity, backingStore, num5, num6, bool, bool2, mask3, num7, cursor);
        mask6.isSet(WindowAttributeNames.BACKGROUND_PIXMAP);
        if (mask6.isSet(WindowAttributeNames.BACKGROUND_PIXEL)) {
            window.getActiveBackingStore().getPainter().fillWithColor(num2.intValue());
        }
    }

    private boolean willBeInConflict(XClient xClient, Window window, EventName eventName) {
        return window.getEventListenersList().isListenerInstalledForEvent(eventName) && !xClient.isInterestedIn(window, eventName);
    }

    @RequestHandler(opcode = 3)
    @Locks({"WINDOWS_MANAGER"})
    public void GetWindowAttributes(XClient xClient, XResponse xResponse, @RequestParam Window window) throws IOException {
        WindowAttributes windowAttributes = window.getWindowAttributes();
        xResponse.sendSimpleSuccessReply((byte) windowAttributes.getBackingStore().ordinal(), Integer.valueOf(window.isInputOutput() ? window.getActiveBackingStore().getVisual().getId() : 0), Short.valueOf((short) windowAttributes.getWindowClass().ordinal()), Byte.valueOf((byte) windowAttributes.getBitGravity().ordinal()), Byte.valueOf((byte) windowAttributes.getWinGravity().ordinal()), Integer.valueOf(windowAttributes.getBackingPlanes()), Integer.valueOf(windowAttributes.getBackingPixel()), Boolean.valueOf(windowAttributes.isSaveUnder()), Boolean.valueOf(true), Byte.valueOf((byte) WindowHelpers.getWindowMapState(window).ordinal()), Boolean.valueOf(windowAttributes.isOverrideRedirect()), Integer.valueOf(0), Integer.valueOf(window.getEventListenersList().calculateAllEventsMask().getRawMask()), Integer.valueOf(xClient.getEventMask(window).getRawMask()), Short.valueOf((short) windowAttributes.getDoNotPropagateMask().getRawMask()));
    }

    @RequestHandler(opcode = 40)
    @Locks({"WINDOWS_MANAGER"})
    public void TranslateCoordinates(XResponse xResponse, @RequestParam Window window, @RequestParam Window window2, @RequestParam @Width(2) @Signed int i, @RequestParam @Width(2) @Signed int i2) throws IOException, XProtocolError {
        Point convertWindowCoordsToRoot = WindowHelpers.convertWindowCoordsToRoot(window, i, i2);
        final Point convertRootCoordsToWindow = WindowHelpers.convertRootCoordsToWindow(window2, convertWindowCoordsToRoot.x, convertWindowCoordsToRoot.y);
        final Window directMappedSubWindowByCoords = WindowHelpers.getDirectMappedSubWindowByCoords(window2, convertWindowCoordsToRoot.x, convertWindowCoordsToRoot.y);
        xResponse.sendSimpleSuccessReply((byte) 1, (ResponseDataWriter) new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                if (directMappedSubWindowByCoords != null) {
                    byteBuffer.putInt(directMappedSubWindowByCoords.getId());
                } else {
                    byteBuffer.putInt(0);
                }
                byteBuffer.putShort((short)(short) convertRootCoordsToWindow.x);
                byteBuffer.putShort((short)(short) convertRootCoordsToWindow.y);
            }
        });
    }

    @RequestHandler(opcode = 7)
    @Locks({"WINDOWS_MANAGER"})
    public void ReparentWindow(XResponse xResponse, @RequestParam Window window, @RequestParam Window window2, @RequestParam @Width(2) @Signed int i, @RequestParam @Width(2) @Signed int i2) {
        Window parent = window.getParent();
        if (parent != null) {
            parent.getChildrenList().remove(window);
        }
        window.setParent(null);
        window2.getChildrenList().add(window);
    }
}
