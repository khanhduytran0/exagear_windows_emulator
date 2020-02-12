package com.eltechs.axs.xserver.helpers;

import com.eltechs.axs.xserver.KeyButNames;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.impl.masks.Mask;

public class EventHelpers {
    public static Mask<KeyButNames> getKeyButMask(XServer xServer) {
        Mask<KeyButNames> mergeMasksOR = Mask.mergeMasksOR(xServer.getPointer().getButtonMask(), xServer.getKeyboard().getModifiersMask());
        KeyButNames.clearVirtualModifiers(mergeMasksOR);
        return mergeMasksOR;
    }

    public static Mask<KeyButNames> getKeyButMask(XServer xServer, Mask<KeyButNames> mask) {
        Mask<KeyButNames> mergeMasksOR = Mask.mergeMasksOR(xServer.getPointer().getButtonMask(), mask);
        KeyButNames.clearVirtualModifiers(mergeMasksOR);
        return mergeMasksOR;
    }
}
