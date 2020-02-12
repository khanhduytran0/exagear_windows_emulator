package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.gamesControls.BasicRolePlayingGamesUI;
import com.eltechs.axs.gamesControls.JA2TouchScreenControlsFactory;
import com.eltechs.ed.R;
import java.util.Arrays;
import java.util.List;

public class JA2Controls extends Controls {
    public String getId() {
        return "ja2";
    }

    public String getName() {
        return "Jagged Alliance 2";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "Jagged Alliance 2", "These controls are optimized for Jagged Alliance 2. But you can also try them with other similar games."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click (To Cursor Pos)", "Short tap near cursor"), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click (To Finger Pos)", "Short tap far from cursor"), new ControlsInfoElem(R.drawable.gesture_rclick, "Right Click (To Cursor Pos)", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_rclick_long, "Long Right Click (To Finger Pos)", "Long tap"), new ControlsInfoElem(R.drawable.gesture_move_cursor, "Move Cursor", "Finger move near cursor"), new ControlsInfoElem(R.drawable.gesture_scroll_mouse, "Scroll (Mouse)", "Finger move far from cursor"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_menu, "App Menu", "Four fingers tap")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new BasicRolePlayingGamesUI(new JA2TouchScreenControlsFactory());
    }
}
