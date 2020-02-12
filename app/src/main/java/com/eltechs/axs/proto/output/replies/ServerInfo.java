package com.eltechs.axs.proto.output.replies;

import com.eltechs.axs.proto.X11ImplementationVendor;
import com.eltechs.axs.proto.input.impl.ProtoHelpers;
import com.eltechs.axs.proto.output.POD;
import com.eltechs.axs.proto.output.PODWriter;
import com.eltechs.axs.xserver.IdInterval;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.impl.drawables.ImageFormat;
import java.util.Collection;

@POD({"success", "unused0", "majorProtocolVersion", "minorProtocolVersion", "additionalDataLength", "releaseNumber", "resourceIdBase", "resourceIdMask", "motionBufferSize", "vendorNameLength", "maximumRequestLength", "screensCount", "pixmapFormatsCount", "imageByteOrder", "bitmapFormatBitOrder", "bitmapFormatScanlineUnit", "bitmapFormatScanlinePad", "minKeycode", "maxKeycode", "unused1", "vendorName", "pixmapFormats", "roots"})
public class ServerInfo {
    public final short additionalDataLength;
    public final byte bitmapFormatBitOrder;
    public final byte bitmapFormatScanlinePad;
    public final byte bitmapFormatScanlineUnit;
    public final byte imageByteOrder;
    public final short majorProtocolVersion;
    public final byte maxKeycode;
    public final short maximumRequestLength;
    public final byte minKeycode;
    public final short minorProtocolVersion;
    public final int motionBufferSize;
    public final PixmapFormat[] pixmapFormats;
    public final byte pixmapFormatsCount;
    public final int releaseNumber;
    public final int resourceIdBase;
    public final int resourceIdMask;
    public final Screen[] roots;
    public final byte screensCount;
    public final byte success = 1;
    public final byte unused0;
    public final int unused1;
    public final String vendorName;
    public final short vendorNameLength;

    public ServerInfo(XServer xServer, IdInterval idInterval) {
        int i = 0;
        this.unused0 = 0;
        this.majorProtocolVersion = 11;
        this.minorProtocolVersion = 0;
        this.releaseNumber = 1;
        this.motionBufferSize = 256;
        this.maximumRequestLength = -1;
        this.screensCount = 1;
        this.imageByteOrder = 0;
        this.bitmapFormatBitOrder = 0;
        this.bitmapFormatScanlineUnit = 32;
        this.bitmapFormatScanlinePad = 32;
        this.minKeycode = 8;
        this.maxKeycode = -1;
        this.unused1 = 0;
        this.resourceIdBase = idInterval.getIdBase();
        this.resourceIdMask = idInterval.getIdMask();
        this.vendorName = X11ImplementationVendor.VENDOR_NAME;
        this.vendorNameLength = (short) this.vendorName.length();
        this.roots = new Screen[]{new Screen(xServer)};
        Collection<ImageFormat> supportedImageFormats = xServer.getDrawablesManager().getSupportedImageFormats();
        this.pixmapFormatsCount = (byte) supportedImageFormats.size();
        this.pixmapFormats = new PixmapFormat[this.pixmapFormatsCount];
        for (ImageFormat imageFormat : supportedImageFormats) {
            int i2 = i + 1;
            this.pixmapFormats[i] = new PixmapFormat((byte) imageFormat.getDepth(), (byte) imageFormat.getBitsPerPixel(), (byte) imageFormat.getScanlinePad());
            i = i2;
        }
        this.additionalDataLength = (short) (8 + (2 * this.pixmapFormatsCount) + ProtoHelpers.calculateLengthInWords(ProtoHelpers.roundUpLength4(this.vendorNameLength)) + ProtoHelpers.calculateLengthInWords(PODWriter.getOnWireLength(this.roots)));
    }
}
