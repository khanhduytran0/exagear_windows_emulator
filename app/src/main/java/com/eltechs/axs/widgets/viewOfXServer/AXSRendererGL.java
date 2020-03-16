package com.eltechs.axs.widgets.viewOfXServer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import com.eltechs.axs.configuration.XServerViewConfiguration;
import com.eltechs.axs.geom.Point;
import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.geom.RectangleF;
import com.eltechs.axs.graphicsScene.SceneOfRectangles;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import com.eltechs.axs.xserver.LocksManager.XLock;
import com.eltechs.axs.xserver.PlacedDrawable;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.axs.xserver.ViewFacade;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowAttributeNames;
import com.eltechs.axs.xserver.impl.drawables.gl.PersistentGLDrawable;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AXSRendererGL implements Renderer {
    private boolean active;
    private boolean created = false;
    private PlacedDrawable cursorDrawable;
    private boolean freeze;
    private int glViewportHeight;
    private int glViewportWidth;
    final ViewOfXServer host;
    private final Bitmap invisibleCursorBitmap;
    private final Bitmap rootCursorBitmap;
    private int scHeight;
    private int scWidth;
    private int scX;
    private int scY;
    private SceneOfRectangles scene;
    private final ViewFacade viewFacade;
    private List<PersistentGLDrawable> windowDrawables;
    private RectangleF xViewport;

    public AXSRendererGL(ViewOfXServer viewOfXServer, ViewFacade viewFacade2) {
        this.host = viewOfXServer;
        this.viewFacade = viewFacade2;
        this.cursorDrawable = null;
        ScreenInfo screenInfo = viewFacade2.getScreenInfo();
        this.xViewport = new RectangleF(0.0f, 0.0f, (float) screenInfo.widthInPixels, (float) screenInfo.heightInPixels);
        this.rootCursorBitmap = createXCursorBitmap();
        this.invisibleCursorBitmap = createInvisibleCursorBitmap();
    }

    private Bitmap createXCursorBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(10, 10, Config.ARGB_8888);
        for (int i = 0; i < 10; i++) {
            createBitmap.setPixel(i, i, -1);
            createBitmap.setPixel(i, 9 - i, -1);
        }
        return createBitmap;
    }

    private Bitmap createInvisibleCursorBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
        createBitmap.setPixel(0, 0, 0);
        return createBitmap;
    }

    public synchronized void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(2884);
        GLES20.glEnable(2929);
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(770, 771);
        this.created = true;
        recreateScene();
    }

    public synchronized void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
        this.glViewportWidth = i;
        this.glViewportHeight = i2;
        this.active = true;
        updateSceneViewports();
        recreateSceneOfControls();
    }

    public synchronized void onPause() {
        this.active = false;
    }

    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(16640);
        synchronized (this) {
            XLock lock = this.viewFacade.getXServer().getLocksManager().lock(Subsystem.DRAWABLES_MANAGER);
            try {
                GLES20.glEnable(3089);
                GLES20.glScissor(this.scX, this.glViewportHeight - (this.scY + this.scHeight), this.scWidth, this.scHeight);
                if (!this.freeze) {
                    reloadWindowTextures(0);
                    reloadCursorTexture(this.windowDrawables.size());
                }
                this.scene.draw();
            } finally {
                GLES20.glDisable(3089);
                lock.close();
            }
        }
    }

    public synchronized void freeze() {
        this.freeze = true;
    }

    public synchronized void unFreeze() {
        this.freeze = false;
    }

    public synchronized void setXViewport(RectangleF rectangleF) {
        this.xViewport = rectangleF;
        updateSceneViewports();
    }

    private void updateSceneViewports() {
        if (this.scene != null) {
            XServerViewConfiguration configuration = getConfiguration();
            TransformationDescription makeTransformationDescription = TransformationHelpers.makeTransformationDescription((float) this.glViewportWidth, (float) this.glViewportHeight, this.xViewport.x, this.xViewport.y, this.xViewport.width, this.xViewport.height, configuration.getFitStyleHorizontal(), configuration.getFitStyleVertical());
            this.scene.setSceneViewport(this.xViewport.x, -this.xViewport.y, this.xViewport.width, this.xViewport.height);
            this.scene.setViewport(makeTransformationDescription.viewTranslateX / ((float) this.glViewportWidth), makeTransformationDescription.viewTranslateY / ((float) this.glViewportHeight), (this.xViewport.width * makeTransformationDescription.scaleX) / ((float) this.glViewportWidth), (this.xViewport.height * makeTransformationDescription.scaleY) / ((float) this.glViewportHeight));
            this.scX = (int) Math.ceil((double) ((makeTransformationDescription.xServerTranslateX * makeTransformationDescription.scaleX) + makeTransformationDescription.viewTranslateX));
            this.scY = (int) Math.ceil((double) ((makeTransformationDescription.xServerTranslateY * makeTransformationDescription.scaleY) + makeTransformationDescription.viewTranslateY));
            this.scWidth = (int) (((float) this.viewFacade.getScreenInfo().widthInPixels) * makeTransformationDescription.scaleX);
            this.scHeight = (int) (((float) this.viewFacade.getScreenInfo().heightInPixels) * makeTransformationDescription.scaleY);
        }
    }

    private void reloadWindowTextures(int i) {
        for (PersistentGLDrawable updateTextureFromDrawable : this.windowDrawables) {
            int i2 = i + 1;
            this.scene.updateTextureFromDrawable(i, updateTextureFromDrawable);
            i = i2;
        }
    }

    private void reloadCursorTexture(int i) {
        if (!getConfiguration().isCursorShowNeeded()) {
            this.scene.updateTextureFromBitmap(i, this.invisibleCursorBitmap);
        } else if (this.cursorDrawable == null) {
            this.scene.updateTextureFromBitmap(i, this.rootCursorBitmap);
        } else {
            this.scene.updateTextureFromDrawable(i, (PersistentGLDrawable) this.cursorDrawable.getDrawable());
        }
    }

    private void recreateScene() {
        recreateSceneOfXServer();
        recreateSceneOfControls();
    }

    private void placeCursor(int i) {
        this.cursorDrawable = this.viewFacade.getCursorDrawable();
        if (this.cursorDrawable == null) {
            Point pointerLocation = this.viewFacade.getPointerLocation();
            int width = pointerLocation.x - (this.rootCursorBitmap.getWidth() / 2);
            int height = pointerLocation.y - (this.rootCursorBitmap.getHeight() / 2);
            this.scene.setTextureSize(i, this.rootCursorBitmap.getWidth(), this.rootCursorBitmap.getHeight());
            this.scene.placeRectangle(i, (float) width, (float) (-height), (float) this.rootCursorBitmap.getWidth(), (float) this.rootCursorBitmap.getHeight(), 1.0f, i, 1.0f, false);
            return;
        }
        placeDrawable(i, 1, this.cursorDrawable, false);
    }

    private void placeDrawable(int i, int i2, PlacedDrawable placedDrawable, boolean z) {
        Rectangle location = placedDrawable.getLocation();
        int i3 = i;
        this.scene.setTextureSize(i3, location.width, location.height);
        this.scene.placeRectangle(i3, (float) location.x, (float) (-location.y), (float) location.width, (float) location.height, (float) i2, i3, 1.0f, z);
    }

    private void moveDrawable(int i, int i2, Rectangle rectangle) {
        this.scene.setTextureSize(i, rectangle.width, rectangle.height);
        this.scene.moveRectangle(i, (float) rectangle.x, (float) (-rectangle.y), (float) rectangle.width, (float) rectangle.height, (float) i2);
    }

    private void recreateSceneOfXServer() {
        if (this.created) {
            ArrayList listNonRootWindowDrawables = this.viewFacade.listNonRootWindowDrawables();
            int size = listNonRootWindowDrawables.size();
            int i = size + 1;
            int i2 = 0;
            this.windowDrawables = new ArrayList(size);
            setScene(new SceneOfRectangles(i, i));
            updateSceneViewports();
            while (i2 < size) {
                PlacedDrawable placedDrawable = (PlacedDrawable) listNonRootWindowDrawables.get(i2);
                this.windowDrawables.add((PersistentGLDrawable) placedDrawable.getDrawable());
                placeDrawable(i2, i - i2, placedDrawable, true);
                i2++;
            }
            placeCursor(i2);
        }
    }

    private void recreateSceneOfControls() {
        boolean z = this.active;
    }

    private void setScene(SceneOfRectangles sceneOfRectangles) {
        if (this.scene != null) {
            this.scene.destroy();
        }
        this.scene = sceneOfRectangles;
    }

    public synchronized void windowMapped(Window window) {
        recreateSceneOfXServer();
    }

    public synchronized void windowUnmapped(Window window) {
        recreateSceneOfXServer();
    }

    public synchronized void windowZOrderChanged(Window window) {
        recreateSceneOfXServer();
    }

    public synchronized void contentChanged(Window window, int i, int i2, int i3, int i4) {
    }

    public synchronized void cursorChanged() {
        if (this.windowDrawables != null) {
            placeCursor(this.windowDrawables.size());
        }
    }

    public synchronized void frontBufferReplaced(Window window) {
        recreateSceneOfXServer();
    }

    public synchronized void windowGeometryChanged(Window window) {
        if (this.windowDrawables != null) {
            int size = this.windowDrawables.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                } else if (this.windowDrawables.get(i) == window.getActiveBackingStore()) {
                    moveDrawable(i, (size - i) + 1, window.getBoundingRectangle());
                    break;
                } else {
                    i++;
                }
            }
        }
    }

    public synchronized void windowAttributesChanged(Window window, Mask<WindowAttributeNames> mask) {
        cursorChanged();
    }

    private XServerViewConfiguration getConfiguration() {
        return this.host.getConfiguration();
    }
}
