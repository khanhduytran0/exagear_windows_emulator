package com.eltechs.axs.proto.output.replies;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.output.POD;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.util.ArrayList;
import java.util.Collection;

@POD({"depth", "unused0", "numberOfVisuals", "unused1", "visuals"})
public class Depth {
    public final byte depth;
    public final short numberOfVisuals;
    public final byte unused0 = 0;
    public final int unused1 = 0;
    public final VisualType[] visuals;

    public Depth(int i, Collection<Visual> collection) {
        Collection<Visual> displayableVisuals = getDisplayableVisuals(collection);
        this.depth = (byte) i;
        this.numberOfVisuals = (short) ((byte) displayableVisuals.size());
        this.visuals = new VisualType[displayableVisuals.size()];
        int i2 = 0;
        for (Visual visual : displayableVisuals) {
            Assert.isTrue(visual.getDepth() == i, String.format("Visuals associated with a depth descriptor %d need to have the same depth, but %s has the depth %d.", new Object[]{Integer.valueOf(i), visual, Integer.valueOf(visual.getDepth())}));
            int i3 = i2 + 1;
            this.visuals[i2] = new VisualType(visual);
            i2 = i3;
        }
    }

    private Collection<Visual> getDisplayableVisuals(Collection<Visual> collection) {
        ArrayList arrayList = new ArrayList();
        for (Visual visual : collection) {
            if (visual.isDisplayable()) {
                arrayList.add(visual);
            }
        }
        return arrayList;
    }
}
