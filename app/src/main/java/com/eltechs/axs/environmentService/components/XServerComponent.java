package com.eltechs.axs.environmentService.components;

import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.desktopExperience.DesktopExperienceImpl;
import com.eltechs.axs.environmentService.EnvironmentComponent;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.RootXRequestHandlerConfigurer;
import com.eltechs.axs.rendering.impl.virglRenderer.VirglRedererEngine;
import com.eltechs.axs.xconnectors.epoll.FairEpollConnector;
import com.eltechs.axs.xconnectors.epoll.UnixSocketConfiguration;
import com.eltechs.axs.xserver.DesktopExperience;
import com.eltechs.axs.xserver.KeyButNames;
import com.eltechs.axs.xserver.KeyboardLayout;
import com.eltechs.axs.xserver.KeyboardModel;
import com.eltechs.axs.xserver.KeyboardModifiersLayout;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.client.XClientConnectionHandler;
import com.eltechs.axs.xserver.impl.drawables.gl.GLDrawablesFactory;
import com.eltechs.axs.xserver.keysyms.FunctionKeysyms;
import com.eltechs.axs.xserver.keysyms.KeypadKeysyms;
import com.eltechs.axs.xserver.keysyms.ModifierKeysyms;
import com.eltechs.axs.xserver.keysyms.NavigationKeysyms;
import java.io.IOException;

public class XServerComponent extends EnvironmentComponent {
    private FairEpollConnector<XClient> connector;
    private DesktopExperience desktopExperience;
    private final int displayNumber;
    private final ScreenInfo screenInfo;
    private UnixSocketConfiguration socketConf;
    private XServer xServer;

    public XServerComponent(ScreenInfo screenInfo2, int i, UnixSocketConfiguration unixSocketConfiguration) {
        this.screenInfo = screenInfo2;
        this.displayNumber = i;
        this.socketConf = unixSocketConfiguration;
    }

    public void start() throws IOException {
        Assert.state(this.connector == null, "X-server component is already started.");
        this.xServer = createXServer(this.screenInfo, createKeyboardModel());
        this.desktopExperience = new DesktopExperienceImpl();
        this.desktopExperience.attachToXServer(this.xServer);
        startXConnector(this.xServer);
    }

    public void stop() {
        Assert.state(this.connector != null, "X-server component is not yet started.");
        try {
            this.connector.stop();
        } catch (IOException unused) {
        }
        this.xServer = null;
        this.desktopExperience = null;
        this.connector = null;
    }

    public ScreenInfo getScreenInfo() {
        return this.screenInfo;
    }

    public int getDisplayNumber() {
        return this.displayNumber;
    }

    public XServer getXServer() {
        Assert.state(this.connector != null, "X-server component is not yet started.");
        return this.xServer;
    }

    private XServer createXServer(ScreenInfo screenInfo2, KeyboardModel keyboardModel) {
        SysVIPCEmulatorComponent sysVIPCEmulatorComponent = (SysVIPCEmulatorComponent) getEnvironment().getComponent(SysVIPCEmulatorComponent.class);
        XServer xServer2 = new XServer(screenInfo2, keyboardModel, GLDrawablesFactory.create(screenInfo2.depth), sysVIPCEmulatorComponent != null ? sysVIPCEmulatorComponent.getShmEngine() : null, new VirglRedererEngine(), 512);
        return xServer2;
    }

    private void startXConnector(XServer xServer2) throws IOException {
        this.connector = FairEpollConnector.listenOnSpecifiedUnixSocket(this.socketConf, new XClientConnectionHandler(xServer2), RootXRequestHandlerConfigurer.createRequestHandler(xServer2));
        this.connector.setInitialInputBufferCapacity(262144);
        this.connector.start();
    }

    private static KeyboardModel createKeyboardModel() {
        KeyboardLayout keyboardLayout = new KeyboardLayout();
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_ESC.getValue(), FunctionKeysyms.ESC.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_RETURN.getValue(), FunctionKeysyms.RETURN.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_RIGHT.getValue(), NavigationKeysyms.RIGHT.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_UP.getValue(), NavigationKeysyms.UP.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_LEFT.getValue(), NavigationKeysyms.LEFT.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_DOWN.getValue(), NavigationKeysyms.DOWN.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_DELETE.getValue(), FunctionKeysyms.DELETE.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_BACKSPACE.getValue(), FunctionKeysyms.BACKSPACE.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_INSERT.getValue(), FunctionKeysyms.INSERT.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_PRIOR.getValue(), NavigationKeysyms.PRIOR.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_NEXT.getValue(), NavigationKeysyms.NEXT.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_HOME.getValue(), NavigationKeysyms.HOME.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_END.getValue(), NavigationKeysyms.END.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_SHIFT_LEFT.getValue(), ModifierKeysyms.SHIFT_L.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_SHIFT_RIGHT.getValue(), ModifierKeysyms.SHIFT_R.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_CONTROL_LEFT.getValue(), ModifierKeysyms.CONTROL_L.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_CONTROL_RIGHT.getValue(), ModifierKeysyms.CONTROL_R.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_ALT_LEFT.getValue(), ModifierKeysyms.ALT_L.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_ALT_RIGHT.getValue(), ModifierKeysyms.ALT_R.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_TAB.getValue(), FunctionKeysyms.TAB.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_SPACE.getValue(), 32, 32);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_A.getValue(), 97, 65);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_B.getValue(), 98, 66);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_C.getValue(), 99, 67);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_D.getValue(), 100, 68);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_E.getValue(), 101, 69);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F.getValue(), 102, 70);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_G.getValue(), 103, 71);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_H.getValue(), 104, 72);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_I.getValue(), 105, 73);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_J.getValue(), 106, 74);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_K.getValue(), 107, 75);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_L.getValue(), 108, 76);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_M.getValue(), 109, 77);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_N.getValue(), 110, 78);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_O.getValue(), 111, 79);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_P.getValue(), 112, 80);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_Q.getValue(), 113, 81);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_R.getValue(), 114, 82);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_S.getValue(), 115, 83);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_T.getValue(), 116, 84);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_U.getValue(), 117, 85);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_V.getValue(), 118, 86);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_W.getValue(), 119, 87);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_X.getValue(), 120, 88);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_Y.getValue(), 121, 89);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_Z.getValue(), 122, 90);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_1.getValue(), 49, 33);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_2.getValue(), 50, 64);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_3.getValue(), 51, 35);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_4.getValue(), 52, 36);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_5.getValue(), 53, 37);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_6.getValue(), 54, 94);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_7.getValue(), 55, 38);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_8.getValue(), 56, 42);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_9.getValue(), 57, 40);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_0.getValue(), 48, 41);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_COMMA.getValue(), 44, 60);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_PERIOD.getValue(), 46, 62);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_SEMICOLON.getValue(), 59, 58);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_APOSTROPHE.getValue(), 39, 34);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_BRACKET_LEFT.getValue(), 91, 123);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_BRACKET_RIGHT.getValue(), 93, 125);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_GRAVE.getValue(), 96, 126);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_MINUS.getValue(), 45, 95);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_EQUAL.getValue(), 61, 43);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_SLASH.getValue(), 47, 63);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_BACKSLASH.getValue(), 92, 124);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_DIV.getValue(), 47, 47);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_MULTIPLY.getValue(), 42, 42);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_SUB.getValue(), 45, 45);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_ADD.getValue(), 43, 43);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_0.getValue(), 48, 48);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_1.getValue(), 49, 49);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_2.getValue(), 50, 50);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_3.getValue(), 51, 51);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_4.getValue(), 52, 52);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_5.getValue(), 53, 53);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_6.getValue(), 54, 54);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_7.getValue(), 55, 55);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_8.getValue(), 56, 56);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_9.getValue(), 57, 57);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_KP_DEL.getValue(), KeypadKeysyms.KEYPAD_DEL.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F1.getValue(), FunctionKeysyms.F1.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F2.getValue(), FunctionKeysyms.F2.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F3.getValue(), FunctionKeysyms.F3.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F4.getValue(), FunctionKeysyms.F4.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F5.getValue(), FunctionKeysyms.F5.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F6.getValue(), FunctionKeysyms.F6.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F7.getValue(), FunctionKeysyms.F7.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F8.getValue(), FunctionKeysyms.F8.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F9.getValue(), FunctionKeysyms.F9.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F10.getValue(), FunctionKeysyms.F10.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F11.getValue(), FunctionKeysyms.F11.getKeysym(), 0);
        keyboardLayout.setKeysymMapping(KeyCodesX.KEY_F12.getValue(), FunctionKeysyms.F12.getKeysym(), 0);
        KeyboardModifiersLayout keyboardModifiersLayout = new KeyboardModifiersLayout();
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_SHIFT_LEFT.getValue(), KeyButNames.SHIFT);
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_SHIFT_RIGHT.getValue(), KeyButNames.SHIFT);
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_CONTROL_LEFT.getValue(), KeyButNames.CONTROL);
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_CONTROL_RIGHT.getValue(), KeyButNames.CONTROL);
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_ALT_LEFT.getValue(), KeyButNames.MOD1);
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_ALT_RIGHT.getValue(), KeyButNames.MOD1);
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_NUM_LOCK.getValue(), KeyButNames.MOD2);
        keyboardModifiersLayout.setKeycodeToModifier((byte) KeyCodesX.KEY_CAPS_LOCK.getValue(), KeyButNames.LOCK);
        keyboardModifiersLayout.setModifierSticky(KeyButNames.LOCK, true);
        keyboardModifiersLayout.setModifierSticky(KeyButNames.MOD2, true);
        return new KeyboardModel(keyboardModifiersLayout, keyboardLayout);
    }
}
