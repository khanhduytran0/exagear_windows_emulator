package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.proto.input.annotations.Unsigned;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.CursorsManager;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;

public class CursorManipulationRequests extends HandlerObjectBase {
    public CursorManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 93)
    @Locks({"CURSORS_MANAGER", "PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
    public void CreateCursor(XClient xClient, @NewXId @RequestParam int i, @RequestParam Pixmap pixmap, @RequestParam @SpecialNullValue(0) Pixmap pixmap2, @RequestParam @Width(2) @Unsigned int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam @Width(2) @Unsigned int i4, @RequestParam @Width(2) @Unsigned int i5, @RequestParam @Width(2) @Unsigned int i6, @RequestParam @Width(2) @Unsigned int i7, @RequestParam @Width(2) @Unsigned int i8, @RequestParam @Width(2) @Unsigned int i9) throws XProtocolError {
        Drawable backingStore = pixmap.getBackingStore();
        if (pixmap2 != null) {
            Drawable backingStore2 = pixmap2.getBackingStore();
            if (!(backingStore2.getVisual().getDepth() == 1 && backingStore2.getWidth() == backingStore.getWidth() && backingStore2.getHeight() == backingStore.getHeight())) {
                throw new BadMatch();
            }
        }
        int i10 = i8;
        if (i10 <= backingStore.getWidth()) {
            int i11 = i9;
            if (i11 <= backingStore.getHeight()) {
                CursorsManager cursorsManager = this.xServer.getCursorsManager();
                Cursor createCursor = cursorsManager.createCursor(i, i10, i11, pixmap, pixmap2);
                if (createCursor == null) {
                    throw new BadIdChoice(i);
                }
                cursorsManager.recolorCursor(createCursor, i2, i3, i4, i5, i6, i7);
                xClient.registerAsOwnerOfCursor(createCursor);
                return;
            }
        }
        throw new BadMatch();
    }

    @RequestHandler(opcode = 94)
    @Locks({"CURSORS_MANAGER", "PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
    public void CreateGlyphCursor(XClient xClient, @NewXId @RequestParam int i, @RequestParam Integer num, @RequestParam Integer num2, @RequestParam @Width(2) @Unsigned int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam @Width(2) @Unsigned int i4, @RequestParam @Width(2) @Unsigned int i5, @RequestParam @Width(2) @Unsigned int i6, @RequestParam @Width(2) @Unsigned int i7, @RequestParam @Width(2) @Unsigned int i8, @RequestParam @Width(2) @Unsigned int i9) throws XProtocolError {
        int i10 = i;
        CursorsManager cursorsManager = this.xServer.getCursorsManager();
        Cursor createFakeCursor = cursorsManager.createFakeCursor(i10);
        if (createFakeCursor == null) {
            throw new BadIdChoice(i10);
        }
        cursorsManager.recolorCursor(createFakeCursor, i4, i5, i6, i7, i8, i9);
        xClient.registerAsOwnerOfCursor(createFakeCursor);
    }

    @RequestHandler(opcode = 95)
    @Locks({"CURSORS_MANAGER", "PIXMAPS_MANAGER", "DRAWABLES_MANAGER"})
    public void FreeCursor(XClient xClient, @RequestParam Cursor cursor) {
        this.xServer.getCursorsManager().freeCursor(cursor);
    }

    @RequestHandler(opcode = 96)
    @Locks({"CURSORS_MANAGER", "DRAWABLES_MANAGER"})
    public void RecolorCursor(@RequestParam Cursor cursor, @RequestParam @Width(2) @Unsigned int i, @RequestParam @Width(2) @Unsigned int i2, @RequestParam @Width(2) @Unsigned int i3, @RequestParam @Width(2) @Unsigned int i4, @RequestParam @Width(2) @Unsigned int i5, @RequestParam @Width(2) @Unsigned int i6) {
        this.xServer.getCursorsManager().recolorCursor(cursor, i, i2, i3, i4, i5, i6);
    }
}
