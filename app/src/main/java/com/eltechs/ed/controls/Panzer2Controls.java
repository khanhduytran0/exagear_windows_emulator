package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.gamesControls.BasicStrategiesUI;
import com.eltechs.axs.gamesControls.Disciples2TouchScreenControlsFactory;
import com.eltechs.ed.R;
import java.util.Arrays;
import java.util.List;

public class Panzer2Controls extends Controls {
    public String getId() {
        return "panzer2";
    }

    public String getName() {
        return "Panzer General 2";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "Panzer General 2", "These controls are optimized for Panzer General 2. But you can also try them with other similar games."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click", "Short tap"), new ControlsInfoElem(R.drawable.gesture_rclick_long, "Long Right Click", "Long tap"), new ControlsInfoElem(R.drawable.gesture_move_cursor, "Move Cursor", "Slow finger move"), new ControlsInfoElem(R.drawable.gesture_scroll_mouse, "Scroll (Mouse)", "Fast finger move"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_enter, "Press Enter", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_space, "Press Space", "Three fingers tap"), new ControlsInfoElem(R.drawable.gesture_menu, "App Menu", "Four fingers tap")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new BasicStrategiesUI(new Disciples2TouchScreenControlsFactory());
    }
}
