package com.eltechs.axs.widgets.viewOfXServer;

import android.content.Context;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.eltechs.axs.configuration.XServerViewConfiguration;
import com.eltechs.axs.configuration.XServerViewConfiguration.FitStyleHorizontal;
import com.eltechs.axs.geom.RectangleF;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.PointerListener;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.axs.xserver.ViewFacade;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.WindowAttributeNames;
import com.eltechs.axs.xserver.WindowChangeListener;
import com.eltechs.axs.xserver.WindowContentModificationListener;
import com.eltechs.axs.xserver.WindowLifecycleListener;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class ViewOfXServer extends GLSurfaceView {
    private final XServerViewConfiguration configuration;
    private final WindowContentModificationListener contentModificationListener = new WindowContentModificationListener() {
        public void contentChanged(Window window, int i, int i2, int i3, int i4) {
            ViewOfXServer.this.queueWindowContentChanged(window, i, i2, i3, i4);
        }

        public void frontBufferReplaced(Window window) {
            ViewOfXServer.this.queueWindowBufferReplaced(window);
        }
    };
    private final PointerListener pointerListener = new PointerListener() {
        public void pointerButtonPressed(int i) {
        }

        public void pointerButtonReleased(int i) {
        }

        public void pointerMoved(int i, int i2) {
            ViewOfXServer.this.queueCursorPositionChanged();
        }

        public void pointerWarped(int i, int i2) {
            ViewOfXServer.this.queueCursorPositionChanged();
        }
    };
    /* access modifiers changed from: private */
    public final AXSRendererGL renderer;
    private Matrix transformationViewToXServer;
    private final WindowChangeListener windowChangeListener = new WindowChangeListener() {
        public void geometryChanged(Window window) {
            ViewOfXServer.this.queueWindowGeometryChanged(window);
        }

        public void attributesChanged(Window window, Mask<WindowAttributeNames> mask) {
            ViewOfXServer.this.queueWindowAttributesChanged(window, mask);
        }
    };
    private final WindowLifecycleListener windowLifecycleListener = new WindowLifecycleListener() {
        public void windowCreated(Window window) {
        }

        public void windowDestroyed(Window window) {
        }

        public void windowReparented(Window window, Window window2) {
        }

        public void windowMapped(Window window) {
            ViewOfXServer.this.queueWindowMapped(window);
        }

        public void windowUnmapped(Window window) {
            ViewOfXServer.this.queueWindowUnmapped(window);
        }

        public void windowZOrderChange(Window window) {
            ViewOfXServer.this.queueWindowZOrderChanged(window);
        }
    };
    private final ViewFacade xServerFacade;
    private XZoomController zoomController;

    /* access modifiers changed from: private */
    public void queueWindowGeometryChanged(final Window window) {
        queueEvent(new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.windowGeometryChanged(window);
            }
        });
        requestRender();
    }

    /* access modifiers changed from: private */
    public void queueWindowAttributesChanged(final Window window, final Mask<WindowAttributeNames> mask) {
        queueEvent(new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.windowAttributesChanged(window, mask);
            }
        });
    }

    /* access modifiers changed from: private */
    public void queueWindowMapped(final Window window) {
        queueEvent(new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.windowMapped(window);
            }
        });
    }

    /* access modifiers changed from: private */
    public void queueWindowUnmapped(final Window window) {
        queueEvent(new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.windowUnmapped(window);
            }
        });
        requestRender();
    }

    /* access modifiers changed from: private */
    public void queueWindowZOrderChanged(final Window window) {
        queueEvent(new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.windowZOrderChanged(window);
            }
        });
    }

    /* access modifiers changed from: private */
    public void queueWindowContentChanged(Window window, int i, int i2, int i3, int i4) {
        final Window window2 = window;
        final int i5 = i;
        final int i6 = i2;
        final int i7 = i3;
        final int i8 = i4;
        Runnable r0 = new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.contentChanged(window2, i5, i6, i7, i8);
            }
        };
        queueEvent(r0);
        requestRender();
    }

    /* access modifiers changed from: private */
    public void queueWindowBufferReplaced(final Window window) {
        queueEvent(new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.frontBufferReplaced(window);
            }
        });
    }

    /* access modifiers changed from: private */
    public void queueCursorPositionChanged() {
        queueEvent(new Runnable() {
            public void run() {
                ViewOfXServer.this.renderer.cursorChanged();
            }
        });
        requestRender();
    }

    public ViewOfXServer(Context context, XServer xServer, ViewFacade viewFacade, XServerViewConfiguration xServerViewConfiguration) {
        super(context);
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(1);
        this.configuration = xServerViewConfiguration;
        if (viewFacade == null) {
            this.xServerFacade = new ViewFacade(xServer);
        } else {
            this.xServerFacade = viewFacade;
        }
        this.renderer = new AXSRendererGL(this, this.xServerFacade);
        setRenderer(this.renderer);
        setRenderMode(0);
        this.transformationViewToXServer = new Matrix();
        this.zoomController = new XZoomController(this, xServer.getScreenInfo());
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public Matrix getViewToXServerTransformationMatrix() {
        Assert.notNull(this.transformationViewToXServer, "transformation matrix is not set");
        return this.transformationViewToXServer;
    }

    public Matrix getXServerToViewTransformationMatrix() {
        Matrix matrix = new Matrix();
        getViewToXServerTransformationMatrix().invert(matrix);
        return matrix;
    }

    public void setViewToXServerTransformationMatrix(Matrix matrix) {
        Assert.notNull(this.transformationViewToXServer, "transformation matrix is not set");
        this.transformationViewToXServer = matrix;
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        BaseInputConnection baseInputConnection = new BaseInputConnection(this, false);
        editorInfo.actionLabel = null;
        editorInfo.inputType = 0;
        editorInfo.imeOptions = 6;
        return baseInputConnection;
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        this.renderer.onPause();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.xServerFacade.addWindowLifecycleListener(this.windowLifecycleListener);
        this.xServerFacade.addWindowContentModificationListner(this.contentModificationListener);
        this.xServerFacade.addWindowChangeListener(this.windowChangeListener);
        this.xServerFacade.addPointerListener(this.pointerListener);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.xServerFacade.removeWindowLifecycleListener(this.windowLifecycleListener);
        this.xServerFacade.removeWindowContentModificationListner(this.contentModificationListener);
        this.xServerFacade.removeWindowChangeListener(this.windowChangeListener);
        this.xServerFacade.removePointerListener(this.pointerListener);
        super.onDetachedFromWindow();
    }

    private void reinitRenderTransformation() {
        ScreenInfo screenInfo = this.xServerFacade.getScreenInfo();
        TransformationHelpers.makeTransformationMatrix((float) getWidth(), (float) getHeight(), 0.0f, 0.0f, (float) screenInfo.widthInPixels, (float) screenInfo.heightInPixels, this.configuration.getFitStyleHorizontal(), this.configuration.getFitStyleVertical()).invert(this.transformationViewToXServer);
        this.zoomController = new XZoomController(this, this.xServerFacade.getScreenInfo());
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        reinitRenderTransformation();
    }

    public ViewFacade getXServerFacade() {
        return this.xServerFacade;
    }

    public void setXViewport(RectangleF rectangleF) {
        this.renderer.setXViewport(rectangleF);
        requestRender();
    }

    public XZoomController getZoomController() {
        Assert.state(this.zoomController != null, "Zoom controller is not initialized");
        return this.zoomController;
    }

    public XServerViewConfiguration getConfiguration() {
        return this.configuration;
    }

    public final boolean isHorizontalStretchEnabled() {
        return this.configuration.getFitStyleHorizontal() == FitStyleHorizontal.STRETCH;
    }

    public void setHorizontalStretchEnabled(boolean z) {
        if (z) {
            this.configuration.setFitStyleHorizontal(FitStyleHorizontal.STRETCH);
        } else {
            this.configuration.setFitStyleHorizontal(FitStyleHorizontal.CENTER);
        }
        if (!isDegenerate()) {
            reinitRenderTransformation();
            this.zoomController.revertZoom();
        }
    }

    private boolean isDegenerate() {
        return getWidth() == 0 || getHeight() == 0;
    }

    public void freezeRenderer() {
        if (this.renderer != null) {
            this.renderer.freeze();
        }
    }

    public void unfreezeRenderer() {
        if (this.renderer != null) {
            this.renderer.unFreeze();
        }
    }
}
