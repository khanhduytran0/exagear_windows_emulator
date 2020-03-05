package com.eltechs.axs.xserver.impl;

import android.util.ArrayMap;
import android.util.Log;

import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.DrawablesManager;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.impl.drawables.DrawablesFactory;
import com.eltechs.axs.xserver.impl.drawables.ImageFormat;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.util.Collection;
import java.util.Map;

public class DrawablesManagerImpl implements DrawablesManager {
    private final Map<Integer, Drawable> drawables = new ArrayMap<Integer, Drawable>();
    private final DrawablesFactory factory;

    public DrawablesManagerImpl(DrawablesFactory drawablesFactory) {
        this.factory = drawablesFactory;
    }

    public Drawable getDrawable(int i) {
        return this.drawables.get(i);
    }

    public Drawable createDrawable(int i, Window window, int i2, int i3, byte b) {
        Visual preferredVisualForDepth = this.factory.getPreferredVisualForDepth(ArithHelpers.extendAsUnsigned(b));
        if (preferredVisualForDepth == null) {
            Log.w("FIXME", "Code modified to bypass createDrawable(preferredVisualForDepth==null)");
            return null;
        }
        return createDrawable(i, window, i2, i3, preferredVisualForDepth);
    }

    public Drawable createDrawable(int i, Window window, int i2, int i3, Visual visual) {
        if (this.drawables.containsKey(i)) {
            return null;
        }
        Drawable create = this.factory.create(i, window, i2, i3, visual);
        this.drawables.put(i, create);
        return create;
    }

    public void removeDrawable(Drawable drawable) {
        this.drawables.remove(drawable.getId());
    }

    public Collection<Visual> getSupportedVisuals() {
        return this.factory.getSupportedVisuals();
    }

    public Visual getPreferredVisual() {
        return this.factory.getPreferredVisual();
    }

    public Visual getVisual(int i) {
        return this.factory.getVisual(i);
    }

    public Collection<ImageFormat> getSupportedImageFormats() {
        return this.factory.getSupportedImageFormats();
    }
}
