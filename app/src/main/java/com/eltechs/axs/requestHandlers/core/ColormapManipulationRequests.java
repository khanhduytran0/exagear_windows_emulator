package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xserver.Colormap;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;

public class ColormapManipulationRequests extends HandlerObjectBase {

    public enum Alloc {
        NONE,
        ALL
    }

    public ColormapManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 78)
    @Locks({"COLORMAPS_MANAGER", "WINDOWS_MANAGER"})
    public void CreateColormap(XClient xClient, @OOBParam @RequestParam Alloc alloc, @NewXId @RequestParam int i, @RequestParam Window window, @RequestParam int i2) throws XProtocolError {
        Colormap createColormap = this.xServer.getColormapsManager().createColormap(i);
        if (createColormap == null) {
            throw new BadIdChoice(i);
        }
        xClient.registerAsOwnerOfColormap(createColormap);
    }

    @RequestHandler(opcode = 79)
    @Locks({"COLORMAPS_MANAGER"})
    public void FreeColormap(XClient xClient, @RequestParam Colormap colormap) {
        this.xServer.getColormapsManager().freeColormap(colormap);
    }
}
