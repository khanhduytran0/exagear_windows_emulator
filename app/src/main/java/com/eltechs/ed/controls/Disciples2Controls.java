package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.gamesControls.BasicStrategiesUI;
import com.eltechs.axs.gamesControls.Disciples2TouchScreenControlsFactory;
import com.eltechs.ed.R;
import java.util.Arrays;
import java.util.List;

public class Disciples2Controls extends Controls {
    public String getId() {
        return "disciples2";
    }

    public String getName() {
        return "Disciples 2";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "Disciples 2", "These controls are optimized for Disciples 2. But you can also try them with other similar games."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click", "Short tap"), new ControlsInfoElem(R.drawable.gesture_rclick_long, "Long Right Click", "Long tap"), new ControlsInfoElem(R.drawable.gesture_dnd_left, "Drag'n'Drop (Left Button)", "Slow finger move"), new ControlsInfoElem(R.drawable.gesture_scroll_mouse, "Scroll (Mouse)", "Fast finger move"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_enter, "Press Enter", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_space, "Press Space", "Three fingers tap"), new ControlsInfoElem(R.drawable.gesture_menu, "App Menu", "Four fingers tap")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new BasicStrategiesUI(new Disciples2TouchScreenControlsFactory());
    }
}
