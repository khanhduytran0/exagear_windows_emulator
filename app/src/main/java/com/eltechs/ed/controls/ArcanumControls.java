package com.eltechs.ed.controls;

import com.eltechs.axs.activities.XServerDisplayActivityInterfaceOverlay;
import com.eltechs.axs.gamesControls.ArcanumTouchScreenControlsFactory;
import com.eltechs.axs.gamesControls.BasicRolePlayingGamesUI;
import com.eltechs.ed.R;
import java.util.Arrays;
import java.util.List;

public class ArcanumControls extends Controls {
    public String getId() {
        return "arcanum";
    }

    public String getName() {
        return "Arcanum: Of Steamworks and Magick Obscura";
    }

    public List<ControlsInfoElem> getInfoElems() {
        return Arrays.asList(new ControlsInfoElem[]{new ControlsInfoElem(0, "Arcanum: Of Steamworks and Magick Obscura", "These controls are optimized for Arcanum: Of Steamworks and Magick Obscura. But you can also try them with other similar games."), new ControlsInfoElem(R.drawable.gesture_lclick, "Left Click", "Short tap"), new ControlsInfoElem(R.drawable.gesture_rclick_long, "Long Right Click", "Long tap"), new ControlsInfoElem(R.drawable.gesture_dnd_left, "Drag'n'Drop (Left Button)", "Slow finger move"), new ControlsInfoElem(R.drawable.gesture_move_cursor, "Move Cursor", "Fast finger move near cursor"), new ControlsInfoElem(R.drawable.gesture_scroll_mouse, "Scroll (Mouse)", "Fast finger move far from cursor"), new ControlsInfoElem(R.drawable.gesture_zoom, "Zoom", "Two fingers long tap & move"), new ControlsInfoElem(R.drawable.gesture_lclick_alt, "Alt + Left Click (To Cursor Pos)", "Two fingers tap"), new ControlsInfoElem(R.drawable.gesture_space, "Press Space", "Three fingers tap"), new ControlsInfoElem(R.drawable.gesture_menu, "App Menu", "Four fingers tap")});
    }

    public XServerDisplayActivityInterfaceOverlay create() {
        return new BasicRolePlayingGamesUI(new ArcanumTouchScreenControlsFactory());
    }
}
