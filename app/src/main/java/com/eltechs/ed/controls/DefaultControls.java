package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.ed.R;
import com.eltechs.ed.controls.touchControls.DefaultTCF;
import com.eltechs.ed.controls.uiOverlays.DefaultUIOverlay;
import java.util.Arrays;
import java.util.List;

public class DefaultControls extends Controls {
    public String getId() {
        return "default";
    }

    public String getName() {
        return "Default";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "Default Controls", "These controls should be suitable for most regular (non-game) applications."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click", "Short tap"), new ControlsInfoElem(R.drawable.gesture_rclick, "Right Click", "Long tap & release"), new ControlsInfoElem(R.drawable.gesture_vscroll_wheel, "Vertical Scroll (Wheel)", "Short tap & move"), new ControlsInfoElem(R.drawable.gesture_dnd_left, "Drag'n'Drop (Left Button)", "Long tap & move"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_keyboard, "Toggle Keyboard", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_toolbar, "Toggle Toolbar", "Three fingers tap")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new DefaultUIOverlay(this, new DefaultTCF());
    }
}
