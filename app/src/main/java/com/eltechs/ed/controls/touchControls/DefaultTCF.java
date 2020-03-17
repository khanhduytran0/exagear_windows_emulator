package com.eltechs.ed.controls.touchControls;

import com.eltechs.axs.GestureStateMachine.GestureContext;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMeasureSpeed;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMoveToMouseDragAndDrop;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMoveToScrollSync;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerToZoomMove;
import com.eltechs.axs.GestureStateMachine.GestureState2FingersToZoom;
import com.eltechs.axs.GestureStateMachine.GestureStateClickToFingerFirstCoords;
import com.eltechs.axs.GestureStateMachine.GestureStateNeutral;
import com.eltechs.axs.GestureStateMachine.GestureStateWaitFingersNumberChangeWithTimeout;
import com.eltechs.axs.GestureStateMachine.GestureStateWaitForNeutral;
import com.eltechs.axs.GestureStateMachine.PointerContext;
import com.eltechs.axs.GuestAppActionAdapters.AlignedMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.MouseClickAdapterWithCheckPlacementContext;
import com.eltechs.axs.GuestAppActionAdapters.PressAndHoldMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.PressAndReleaseMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.ScrollAdapterMouseWheel;
import com.eltechs.axs.GuestAppActionAdapters.SimpleDragAndDropAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleMouseMoveAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleMousePointAndClickAdapter;
import com.eltechs.axs.TouchArea;
import com.eltechs.axs.TouchEventMultiplexor;
import com.eltechs.axs.finiteStateMachine.FiniteStateMachine;
import com.eltechs.axs.finiteStateMachine.generalStates.FSMStateRunRunnable;
import com.eltechs.axs.helpers.AndroidHelpers;
import com.eltechs.axs.widgets.viewOfXServer.TransformationHelpers;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;
import com.eltechs.ed.controls.uiOverlays.DefaultUIOverlay;

public class DefaultTCF extends AbstractTCF {
    private static final float clickAlignThresholdInches = 0.3f;
    private static final float doubleClickMaxDistanceInches = 0.15f;
    private static final int doubleClickMaxIntervalMs = 200;
    private static final float fingerStandingMaxMoveInches = 0.15f;
    private static final int fingerToLongTimeMs = 250;
    private static final int maxTapTimeMs = 100;
    private static final int mouseActionSleepMs = 50;
    private static final float pixelsInScrollUnit = 20.0f;
    private static final long scrollPeriodMs = 30;

    public GestureContext createGestureContext(ViewOfXServer viewOfXServer, TouchArea touchArea, TouchEventMultiplexor touchEventMultiplexor, int i) {
        ViewOfXServer viewOfXServer2 = viewOfXServer;
        final GestureContext gestureContext = new GestureContext(viewOfXServer2, touchArea, touchEventMultiplexor);
        PointerContext pointerContext = new PointerContext();
        GestureStateNeutral gestureStateNeutral = new GestureStateNeutral(gestureContext);
        GestureStateWaitForNeutral gestureStateWaitForNeutral = new GestureStateWaitForNeutral(gestureContext);
        float f = (float) i;
        float f2 = 0.15f * f;
        GestureState1FingerMeasureSpeed gestureState1FingerMeasureSpeed = new GestureState1FingerMeasureSpeed(gestureContext, 250, f2, f2, f2, 250.0f);
        SimpleMousePointAndClickAdapter simpleMousePointAndClickAdapter = new SimpleMousePointAndClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, 50), pointerContext);
        SimpleMousePointAndClickAdapter simpleMousePointAndClickAdapter2 = simpleMousePointAndClickAdapter;
        GestureState1FingerMeasureSpeed gestureState1FingerMeasureSpeed2 = gestureState1FingerMeasureSpeed;
        ViewOfXServer viewOfXServer3 = viewOfXServer2;
        PointerContext pointerContext2 = pointerContext;
        AlignedMouseClickAdapter alignedMouseClickAdapter = new AlignedMouseClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, 50), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, 50), viewOfXServer3, pointerContext2, f * clickAlignThresholdInches);
        AlignedMouseClickAdapter alignedMouseClickAdapter2 = new AlignedMouseClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, 50), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, 50), viewOfXServer3, pointerContext2, f2);
        MouseClickAdapterWithCheckPlacementContext mouseClickAdapterWithCheckPlacementContext = new MouseClickAdapterWithCheckPlacementContext(simpleMousePointAndClickAdapter2, alignedMouseClickAdapter, alignedMouseClickAdapter2, pointerContext, 200);
        GestureContext gestureContext2 = gestureContext;
        // GestureState1FingerMoveToScrollSync gestureState1FingerMoveToScrollSync = r1;
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords = new GestureStateClickToFingerFirstCoords(gestureContext, mouseClickAdapterWithCheckPlacementContext);
        GestureStateNeutral gestureStateNeutral2 = gestureStateNeutral;
        GestureState1FingerMeasureSpeed gestureState1FingerMeasureSpeed3 = gestureState1FingerMeasureSpeed2;
        GestureState1FingerMoveToScrollSync gestureState1FingerMoveToScrollSync = new GestureState1FingerMoveToScrollSync(gestureContext2, new ScrollAdapterMouseWheel(gestureContext.getPointerReporter()), 0.05f * TransformationHelpers.getScaleX(viewOfXServer.getViewToXServerTransformationMatrix()), 0.05f * TransformationHelpers.getScaleY(viewOfXServer.getViewToXServerTransformationMatrix()), 0.0f, false, 0, scrollPeriodMs, true);
        GestureState1FingerMeasureSpeed gestureState1FingerMeasureSpeed4 = new GestureState1FingerMeasureSpeed(gestureContext2, 1000000, f2, f2, f2, 1000000.0f);
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords2 = new GestureStateClickToFingerFirstCoords(gestureContext, new SimpleMousePointAndClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 3, 50), pointerContext));
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop = new GestureState1FingerMoveToMouseDragAndDrop(gestureContext, new SimpleDragAndDropAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndHoldMouseClickAdapter(gestureContext.getPointerReporter(), 1), new Runnable() {
            public void run() {
                gestureContext.getPointerReporter().click(3, 50);
            }
        }), pointerContext, false, 0.0f);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout = new GestureStateWaitFingersNumberChangeWithTimeout(gestureContext, 100);
        FSMStateRunRunnable fSMStateRunRunnable = new FSMStateRunRunnable(new Runnable() {
            public void run() {
                AndroidHelpers.toggleSoftInput();
            }
        });
        GestureState2FingersToZoom gestureState2FingersToZoom = new GestureState2FingersToZoom(gestureContext);
        GestureState1FingerToZoomMove gestureState1FingerToZoomMove = new GestureState1FingerToZoomMove(gestureContext);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout2 = new GestureStateWaitFingersNumberChangeWithTimeout(gestureContext, 100);
        FSMStateRunRunnable fSMStateRunRunnable2 = new FSMStateRunRunnable(new Runnable() {
            public void run() {
                ((DefaultUIOverlay) DefaultTCF.this.mUIOverlay).toggleToolbar();
            }
        });
        FiniteStateMachine finiteStateMachine = new FiniteStateMachine();
        GestureState1FingerMeasureSpeed gestureState1FingerMeasureSpeed5 = gestureState1FingerMeasureSpeed3;
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords3 = gestureStateClickToFingerFirstCoords;
        GestureContext gestureContext3 = gestureContext;
        GestureState1FingerMoveToScrollSync gestureState1FingerMoveToScrollSync3 = gestureState1FingerMoveToScrollSync;
        finiteStateMachine.setStatesList(gestureStateWaitForNeutral, gestureStateNeutral2, gestureState1FingerMeasureSpeed5, gestureStateClickToFingerFirstCoords3, gestureState1FingerMoveToScrollSync3, gestureState1FingerMeasureSpeed4, gestureStateClickToFingerFirstCoords2, gestureState1FingerMoveToMouseDragAndDrop, gestureStateWaitFingersNumberChangeWithTimeout, fSMStateRunRunnable, gestureState2FingersToZoom, gestureState1FingerToZoomMove, gestureStateWaitFingersNumberChangeWithTimeout2, fSMStateRunRunnable2);
        GestureStateNeutral gestureStateNeutral3 = gestureStateNeutral2;
        finiteStateMachine.addTransition(gestureStateWaitForNeutral, GestureStateWaitForNeutral.GESTURE_COMPLETED, gestureStateNeutral3);
        finiteStateMachine.addTransition(gestureStateNeutral3, GestureStateNeutral.FINGER_TOUCHED, gestureState1FingerMeasureSpeed5);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed5, GestureState1FingerMeasureSpeed.FINGER_TAPPED, gestureStateClickToFingerFirstCoords3);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed5, GestureState1FingerMeasureSpeed.FINGER_WALKED, gestureState1FingerMoveToScrollSync3);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed5, GestureState1FingerMeasureSpeed.FINGER_FLASHED, gestureState1FingerMoveToScrollSync3);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed5, GestureState1FingerMeasureSpeed.FINGER_STANDING, gestureState1FingerMeasureSpeed4);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed5, GestureState1FingerMeasureSpeed.FINGER_TOUCHED, gestureStateWaitFingersNumberChangeWithTimeout);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed4, GestureState1FingerMeasureSpeed.FINGER_TAPPED, gestureStateClickToFingerFirstCoords2);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed4, GestureState1FingerMeasureSpeed.FINGER_WALKED, gestureState1FingerMoveToMouseDragAndDrop);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed4, GestureState1FingerMeasureSpeed.FINGER_FLASHED, gestureState1FingerMoveToMouseDragAndDrop);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_RELEASED, fSMStateRunRunnable);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout, GestureStateWaitFingersNumberChangeWithTimeout.TIMED_OUT, gestureState2FingersToZoom);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_TOUCHED, gestureStateWaitFingersNumberChangeWithTimeout2);
        finiteStateMachine.addTransition(gestureState2FingersToZoom, GestureState2FingersToZoom.FINGER_RELEASED, gestureState1FingerToZoomMove);
        finiteStateMachine.addTransition(gestureState1FingerToZoomMove, GestureState1FingerToZoomMove.FINGER_TOUCHED, gestureState2FingersToZoom);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout2, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_RELEASED, fSMStateRunRunnable2);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout2, GestureStateWaitFingersNumberChangeWithTimeout.TIMED_OUT, fSMStateRunRunnable2);
        finiteStateMachine.setInitialState(gestureStateNeutral3);
        finiteStateMachine.setDefaultState(gestureStateWaitForNeutral);
        finiteStateMachine.configurationCompleted();
        GestureContext gestureContext4 = gestureContext3;
        gestureContext4.setMachine(finiteStateMachine);
        return gestureContext4;
    }
}
