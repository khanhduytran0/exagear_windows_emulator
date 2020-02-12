package com.eltechs.axs.helpers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.eltechs.axs.Globals;

public class BitmapLoader {
    private final Options options;
    private final Resources resources;

    public BitmapLoader(Resources resources2) {
        this.options = new Options();
        this.options.inScaled = false;
        this.resources = resources2;
    }

    public static BitmapLoader createBitmapLoader() {
        return new BitmapLoader(Globals.getAppContext().getResources());
    }

    public static Bitmap loadOneBitmap(int i) {
        return createBitmapLoader().loadBitmap(i);
    }

    public Bitmap loadBitmap(int i) {
        return BitmapFactory.decodeResource(this.resources, i, this.options);
    }
}
