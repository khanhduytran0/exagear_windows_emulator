package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.gamesControls.BasicStrategiesUI;
import com.eltechs.axs.gamesControls.HoMM3TouchScreenControlsFactory;
import com.eltechs.ed.R;
import java.util.Arrays;
import java.util.List;

public class HoMM3Controls extends Controls {
    public String getId() {
        return "heroes3";
    }

    public String getName() {
        return "Heroes of Might and Magic 3";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "Heroes of Might and Magic 3", "These controls are optimized for Heroes of Might and Magic 3. But you can also try them with other similar games."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click", "Short tap"), new ControlsInfoElem(R.drawable.gesture_rclick_long, "Long Right Click", "Long tap"), new ControlsInfoElem(R.drawable.gesture_move_cursor, "Move Cursor", "Slow finger move"), new ControlsInfoElem(R.drawable.gesture_scroll_arrows_ctrl, "Scroll (Ctrl + Arrows)", "Fast finger move"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_enter, "Press Enter", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_space, "Press Space", "Three fingers tap"), new ControlsInfoElem(R.drawable.gesture_menu, "App Menu", "Four fingers tap")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new BasicStrategiesUI(new HoMM3TouchScreenControlsFactory());
    }
}
