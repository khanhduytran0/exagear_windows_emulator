package com.eltechs.axs.gamesControls;

import com.eltechs.axs.GestureStateMachine.GestureContext;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMeasureSpeed;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMoveToMouseDragAndDrop;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerMoveToMouseMove;
import com.eltechs.axs.GestureStateMachine.GestureState1FingerToZoomMove;
import com.eltechs.axs.GestureStateMachine.GestureState2FingersToZoom;
import com.eltechs.axs.GestureStateMachine.GestureStateCheckIfZoomed;
import com.eltechs.axs.GestureStateMachine.GestureStateClickToFingerFirstCoords;
import com.eltechs.axs.GestureStateMachine.GestureStateNeutral;
import com.eltechs.axs.GestureStateMachine.GestureStatePressKey;
import com.eltechs.axs.GestureStateMachine.GestureStateWaitFingersNumberChangeWithTimeout;
import com.eltechs.axs.GestureStateMachine.GestureStateWaitForNeutral;
import com.eltechs.axs.GestureStateMachine.PointerContext;
import com.eltechs.axs.GuestAppActionAdapters.MouseMoveAdapter;
import com.eltechs.axs.GuestAppActionAdapters.PressAndHoldWithPauseMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.PressAndReleaseMouseClickAdapter;
import com.eltechs.axs.GuestAppActionAdapters.RelativeToCurrentPositionMouseMoveAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleDragAndDropAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleMouseMoveAdapter;
import com.eltechs.axs.GuestAppActionAdapters.SimpleMousePointAndClickAdapter;
import com.eltechs.axs.KeyCodesX;
import com.eltechs.axs.TouchArea;
import com.eltechs.axs.TouchEventMultiplexor;
import com.eltechs.axs.finiteStateMachine.FiniteStateMachine;
import com.eltechs.axs.finiteStateMachine.generalStates.FSMStateRunRunnable;
import com.eltechs.axs.widgets.viewOfXServer.ViewOfXServer;

public class GestureMachineConfigurerOxygen {
    private static final float dragNDropThresholdInches = 0.12f;
    private static final float fingerAimingMaxMoveInches = 0.51f;
    private static final int fingerSpeedCheckTimeMs = 200;
    private static final float fingerStandingMaxMoveInches = 0.06f;
    private static final float fingerTapMaxMoveInches = 0.2f;
    private static final int fingerTapMaxTimeMs = 200;
    private static final int maxTapTimeMs = 100;
    private static final int mouseActionSleepMs = 150;

    public static GestureContext createGestureContext(ViewOfXServer viewOfXServer, TouchArea touchArea, TouchEventMultiplexor touchEventMultiplexor, int i, Runnable runnable) {
        GestureContext gestureContext = new GestureContext(viewOfXServer, touchArea, touchEventMultiplexor);
        PointerContext pointerContext = new PointerContext();
        GestureStateNeutral gestureStateNeutral = new GestureStateNeutral(gestureContext);
        GestureStateWaitForNeutral gestureStateWaitForNeutral = new GestureStateWaitForNeutral(gestureContext);
        float f = (float) i;
        GestureState1FingerMeasureSpeed gestureState1FingerMeasureSpeed = new GestureState1FingerMeasureSpeed(gestureContext, 200, fingerStandingMaxMoveInches * f, fingerAimingMaxMoveInches * f, fingerTapMaxMoveInches * f, 200.0f);
        GestureStateCheckIfZoomed gestureStateCheckIfZoomed = new GestureStateCheckIfZoomed(gestureContext);
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords = new GestureStateClickToFingerFirstCoords(gestureContext, new SimpleMousePointAndClickAdapter(new MouseMoveAdapter() {
            public void moveTo(float f, float f2) {
            }

            public void prepareMoving(float f, float f2) {
            }
        }, new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), pointerContext));
        GestureStateClickToFingerFirstCoords gestureStateClickToFingerFirstCoords2 = new GestureStateClickToFingerFirstCoords(gestureContext, new SimpleMousePointAndClickAdapter(new MouseMoveAdapter() {
            public void moveTo(float f, float f2) {
            }

            public void prepareMoving(float f, float f2) {
            }
        }, new PressAndReleaseMouseClickAdapter(gestureContext.getPointerReporter(), 3, mouseActionSleepMs), pointerContext));
        float f2 = dragNDropThresholdInches * f;
        // GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop = r0;
        GestureState1FingerMoveToMouseDragAndDrop gestureState1FingerMoveToMouseDragAndDrop = new GestureState1FingerMoveToMouseDragAndDrop(gestureContext, new SimpleDragAndDropAdapter(new RelativeToCurrentPositionMouseMoveAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), gestureContext.getViewFacade(), gestureContext.getHostView()), new PressAndHoldWithPauseMouseClickAdapter(gestureContext.getPointerReporter(), 1, mouseActionSleepMs), new Runnable() {
            public void run() {
            }
        }), pointerContext, true, f2);
        GestureState1FingerMoveToMouseMove gestureState1FingerMoveToMouseMove = new GestureState1FingerMoveToMouseMove(gestureContext, pointerContext, new RelativeToCurrentPositionMouseMoveAdapter(new SimpleMouseMoveAdapter(gestureContext.getPointerReporter()), gestureContext.getViewFacade(), gestureContext.getHostView()));
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout = new GestureStateWaitFingersNumberChangeWithTimeout(gestureContext, 100);
        GestureStateWaitFingersNumberChangeWithTimeout gestureStateWaitFingersNumberChangeWithTimeout2 = new GestureStateWaitFingersNumberChangeWithTimeout(gestureContext, 100);
        FSMStateRunRunnable fSMStateRunRunnable = new FSMStateRunRunnable(runnable);
        GestureStatePressKey gestureStatePressKey = new GestureStatePressKey(gestureContext, KeyCodesX.KEY_SPACE);
        GestureState1FingerToZoomMove gestureState1FingerToZoomMove = new GestureState1FingerToZoomMove(gestureContext);
        GestureState2FingersToZoom gestureState2FingersToZoom = new GestureState2FingersToZoom(gestureContext);
        FiniteStateMachine finiteStateMachine = new FiniteStateMachine();
        GestureContext gestureContext2 = gestureContext;
        finiteStateMachine.setStatesList(gestureStateNeutral, gestureState1FingerMeasureSpeed, gestureStateClickToFingerFirstCoords, gestureStateClickToFingerFirstCoords2, gestureState1FingerMoveToMouseDragAndDrop, gestureState1FingerMoveToMouseMove, gestureStateCheckIfZoomed, gestureStateWaitFingersNumberChangeWithTimeout, gestureStateWaitFingersNumberChangeWithTimeout2, fSMStateRunRunnable, gestureStatePressKey, gestureState1FingerToZoomMove, gestureState2FingersToZoom, gestureStateWaitForNeutral);
        finiteStateMachine.addTransition(gestureStateWaitForNeutral, GestureStateWaitForNeutral.GESTURE_COMPLETED, gestureStateNeutral);
        finiteStateMachine.addTransition(gestureStateNeutral, GestureStateNeutral.FINGER_TOUCHED, gestureState1FingerMeasureSpeed);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_TAPPED, gestureStateClickToFingerFirstCoords);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_STANDING, gestureState1FingerMoveToMouseDragAndDrop);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_WALKED, gestureState1FingerMoveToMouseMove);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_FLASHED, gestureStateCheckIfZoomed);
        finiteStateMachine.addTransition(gestureState1FingerMeasureSpeed, GestureState1FingerMeasureSpeed.FINGER_TOUCHED, gestureStateWaitFingersNumberChangeWithTimeout);
        finiteStateMachine.addTransition(gestureStateCheckIfZoomed, GestureStateCheckIfZoomed.ZOOM_OFF, gestureState1FingerMoveToMouseMove);
        finiteStateMachine.addTransition(gestureStateCheckIfZoomed, GestureStateCheckIfZoomed.ZOOM_ON, gestureState1FingerToZoomMove);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_TOUCHED, gestureStateWaitFingersNumberChangeWithTimeout2);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_RELEASED, gestureStateClickToFingerFirstCoords2);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout, GestureStateWaitFingersNumberChangeWithTimeout.TIMED_OUT, gestureState2FingersToZoom);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout2, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_RELEASED, gestureStatePressKey);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout2, GestureStateWaitFingersNumberChangeWithTimeout.TIMED_OUT, gestureStatePressKey);
        finiteStateMachine.addTransition(gestureStateWaitFingersNumberChangeWithTimeout2, GestureStateWaitFingersNumberChangeWithTimeout.FINGER_TOUCHED, fSMStateRunRunnable);
        finiteStateMachine.addTransition(gestureState2FingersToZoom, GestureState2FingersToZoom.FINGER_RELEASED, gestureState1FingerToZoomMove);
        finiteStateMachine.addTransition(gestureState1FingerToZoomMove, GestureState1FingerToZoomMove.FINGER_TOUCHED, gestureState2FingersToZoom);
        finiteStateMachine.setInitialState(gestureStateNeutral);
        finiteStateMachine.setDefaultState(gestureStateWaitForNeutral);
        finiteStateMachine.configurationCompleted();
        GestureContext gestureContext3 = gestureContext2;
        gestureContext3.setMachine(finiteStateMachine);
        return gestureContext3;
    }
}
