package com.eltechs.axs.widgets.helpers;

import android.app.Activity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;

public class ButtonHelpers {
    public static ImageButton createRegularImageButton(Activity activity, int i, int i2, int i3) {
        ImageButton imageButton = new ImageButton(activity);
        imageButton.setImageResource(i3);
        imageButton.setLayoutParams(new LayoutParams(i2, i));
        imageButton.setMinimumHeight(i);
        imageButton.setMaxWidth(i2);
        imageButton.setMaxHeight(i);
        imageButton.setScaleType(ScaleType.FIT_XY);
        return imageButton;
    }
}
