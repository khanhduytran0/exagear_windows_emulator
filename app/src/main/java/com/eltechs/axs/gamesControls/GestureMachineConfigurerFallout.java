package com.eltechs.axs.gamesControls;

import com.eltechs.axs.GestureStateMachine.GestureContext;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMeasureSpeed;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMoveToMouseDragAndDrop;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMoveToScrollAsync;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerToZoomMove;
import com.eltechs.axs.GestureStateMachine.GestureState2FingersToZoom;
import com.eltechs.axs.GestureStateMachine.GestureState3FingersToZoom;
import com.eltechs.axs.GestureStateMachine.GestureStateCheckIfZoomed;
import com.eltechs.axs.GestureStateMachine.GestureStateClickToFingerFirstCoords;
import com.eltechs.axs.GestureStateMachine.GestureStateNeutral;
import com.eltechs.axs.GestureStateMachine.GestureStateWaitFingersNumberChangeWithTimeout;
import com.eltechs.axs.GestureStateMachine.GestureStateWaitForNeutral;
import com.eltechs.axs.GestureStateMachine.PointerContext;
import com.eltechs.axs.GuestAppActionAdapters.AlignedMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.AsyncScrollAdapterWithPointer;
import com.eltechs.axs.GuestAppActionAdapters.MouseClickAdapterWithCheckPlacementContext;
import com.eltechs.axs.GuestAppActionAdapters.PressAndHoldMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.PressAndHoldWithPauseMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.PressAndReleaseMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleDragAndDropAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleMouseMoveAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleMousePointAndClickAdapter;
import com.eltechs.axs.TouchArea;
import com.eltechs.axs.TouchEventMultiplexor;
import com.eltechs.axs.finiteStateMachine.FiniteStateMachine;
import com.eltechs.axs.finiteStateMachine.generalStates.FSMStateRunRunnable;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;

public class GestureMachineConfigurerFallout {
    private static final float clickAlignThresholdInches = 0.3f;
    private static final float doubleClickMaxDistance = 0.15f;
    private static final int doubleClickMaxIntervalMs = 200;
    private static final float fingerAimingMaxMoveInches = 0.2f;
    private static final int fingerSpeedCheckTimeMs = 400;
    private static final float fingerStandingMaxMoveInches = 0.12f;
    private static final float fingerTapMaxMoveInches = 0.2f;
    private static final int fingerTapMaxTimeMs = 400;
    private static final int maxTapTimeMs = 300;
    private static final int mouseActionSleepMs = 150;
    private static final float pixelsInScrollUnitX = 50.0f;
    private static final float pixelsInScrollUnitY = 50.0f;
    private static final int pointerMarginXPixels = 50;
    private static final long scrollTimerPeriod = 150;

    public static GestureContext createGestureContext(ViewOfXServer viewOfXServer, TouchArea touchArea, TouchEventMultiplexor touchEventMultiplexor, int i, Runnable runnable) {
        ViewOfXServer viewOfXServer2 = viewOfXServer;
        GestureContext gestureContext = new GestureContext(viewOfXServer2, touchArea, touchEventMultiplexor);
        PointerContext pointerContext = new PointerContext();
        GestureStateNeutral gestureStateNeutral = new GestureStateNeutral(gestureContext);
        GestureStateWaitForNeutral gestureStateWaitForNeutral = new GestureStateWaitForNeutral(gestureContext);
        float f = (float) i;
        float f2 = 0.2f * f;
        GestureState1FingerMeasureSpeed gestureState1FingerMeasureSpeed = new GestureState1FingerMeasureSpeed(gestureContext, 400, fingerStandingMaxMoveInches * f, f2, f2, 400.0f);
        GestureStateCheckIfZoomed gestureStateCheckIfZoomed = new GestureStateCheckIfZoomed(gestureContext);
        SimpleMousePointAndClickAdapter simpleMousePointAndClickAdapter = new SimpleMousePointAndClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), pointerContext);
        float f3 = clickAlignThresholdInches * f;
        GestureStateWaitForNeutral gestureStateWaitForNeutral2 = gestureStateWaitForNeutral;
        PointerContext pointerContext2 = pointerContext;
        AlignedMouseClickAdapter alignedMouseClickAdapter = new AlignedMouseClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), viewOfXServer2, pointerContext2, f3);
        float f4 = 0.15f * f;
        AlignedMouseClickAdapter alignedMouseClickAdapter2 = new AlignedMouseClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), viewOfXServer2, pointerContext2, f4);
        MouseClickAdapterWithCheckPlacementContext mouseClickAdapterWithCheckPlacementContext = new MouseClickAdapterWithCheckPlacementContext(simpleMousePointAndClickAdapter, alignedMouseClickAdapter, alignedMouseClickAdapter2, pointerContext, 200);
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords = new GestureStateClickToFingerFirstCoords(gestureContext, mouseClickAdapterWithCheckPlacementContext);
        SimpleMousePointAndClickAdapter simpleMousePointAndClickAdapter2 = new SimpleMousePointAndClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 3, mouseActionSleepMs), pointerContext);
        PointerContext pointerContext3 = pointerContext;
        AlignedMouseClickAdapter alignedMouseClickAdapter3 = new AlignedMouseClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 3, mouseActionSleepMs), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 3, mouseActionSleepMs), viewOfXServer2, pointerContext3, f3);
        AlignedMouseClickAdapter alignedMouseClickAdapter4 = new AlignedMouseClickAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 3, mouseActionSleepMs), new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 3, mouseActionSleepMs), viewOfXServer2, pointerContext3, f4);
        MouseClickAdapterWithCheckPlacementContext mouseClickAdapterWithCheckPlacementContext2 = new MouseClickAdapterWithCheckPlacementContext(simpleMousePointAndClickAdapter2, alignedMouseClickAdapter3, alignedMouseClickAdapter4, pointerContext, 200);
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords2 = new GestureStateClickToFingerFirstCoords(gestureContext, mouseClickAdapterWithCheckPlacementContext2);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout = new GestureStateWaitFingersNumberChangeWithTimeout(gestureContext, 1000000000);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout2 = new GestureStateWaitFingersNumberChangeWithTimeout(gestureContext, maxTapTimeMs);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout3 = new GestureStateWaitFingersNumberChangeWithTimeout(gestureContext, maxTapTimeMs);
        FSMStateRunRunnable fSMStateRunRunnable = new FSMStateRunRunnable(runnable);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout4 = gestureStateWaitFingersNumberChangeWithTimeout3;
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout5 = gestureStateWaitFingersNumberChangeWithTimeout2;
        SimpleDragAndDropAdapter simpleDragAndDropAdapter = new SimpleDragAndDropAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndHoldWithPauseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), new Runnable() {
            public void run() {
            }
        });
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop = new GestureState1FingerMoveToMouseDragAndDrop(gestureContext, simpleDragAndDropAdapter, pointerContext, false, 0.0f);
        FSMStateRunRunnable fSMStateRunRunnable2 = fSMStateRunRunnable;
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout6 = gestureStateWaitFingersNumberChangeWithTimeout4;
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout7 = gestureStateWaitFingersNumberChangeWithTimeout5;
        // GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop2 = 
		SimpleDragAndDropAdapter simpleDragAndDropAdapter2 = new SimpleDragAndDropAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), new PressAndHoldMouseClickAdapter(gestureContext.getPointerReporter(), 3), new Runnable() {
            public void run() {
            }
        });
        PointerContext pointerContext4 = pointerContext;
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop3 = new GestureState1FingerMoveToMouseDragAndDrop(gestureContext, simpleDragAndDropAdapter2, pointerContext4, false, 0.0f);
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop5 = gestureState1FingerMoveToMouseDragAndDrop3;
        // GestureState1FingerMoveToScrollAsync gestureState1FingerMoveToScrollAsync = r0;
        float f5 = f * 1.0f;
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout8 = gestureStateWaitFingersNumberChangeWithTimeout;
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords3 = gestureStateClickToFingerFirstCoords2;
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop6 = gestureState1FingerMoveToMouseDragAndDrop;
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop7 = gestureState1FingerMoveToMouseDragAndDrop5;
        GestureState1FingerMoveToScrollAsync gestureState1FingerMoveToScrollAsync = new GestureState1FingerMoveToScrollAsync(gestureContext, new AsyncScrollAdapterWithPointer(gestureContext.getViewFacade(), new Rectangle(0, 0, gestureContext.getViewFacade().getScreenInfo().widthInPixels, gestureContext.getViewFacade().getScreenInfo().heightInPixels)), 1000000.0f, 1000000.0f, f5, true, 15, true);
        GestureState1FingerToZoomMove gestureState1FingerToZoomMove = new GestureState1FingerToZoomMove(gestureContext);
        GestureState3FingersToZoom gestureState3FingersToZoom = new GestureState3FingersToZoom(gestureContext);
        GestureState2FingersToZoom gestureState2FingersToZoom = new GestureState2FingersToZoom(gestureContext);
        FiniteStateMachine finiteStateMachine = new FiniteStateMachine();
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout9 = gestureStateWaitFingersNumberChangeWithTimeout7;
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout10 = gestureStateWaitFingersNumberChangeWithTimeout6;
        FSMStateRunRunnable fSMStateRunRunnable3 = fSMStateRunRunnable2;
        GestureContext gestureContext2 = gestureContext;
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords4 = gestureStateClickToFingerFirstCoords3;
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout11 = gestureStateWaitFingersNumberChangeWithTimeout8;
        GestureState1FingerMoveToScrollAsync gestureState1FingerMoveToScrollAsync3 = gestureState1FingerMoveToScrollAsync;
        finiteStateMachine.setStatesList(gestureStateNeutral, gestureState1FingerMeasureSpeed, gestureStateClickToFingerFirstCoords, gestureStateCheckIfZoomed, gestureStateWaitFingersNumberChangeWithTimeout8, gestureStateWaitFingersNumberChangeWithTimeout9, gestureStateWaitFingersNumberChangeWithTimeout10, fSMStateRunRunnable3, gestureState1FingerMoveToMouseDragAndDrop7, gestureState1FingerMoveToMouseDragAndDrop6, gestureStateClickToFingerFirstCoords4, gestureState1FingerMoveToScrollAsync3, gestureState1FingerToZoomMove, gestureState2FingersToZoom, gestureState3FingersToZoom, gestureStateWaitForNeutral2);
        GestureStateWaitForNeutral gestureStateWaitForNeutral3 = gestureStateWaitForNeutral2;
        finiteStateMachine.addTransition(gestureStateWaitForNeutral3, GestureStateWaitForNeutral.GESTURE_COMPLETED, gestureStateNeutral);
        finiteStateMachine.addTransition(gestureStateNeutral, GestureStateNeutral.FINGER_TOUCHED, gestureState1FingerMeasureSpeed);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_FLASHED, gestureStateCheckIfZoomed);
        finiteStateMachine.addTransition(gestureStateCheckIfZoomed, GestureStateCheckIfZoomed.ZOOM_OFF, gestureState1FingerMoveToScrollAsync3);
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop8 = gestureState1FingerMoveToMouseDragAndDrop6;
        finiteStateMachine.addTransition(gestureStateCheckIfZoomed, GestureStateCheckIfZoomed.ZOOM_OFF, gestureState1FingerMoveToMouseDragAndDrop8);
        finiteStateMachine.addTransition(gestureStateCheckIfZoomed, GestureStateCheckIfZoomed.ZOOM_ON, gestureState1FingerToZoomMove);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_TOUCHED, gestureStateWaitFingersNumberChangeWithTimeout9);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout9, GestureStateWaitFingersNumberChangeWithTimeout.TIMED_OUT, gestureState1FingerMoveToMouseDragAndDrop7);
        finiteStateMachine.addTransition(gestureState1FingerToZoomMove, GestureState1FingerToZoomMove.FINGER_TOUCHED, gestureState2FingersToZoom);
        finiteStateMachine.addTransition(gestureState2FingersToZoom, GestureState2FingersToZoom.FINGER_TOUCHED, gestureState3FingersToZoom);
        finiteStateMachine.addTransition(gestureState2FingersToZoom, GestureState2FingersToZoom.FINGER_RELEASED, gestureState1FingerToZoomMove);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout9, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_TOUCHED, gestureStateWaitFingersNumberChangeWithTimeout10);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout10, GestureStateWaitFingersNumberChangeWithTimeout.TIMED_OUT, gestureState3FingersToZoom);
        finiteStateMachine.addTransition(gestureState3FingersToZoom, GestureState3FingersToZoom.FINGER_RELEASED, gestureState2FingersToZoom);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout10, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_TOUCHED, fSMStateRunRunnable3);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_TAPPED, gestureStateClickToFingerFirstCoords);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_WALKED, gestureState1FingerMoveToMouseDragAndDrop8);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_STANDING, gestureState1FingerMoveToMouseDragAndDrop8);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout9, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_RELEASED, gestureStateClickToFingerFirstCoords4);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout12 = gestureStateWaitFingersNumberChangeWithTimeout11;
        finiteStateMachine.addTransition(gestureStateClickToFingerFirstCoords4, GestureStateClickToFingerFirstCoords.GESTURE_COMPLETED, gestureStateWaitFingersNumberChangeWithTimeout12);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout12, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_TOUCHED, gestureStateWaitFingersNumberChangeWithTimeout9);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout12, GestureStateWaitFingersNumberChangeWithTimeout.TIMED_OUT, gestureStateWaitFingersNumberChangeWithTimeout12);
        finiteStateMachine.setInitialState(gestureStateNeutral);
        finiteStateMachine.setDefaultState(gestureStateWaitForNeutral3);
        finiteStateMachine.configurationCompleted();
        GestureContext gestureContext3 = gestureContext2;
        gestureContext3.setMachine(finiteStateMachine);
        return gestureContext3;
    }
}
