package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.NewXId;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.Optional;
import com.eltechs.axs.proto.input.annotations.ParamName;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.SpecialNullValue;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadIdChoice;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.GraphicsContextsManager;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.graphicsContext.ArcMode;
import com.eltechs.axs.xserver.graphicsContext.FillRule;
import com.eltechs.axs.xserver.graphicsContext.FillStyle;
import com.eltechs.axs.xserver.graphicsContext.GraphicsContextParts;
import com.eltechs.axs.xserver.graphicsContext.JoinStyle;
import com.eltechs.axs.xserver.graphicsContext.LineStyle;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import com.eltechs.axs.xserver.graphicsContext.SubwindowMode;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GraphicsContextManipulationRequests extends HandlerObjectBase {

    public enum ClipRectanglesOrdering {
        UNSORTED,
        Y_SORTED,
        YX_SORTED,
        YX_BANDED
    }

    @RequestHandler(opcode = 59)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER"})
    public void SetClipRectangles(@OOBParam @RequestParam ClipRectanglesOrdering clipRectanglesOrdering, @RequestParam GraphicsContext graphicsContext, @RequestParam short s, @RequestParam short s2, @RequestParam ByteBuffer byteBuffer) {
    }

    @RequestHandler(opcode = 58)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER"})
    public void SetDashes(@RequestParam GraphicsContext graphicsContext, @RequestParam short s, @RequestParam short s2, @RequestParam ByteBuffer byteBuffer) {
    }

    public GraphicsContextManipulationRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 55)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER", "DRAWABLES_MANAGER", "PIXMAPS_MANAGER"})
    public void CreateGC(XClient xClient, XResponse xResponse, @NewXId @RequestParam int i, @RequestParam Drawable drawable, @RequestParam @ParamName("mask") Mask<GraphicsContextParts> mask, @RequestParam @Width(4) @Optional(bit = "FUNCTION") PixelCompositionRule pixelCompositionRule, @RequestParam @Optional(bit = "PLANE_MASK") Integer num, @RequestParam @Optional(bit = "FOREGROUND") Integer num2, @RequestParam @Optional(bit = "BACKGROUND") Integer num3, @RequestParam @Optional(bit = "LINE_WIDTH") Integer num4, @RequestParam @Width(4) @Optional(bit = "LINE_STYLE") LineStyle lineStyle, @RequestParam @Optional(bit = "CAP_STYLE") Integer num5, @RequestParam @Width(4) @Optional(bit = "JOIN_STYLE") JoinStyle joinStyle, @RequestParam @Width(4) @Optional(bit = "FILL_STYLE") FillStyle fillStyle, @RequestParam @Width(4) @Optional(bit = "FILL_RULE") FillRule fillRule, @RequestParam @Optional(bit = "TILE") Pixmap pixmap, @RequestParam @Optional(bit = "STIPPLE") Pixmap pixmap2, @RequestParam @Optional(bit = "TILE_STIPPLE_X_ORIGIN") Integer num6, @RequestParam @Optional(bit = "TILE_STIPPLE_Y_ORIGIN") Integer num7, @RequestParam @Optional(bit = "FONT") Integer num8, @RequestParam @Width(4) @Optional(bit = "SUBWINDOW_MODE") SubwindowMode subwindowMode, @RequestParam @Width(4) @Optional(bit = "GRAPHICS_EXPOSURES") Boolean bool, @RequestParam @Optional(bit = "CLIP_X_ORIGIN") Integer num9, @RequestParam @Optional(bit = "CLIP_Y_ORIGIN") Integer num10, @RequestParam @SpecialNullValue(0) @Optional(bit = "CLIP_MASK") Pixmap pixmap3, @RequestParam @Optional(bit = "DASH_OFFSET") Integer num11, @RequestParam @Optional(bit = "DASHES") Integer num12, @RequestParam @Width(4) @Optional(bit = "ARC_MODE") ArcMode arcMode) throws IOException, XProtocolError {
        int i2 = i;
        GraphicsContextsManager graphicsContextsManager = this.xServer.getGraphicsContextsManager();
        GraphicsContext createGraphicsContext = graphicsContextsManager.createGraphicsContext(i2, drawable);
        if (createGraphicsContext == null) {
            throw new BadIdChoice(i2);
        }
        xClient.registerAsOwnerOfGraphicsContext(createGraphicsContext);
        graphicsContextsManager.updateGraphicsContext(createGraphicsContext, mask, pixelCompositionRule, num, num2, num3, num4, lineStyle, num5, joinStyle, fillStyle, fillRule, pixmap, pixmap2, num6, num7, num8, subwindowMode, bool, num9, num10, pixmap3, num11, num12, arcMode);
    }

    @RequestHandler(opcode = 56)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER", "PIXMAPS_MANAGER"})
    public void ChangeGC(@RequestParam GraphicsContext graphicsContext, @RequestParam @ParamName("mask") Mask<GraphicsContextParts> mask, @RequestParam @Width(4) @Optional(bit = "FUNCTION") PixelCompositionRule pixelCompositionRule, @RequestParam @Optional(bit = "PLANE_MASK") Integer num, @RequestParam @Optional(bit = "FOREGROUND") Integer num2, @RequestParam @Optional(bit = "BACKGROUND") Integer num3, @RequestParam @Optional(bit = "LINE_WIDTH") Integer num4, @RequestParam @Width(4) @Optional(bit = "LINE_STYLE") LineStyle lineStyle, @RequestParam @Optional(bit = "CAP_STYLE") Integer num5, @RequestParam @Width(4) @Optional(bit = "JOIN_STYLE") JoinStyle joinStyle, @RequestParam @Width(4) @Optional(bit = "FILL_STYLE") FillStyle fillStyle, @RequestParam @Width(4) @Optional(bit = "FILL_RULE") FillRule fillRule, @RequestParam @Optional(bit = "TILE") Pixmap pixmap, @RequestParam @Optional(bit = "STIPPLE") Pixmap pixmap2, @RequestParam @Optional(bit = "TILE_STIPPLE_X_ORIGIN") Integer num6, @RequestParam @Optional(bit = "TILE_STIPPLE_Y_ORIGIN") Integer num7, @RequestParam @Optional(bit = "FONT") Integer num8, @RequestParam @Width(4) @Optional(bit = "SUBWINDOW_MODE") SubwindowMode subwindowMode, @RequestParam @Width(4) @Optional(bit = "GRAPHICS_EXPOSURES") Boolean bool, @RequestParam @Optional(bit = "CLIP_X_ORIGIN") Integer num9, @RequestParam @Optional(bit = "CLIP_Y_ORIGIN") Integer num10, @RequestParam @SpecialNullValue(0) @Optional(bit = "CLIP_MASK") Pixmap pixmap3, @RequestParam @Optional(bit = "DASH_OFFSET") Integer num11, @RequestParam @Optional(bit = "DASHES") Integer num12, @RequestParam @Width(4) @Optional(bit = "ARC_MODE") ArcMode arcMode) throws IOException, XProtocolError {
        this.xServer.getGraphicsContextsManager().updateGraphicsContext(graphicsContext, mask, pixelCompositionRule, num, num2, num3, num4, lineStyle, num5, joinStyle, fillStyle, fillRule, pixmap, pixmap2, num6, num7, num8, subwindowMode, bool, num9, num10, pixmap3, num11, num12, arcMode);
    }

    @RequestHandler(opcode = 60)
    @Locks({"GRAPHICS_CONTEXTS_MANAGER"})
    public void FreeGC(XResponse xResponse, @RequestParam GraphicsContext graphicsContext) throws IOException {
        this.xServer.getGraphicsContextsManager().removeGraphicsContext(graphicsContext);
    }
}
