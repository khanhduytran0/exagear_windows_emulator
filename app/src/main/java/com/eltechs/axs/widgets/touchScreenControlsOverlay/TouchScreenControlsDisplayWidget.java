package com.eltechs.axs.widgets.touchScreenControlsOverlay;

import android.opengl.GLSurfaceView;
import com.eltechs.axs.TouchScreenControls;
import com.eltechs.axs.activities.XServerDisplayActivity;

public class TouchScreenControlsDisplayWidget extends GLSurfaceView {
    private final TouchScreenControlsRenderer renderer = new TouchScreenControlsRenderer();

    public TouchScreenControlsDisplayWidget(XServerDisplayActivity xServerDisplayActivity) {
        super(xServerDisplayActivity);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(1);
        setRenderer(this.renderer);
        setRenderMode(1);
    }

    public void setTouchScreenControls(TouchScreenControls touchScreenControls) {
        this.renderer.setTouchScreenControls(touchScreenControls);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        this.renderer.onPause();
        super.onPause();
    }
}
