package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.ed.R;
import com.eltechs.ed.controls.touchControls.RtsTCF;
import com.eltechs.ed.controls.uiOverlays.RtsUIOverlay;
import java.util.Arrays;
import java.util.List;

public class RtsControls extends Controls {
    public String getId() {
        return "rts";
    }

    public String getName() {
        return "RTS";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "RTS Controls", "These controls should be suitable for most RTS (Run-Time Strategies) games."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click", "[Very] Short tap (<100ms)"), new ControlsInfoElem(R.drawable.gesture_rclick, "Right Click", "Long tap (>100ms) & release"), new ControlsInfoElem(R.drawable.gesture_dnd_left, "Drag'n'Drop (Left Button)", "While holding first finger tap with second finger, then move first finger"), new ControlsInfoElem(R.drawable.gesture_dnd_right, "Drag'n'Drop (Right Button)", "Very long tap & move"), new ControlsInfoElem(R.drawable.gesture_move_cursor, "Move cursor", "Slow finger move"), new ControlsInfoElem(R.drawable.gesture_scroll_mouse, "Scroll (Mouse)", "Fast finger move"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_keyboard, "Toggle Keyboard", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_toolbar, "Toggle Toolbar", "Three fingers tap")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new RtsUIOverlay(this, new RtsTCF());
    }
}
