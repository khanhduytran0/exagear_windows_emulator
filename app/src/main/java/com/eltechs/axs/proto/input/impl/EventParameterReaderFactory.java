package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.annotations.ConfigurationContext;
import com.eltechs.axs.proto.input.annotations.RequestParamReadersFactory;
import com.eltechs.axs.proto.input.annotations.impl.NormalRequestDataReader;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.AtomParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.BooleanParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ByteParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ColormapParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.CursorParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.DrawableParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.EnumParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.GraphicsContextParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.IntegerParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.MaskParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.PixmapParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.RemainingRequestDataAsByteBufferParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ShortParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.VisualParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.WindowParameterReader;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Colormap;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.nio.ByteBuffer;
import com.eltechs.axs.xserver.events.*;
import com.eltechs.axs.proto.input.parameterReaders.impl.*;

public class EventParameterReaderFactory {
    public static RequestParamReadersFactory INSTANCE = new RequestParamReadersFactory() {
        public ParameterReader createReader(ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
            RequestDataReader requestDataReader = NormalRequestDataReader.INSTANCE;
            Class<?> rawType = (Class<?>) parameterDescriptor.getRawType();
            if (rawType == Boolean.TYPE || rawType == Boolean.class) {
                return new BooleanParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Byte.TYPE || rawType == Byte.class) {
                return new ByteParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Short.TYPE || rawType == Short.class) {
                return new ShortParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Integer.TYPE || rawType == Integer.class) {
                return new IntegerParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Atom.class) {
                return new AtomParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Drawable.class) {
                return new DrawableParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == GraphicsContext.class) {
                return new GraphicsContextParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Window.class) {
                return new WindowParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Pixmap.class) {
                return new PixmapParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Cursor.class) {
                return new CursorParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Colormap.class) {
                return new ColormapParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Visual.class) {
                return new VisualParameterReader(requestDataReader, parameterDescriptor);
            }
            if (rawType == Mask.class) {
                return new MaskParameterReader(requestDataReader, parameterDescriptor, configurationContext);
            }
            if (Enum.class.isAssignableFrom(rawType)) {
                return new EnumParameterReader(NormalRequestDataReader.INSTANCE, parameterDescriptor);
            }
            if (rawType == ByteBuffer.class) {
                return new RemainingRequestDataAsByteBufferParameterReader();
            }
            Assert.state(false, String.format("Wrong argument type in EventBuilder annotated method: %s\n", new Object[]{rawType.toString()}));
            return null;
        }
    };

    private EventParameterReaderFactory() {
    }
}
