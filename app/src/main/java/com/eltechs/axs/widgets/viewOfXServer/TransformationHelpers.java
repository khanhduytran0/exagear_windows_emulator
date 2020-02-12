/*
 * Decompiled with CFR 0.145.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 */
package com.eltechs.axs.widgets.viewOfXServer;

import android.graphics.Matrix;
import com.eltechs.axs.configuration.XServerViewConfiguration;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.widgets.viewOfXServer.TransformationDescription;

public class TransformationHelpers {
	/*
    public static TransformationDescription makeTransformationDescription(float f, float f2, float f3, float f4, float f5, float f6, XServerViewConfiguration.FitStyleHorizontal fitStyleHorizontal, XServerViewConfiguration.FitStyleVertical fitStyleVertical) {
        float f7 = f / f5;
        float f8 = f2 / f6;
        float f9 = Math.min(f7, f8);
        switch (fitStyleHorizontal) {
            default: {
                break;
            }
            case LEFT: 
            case CENTER: 
            case RIGHT: {
                f7 = f9;
            }
        }
        switch (fitStyleVertical) {
            default: {
                f9 = f8;
            }
            case TOP: 
            case CENTER: 
            case BOTTOM: 
        }
        f5 = f - f5 * f7;
        f6 = f2 - f6 * f9;
		// MOD: ??? Decompiled code:
        // int n = 1.$SwitchMap$com$eltechs$axs$configuration$XServerViewConfiguration$FitStyleHorizontal[fitStyleHorizontal.ordinal()];
		
		// MOD: Replaced by code below, but may not correctly.
        int n = fitStyleHorizontal.ordinal();
		f8 = 0.0f;
        f = f5;
        switch (n) {
            default: {
                Assert.unreachable();
            }
            case 1: 
            case 4: {
                f2 = 0.0f;
                break;
            }
            case 2: {
                f = f5 / 2.0f;
            }
            case 3: {
                f2 = f;
            }
        }
        f = f6;
        f5 = f8;
        switch (fitStyleVertical) {
            default: {
                Assert.unreachable();
                f5 = f8;
                break;
            }
            case CENTER: {
                f = f6 / 2.0f;
            }
            case BOTTOM: {
                f5 = f;
            }
            case TOP: 
            case STRETCH: 
        }
        return new TransformationDescription(f7, f9, -f3, -f4, f2, f5);
    }
*/
    public static TransformationDescription makeTransformationDescription(float totalWidth, float totalHeight, float subTopX, float subTopY, float subWidth, float subHeight, XServerViewConfiguration.FitStyleHorizontal fsh, XServerViewConfiguration.FitStyleVertical fsv) {
        float scaleX = totalWidth / subWidth;
        float scaleY = totalHeight / subHeight;
        float scaleMin = Math.min(scaleX, scaleY);
        float postTranslateX = 0.0f;
        float postTranslateY = 0.0f;
        switch (fsh) {
            case LEFT:
            case CENTER:
            case RIGHT:
                scaleX = scaleMin;
                break;
        }
        switch (fsv) {
            case TOP:
            case CENTER:
            case BOTTOM:
                scaleY = scaleMin;
                break;
        }
        float extraWidth = totalWidth - (scaleX * subWidth);
        float extraHeight = totalHeight - (scaleY * subHeight);
        switch (fsh) {
            case LEFT:
            case STRETCH:
                postTranslateX = 0.0f;
                break;
            case CENTER:
                postTranslateX = extraWidth / 2.0f;
                break;
            case RIGHT:
                postTranslateX = extraWidth;
                break;
            default:
                Assert.unreachable();
                break;
        }
        switch (fsv) {
            case TOP:
            case STRETCH:
                postTranslateY = 0.0f;
                break;
            case CENTER:
                postTranslateY = extraHeight / 2.0f;
                break;
            case BOTTOM:
                postTranslateY = extraHeight;
                break;
            default:
                Assert.unreachable();
                break;
        }
        return new TransformationDescription(scaleX, scaleY, -subTopX, -subTopY, postTranslateX, postTranslateY);
    }

    public static Matrix makeTransformationMatrix(float totalWidth, float totalHeight, float subTopX, float subTopY, float subWidth, float subHeight, XServerViewConfiguration.FitStyleHorizontal fsh, XServerViewConfiguration.FitStyleVertical fsv) {
        TransformationDescription td = makeTransformationDescription(totalWidth, totalHeight, subTopX, subTopY, subWidth, subHeight, fsh, fsv);
        Matrix matrix = new Matrix();
        matrix.postTranslate(td.xServerTranslateX, td.xServerTranslateY);
        matrix.postScale(td.scaleX, td.scaleY);
        matrix.postTranslate(td.viewTranslateX, td.viewTranslateY);
        return matrix;
    }

    public static void mapPoints(Matrix matrix, float[] pts) {
        boolean z;
        if (pts.length == 2) {
            z = true;
        } else {
            z = false;
        }
        Assert.state(z);
        float[] values = new float[9];
        matrix.getValues(values);
        float[] result = {0.0f, 0.0f, 0.0f};
        float[] ptMatrix = {pts[0], pts[1], 1.0f};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i] = result[i] + (values[(i * 3) + j] * ptMatrix[j]);
            }
        }
        pts[0] = result[0];
        pts[1] = result[1];
    }

    public static float getScaleX(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[0];
    }

    public static float getScaleY(Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[4];
    }
}

