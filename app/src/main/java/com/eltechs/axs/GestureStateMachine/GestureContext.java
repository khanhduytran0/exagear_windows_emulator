package com.eltechs.axs.GestureStateMachine;

import com.eltechs.axs.Finger;
import com.eltechs.axs.KeyEventReporter;
import com.eltechs.axs.PointerEventReporter;
import com.eltechs.axs.TouchArea;
import com.eltechs.axs.TouchEventMultiplexor;
import com.eltechs.axs.finiteStateMachine.FiniteStateMachine;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.axs.widgets.viewOfXServer.XZoomController;
import com.eltechs.axs.xserver.ViewFacade;
import java.util.List;

public class GestureContext {
    private final ViewOfXServer host;
    private final KeyEventReporter keyboardReporter;
    private FiniteStateMachine machine;
    private final PointerEventReporter pointerEventReporter;
    private final TouchArea touchArea;
    private final TouchEventMultiplexor touchMultiplexor;

    public GestureContext(ViewOfXServer viewOfXServer, TouchArea touchArea2, TouchEventMultiplexor touchEventMultiplexor) {
        this.host = viewOfXServer;
        this.pointerEventReporter = new PointerEventReporter(viewOfXServer);
        this.keyboardReporter = new KeyEventReporter(viewOfXServer.getXServerFacade());
        this.touchArea = touchArea2;
        this.touchMultiplexor = touchEventMultiplexor;
    }

    public void setMachine(FiniteStateMachine finiteStateMachine) {
        this.machine = finiteStateMachine;
    }

    public FiniteStateMachine getMachine() {
        return this.machine;
    }

    public TouchEventMultiplexor getFingerEventsSource() {
        return this.touchMultiplexor;
    }

    public TouchArea getTouchArea() {
        return this.touchArea;
    }

    public PointerEventReporter getPointerReporter() {
        return this.pointerEventReporter;
    }

    public KeyEventReporter getKeyboardReporter() {
        return this.keyboardReporter;
    }

    public List<Finger> getFingers() {
        return this.touchArea.getFingers();
    }

    public ViewFacade getViewFacade() {
        return this.host.getXServerFacade();
    }

    public ViewOfXServer getHostView() {
        return this.host;
    }

    public XZoomController getZoomController() {
        return this.host.getZoomController();
    }
}
