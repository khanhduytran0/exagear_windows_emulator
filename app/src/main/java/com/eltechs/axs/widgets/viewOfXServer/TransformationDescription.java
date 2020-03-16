package com.eltechs.axs.widgets.viewOfXServer;

public class TransformationDescription {
    final float scaleX;
    final float scaleY;
    final float viewTranslateX;
    final float viewTranslateY;
    final float xServerTranslateX;
    final float xServerTranslateY;

    public TransformationDescription(float f, float f2, float f3, float f4, float f5, float f6) {
        this.scaleX = f;
        this.scaleY = f2;
        this.xServerTranslateX = f3;
        this.xServerTranslateY = f4;
        this.viewTranslateX = f5;
        this.viewTranslateY = f6;
    }
}
