package com.eltechs.axs.proto.output.replies;

import android.support.v4.view.ViewCompat;
import com.eltechs.axs.proto.output.POD;
import com.eltechs.axs.xserver.ScreenInfo;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

@POD({"root", "colormap", "whitePixel", "blackPixel", "currentInputMasks", "widthInPixels", "heightInPixels", "widthInMillimeters", "heightInMillimeters", "minInstalledMaps", "maxInstalledMaps", "rootVisual", "backingStores", "saveUnders", "rootDepth", "allowedDepthsCount", "allowedDepths"})
public class Screen {
    public final Depth[] allowedDepths;
    public final byte allowedDepthsCount;
    public final byte backingStores = 0;
    public final int blackPixel = 0;
    public final int colormap = 0;
    public final int currentInputMasks;
    public final short heightInMillimeters;
    public final short heightInPixels;
    public final short maxInstalledMaps = 1;
    public final short minInstalledMaps = 1;
    public final int root;
    public final byte rootDepth;
    public final int rootVisual;
    public final byte saveUnders = 0;
    public final int whitePixel = ViewCompat.MEASURED_SIZE_MASK;
    public final short widthInMillimeters;
    public final short widthInPixels;

    public Screen(XServer xServer) {
        int i = 0;
        Window rootWindow = xServer.getWindowsManager().getRootWindow();
        this.root = rootWindow.getId();
        this.currentInputMasks = rootWindow.getEventListenersList().calculateAllEventsMask().getRawMask();
        ScreenInfo screenInfo = xServer.getScreenInfo();
        this.widthInPixels = (short) screenInfo.widthInPixels;
        this.heightInPixels = (short) screenInfo.heightInPixels;
        this.widthInMillimeters = (short) screenInfo.widthInMillimeters;
        this.heightInMillimeters = (short) screenInfo.heightInMillimeters;
        TreeMap treeMap = new TreeMap();
        for (Visual visual : xServer.getDrawablesManager().getSupportedVisuals()) {
            Collection collection = (Collection) treeMap.get(Integer.valueOf(visual.getDepth()));
            if (collection == null) {
                collection = new LinkedList();
                treeMap.put(Integer.valueOf(visual.getDepth()), collection);
            }
            collection.add(visual);
        }
        this.allowedDepthsCount = (byte) treeMap.size();
        this.allowedDepths = new Depth[treeMap.size()];
        for (Entry entry : (Set<Entry>) treeMap.entrySet()) {
            int i2 = i + 1;
            this.allowedDepths[i] = new Depth(((Integer) entry.getKey()).intValue(), (Collection) entry.getValue());
            i = i2;
        }
        Visual visual2 = rootWindow.getActiveBackingStore().getVisual();
        this.rootVisual = visual2.getId();
        this.rootDepth = (byte) visual2.getDepth();
    }
}
