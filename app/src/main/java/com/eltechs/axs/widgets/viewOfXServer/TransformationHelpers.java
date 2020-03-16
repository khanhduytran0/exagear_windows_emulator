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

