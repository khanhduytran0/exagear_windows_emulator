package com.eltechs.axs.xserver.impl.drawables.gl;

import android.support.v4.view.MotionEventCompat;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.impl.SmallIdsGenerator;
import com.eltechs.axs.xserver.impl.drawables.DrawablesFactoryImplBase;
import com.eltechs.axs.xserver.impl.drawables.ImageFormat;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collection;

public class GLDrawablesFactory extends DrawablesFactoryImplBase {
    private static final ReferenceQueue<Reference<Drawable>> finalisedDrawablesReferenceQueue = new ReferenceQueue<Reference<Drawable>>();
    private static final GLDrawablesFinaliserThread drawablesFinalisationThread = new GLDrawablesFinaliserThread(finalisedDrawablesReferenceQueue);

    static {
        drawablesFinalisationThread.start();
    }

    private GLDrawablesFactory(Collection<Visual> collection, Collection<ImageFormat> collection2, Visual visual) {
        super(collection, collection2, visual);
    }

    public static GLDrawablesFactory create32Depth() {
        Visual makeDisplayableVisual = Visual.makeDisplayableVisual(SmallIdsGenerator.generateId(), 32, 24, 16711680, 65280, 255);
        Collection<Visual> arrayList = new ArrayList<Visual>();
        arrayList.add(makeDisplayableVisual);
        arrayList.add(Visual.makeNonDisplayableVisual(SmallIdsGenerator.generateId(), 1));
        Collection<ImageFormat> arrayList2 = new ArrayList<ImageFormat>();
        arrayList2.add(new ImageFormat(1, 1, 32));
        arrayList2.add(new ImageFormat(24, 32, 32));
        arrayList2.add(new ImageFormat(32, 32, 32));
        return new GLDrawablesFactory(arrayList, arrayList2, makeDisplayableVisual);
    }

    public static GLDrawablesFactory create16Depth() {
        Visual makeDisplayableVisual = Visual.makeDisplayableVisual(SmallIdsGenerator.generateId(), 16, 16, 63488, 2016, 31);
        Collection<Visual> arrayList = new ArrayList<Visual>();
        arrayList.add(makeDisplayableVisual);
        arrayList.add(Visual.makeNonDisplayableVisual(SmallIdsGenerator.generateId(), 1));
        Collection<ImageFormat> arrayList2 = new ArrayList<ImageFormat>();
        arrayList2.add(new ImageFormat(1, 1, 16));
        arrayList2.add(new ImageFormat(16, 16, 16));
        return new GLDrawablesFactory(arrayList, arrayList2, makeDisplayableVisual);
    }

    public static GLDrawablesFactory create15Depth() {
        Visual makeDisplayableVisual = Visual.makeDisplayableVisual(SmallIdsGenerator.generateId(), 15, 16, 31744, 992, 31);
        Collection<Visual> arrayList = new ArrayList<Visual>();
        arrayList.add(makeDisplayableVisual);
        arrayList.add(Visual.makeNonDisplayableVisual(SmallIdsGenerator.generateId(), 1));
        Collection<ImageFormat> arrayList2 = new ArrayList<ImageFormat>();
        arrayList2.add(new ImageFormat(1, 1, 16));
        arrayList2.add(new ImageFormat(15, 16, 16));
        return new GLDrawablesFactory(arrayList, arrayList2, makeDisplayableVisual);
    }

    public static GLDrawablesFactory create(int i) {
        switch (i) {
            case 15:
                return create15Depth();
            case 16:
                return create16Depth();
            case 32:
                return create15Depth();
            default:
                Assert.isTrue(false, "Invalid BPP.");
                return null;
        }
    }

    public Drawable create(int i, Window window, int i2, int i3, Visual visual) {
        PersistentGLDrawable persistentGLDrawable = new PersistentGLDrawable(i, window, i2, i3, visual);
        drawablesFinalisationThread.registerFinalisationHandler(persistentGLDrawable, new PersistentGLDrawableDestroyer(persistentGLDrawable));
        return persistentGLDrawable;
    }
}
