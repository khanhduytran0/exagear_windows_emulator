package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.gamesControls.Civilization3InterfaceOverlay;
import com.eltechs.ed.R;
import java.util.Arrays;
import java.util.List;

public class Civ3Controls extends Controls {
    public String getId() {
        return "civilization3";
    }

    public String getName() {
        return "Civilization 3";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "Civilization 3", "These controls are optimized for Civilization 3. But you can also try them with other similar games."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click", "Short tap when [Mouse mode] is 'Left'"), new ControlsInfoElem(R.drawable.gesture_rclick, "Right Click", "Short tap when [Mouse mode] is 'Right'. [Mouse mode] resets to 'Left'."), new ControlsInfoElem(R.drawable.gesture_dnd_left, "Drag'n'Drop (Left Button)", "Slow finger tap & move when [Mouse mode] is 'Left'"), new ControlsInfoElem(R.drawable.gesture_move_cursor, "Move Cursor", "Slow finger move when [Mouse mode] is 'Right'"), new ControlsInfoElem(R.drawable.gesture_scroll_mouse, "Scroll (Mouse)", "Fast finger move"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_enter, "Press Enter", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_space, "Press Space", "Three fingers tap"), new ControlsInfoElem(R.drawable.gesture_menu, "App Menu", "Four fingers tap"), new ControlsInfoElem(0, "Left Toolbar Buttons", "Left toolbar contains:\n- 'Switch Mouse Mode' button (see below)\n- 'Toggle Shift' button\n- Some useful keys buttons"), new ControlsInfoElem(R.drawable.mouse_left, "Switch Mouse Mode", "Switches [Mouse Mode] between 'Left' and 'Right'")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new Civilization3InterfaceOverlay();
    }
}
