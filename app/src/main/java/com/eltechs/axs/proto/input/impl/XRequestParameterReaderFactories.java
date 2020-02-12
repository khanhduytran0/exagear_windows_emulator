package com.eltechs.axs.proto.input.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.ConfigurationContext;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.Optional;
import com.eltechs.axs.proto.input.annotations.ParamLength;
import com.eltechs.axs.proto.input.annotations.RequestContextParamReadersFactory;
import com.eltechs.axs.proto.input.annotations.RequestParamReadersFactory;
import com.eltechs.axs.proto.input.annotations.impl.NormalRequestDataReader;
import com.eltechs.axs.proto.input.annotations.impl.OOBRequestDataReader;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.annotations.impl.ParametersCollectionContext;
import com.eltechs.axs.proto.input.annotations.impl.RequestDataReader;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.AtomParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.BooleanParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ByteParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ColormapParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ConnectionContextParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.CursorParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.DrawableParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.EnumParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.EventParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.GraphicsContextParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.IntegerParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.MaskParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.PixmapParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.RemainingRequestDataAsByteBufferParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ResponseParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ShmSegmentParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.ShortParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.String8ParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.VisualParameterReader;
import com.eltechs.axs.proto.input.parameterReaders.impl.WindowParameterReader;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xserver.Atom;
import com.eltechs.axs.xserver.Colormap;
import com.eltechs.axs.xserver.Cursor;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Pixmap;
import com.eltechs.axs.xserver.ShmSegment;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.client.XClient;
import com.eltechs.axs.xserver.events.Event;
import com.eltechs.axs.xserver.impl.drawables.Visual;
import com.eltechs.axs.xserver.impl.masks.FlagsEnum;
import com.eltechs.axs.xserver.impl.masks.Mask;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.lang.annotation.*;

public class XRequestParameterReaderFactories {
    public static final RequestContextParamReadersFactory CONTEXT_PARAM_READERS_FACTORY = new RequestContextParamReadersFactory() {
        public ParameterReader createReader(ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
            if (parameterDescriptor.getRawType() == XClient.class) {
                return new ConnectionContextParameterReader();
            }
            if (parameterDescriptor.getRawType() == XResponse.class) {
                return new ResponseParameterReader();
            }
            return null;
        }
    };
    public static final RequestParamReadersFactory REQUEST_PARAM_READERS_FACTORY = new RequestParamReadersFactory() {
        public ParameterReader createReader(ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
            ParameterReader createSimpleReader = createSimpleReader(parameterDescriptor, configurationContext);
            return createSimpleReader != null ? XRequestParameterReaderFactories.applyMask(createSimpleReader, parameterDescriptor, configurationContext) : createSimpleReader;
        }

        private ParameterReader createSimpleReader(ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
            RequestDataReader access = XRequestParameterReaderFactories.selectRequestDataReaderForParameter(parameterDescriptor);
            Class<?> rawType = (Class<?>) parameterDescriptor.getRawType();
            if (rawType == Boolean.TYPE || rawType == Boolean.class) {
                return new BooleanParameterReader(access, parameterDescriptor);
            }
            if (rawType == Byte.TYPE || rawType == Byte.class) {
                return new ByteParameterReader(access, parameterDescriptor);
            }
            if (rawType == Short.TYPE || rawType == Short.class) {
                return new ShortParameterReader(access, parameterDescriptor);
            }
            if (rawType == Integer.TYPE || rawType == Integer.class) {
                return new IntegerParameterReader(access, parameterDescriptor);
            }
            if (rawType == String.class) {
                return XRequestParameterReaderFactories.createString8Reader(parameterDescriptor, configurationContext);
            }
            if (rawType == Atom.class) {
                return new AtomParameterReader(access, parameterDescriptor);
            }
            if (rawType == Drawable.class) {
                return new DrawableParameterReader(access, parameterDescriptor);
            }
            if (rawType == GraphicsContext.class) {
                return new GraphicsContextParameterReader(access, parameterDescriptor);
            }
            if (rawType == Window.class) {
                return new WindowParameterReader(access, parameterDescriptor);
            }
            if (rawType == Pixmap.class) {
                return new PixmapParameterReader(access, parameterDescriptor);
            }
            if (rawType == Cursor.class) {
                return new CursorParameterReader(access, parameterDescriptor);
            }
            if (rawType == Colormap.class) {
                return new ColormapParameterReader(access, parameterDescriptor);
            }
            if (rawType == ShmSegment.class) {
                return new ShmSegmentParameterReader(access, parameterDescriptor);
            }
            if (rawType == Visual.class) {
                return new VisualParameterReader(access, parameterDescriptor);
            }
            if (rawType == Mask.class) {
                return new MaskParameterReader(access, parameterDescriptor, configurationContext);
            }
            if (Enum.class.isAssignableFrom(rawType)) {
                return new EnumParameterReader(XRequestParameterReaderFactories.selectRequestDataReaderForParameter(parameterDescriptor), parameterDescriptor);
            }
            if (Event.class.isAssignableFrom(rawType)) {
                return new EventParameterReader(XRequestParameterReaderFactories.selectRequestDataReaderForParameter(parameterDescriptor));
            }
            if (rawType == ByteBuffer.class) {
                return XRequestParameterReaderFactories.createByteBufferReader(parameterDescriptor, configurationContext);
            }
			
            return null;
        }
    };

    private XRequestParameterReaderFactories() {
    }

    /* access modifiers changed from: private */
    public static RequestDataReader selectRequestDataReaderForParameter(ParameterDescriptor parameterDescriptor) {
        return parameterDescriptor.getAnnotation(OOBParam.class) == null ? NormalRequestDataReader.INSTANCE : OOBRequestDataReader.INSTANCE;
    }

    /* access modifiers changed from: private */
    public static ParameterReader createString8Reader(ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
        ParamLength paramLength = parameterDescriptor.getAnnotation(ParamLength.class);
        Assert.notNull(paramLength, String.format("Parameter %d of the request handler method %s has type String and must be tagged with @ParamLength.", new Object[]{Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        ParameterDescriptor findNamedParameter = configurationContext.findNamedParameter(paramLength.value());
        Assert.notNull(findNamedParameter, String.format("Parameter %d of the request handler method %s specifies an invalid name of parameter holding the length.", new Object[]{Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        Assert.isTrue(findNamedParameter.getIndex() < parameterDescriptor.getIndex(), String.format("Parameter %d of the request handler method %s must have its length specified by a preceding parameter.", new Object[]{Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        return new String8ParameterReader(selectRequestDataReaderForParameter(parameterDescriptor), findNamedParameter.getIndex());
    }

    /* access modifiers changed from: private */
    public static ParameterReader createByteBufferReader(ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
        Assert.isTrue(parameterDescriptor.getIndex() == configurationContext.getParametersCount() - 1, String.format("Parameter %d of the request handler method %s has type ByteBuffer; such argument must be the last one.", new Object[]{Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        return new RemainingRequestDataAsByteBufferParameterReader();
    }

    /* access modifiers changed from: private */
    public static ParameterReader applyMask(final ParameterReader parameterReader, ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext) {
        Optional optional = (Optional) parameterDescriptor.getAnnotation(Optional.class);
        if (optional == null) {
            return parameterReader;
        }
        ParameterDescriptor findNamedParameter = configurationContext.findNamedParameter(optional.mask());
        Assert.notNull(findNamedParameter, String.format("Parameter %d of the request handler method %s specifies an invalid name of parameter holding the mask.", new Object[]{Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        Assert.isTrue(findNamedParameter.getIndex() < parameterDescriptor.getIndex(), String.format("Parameter %d of the request handler method %s must have its presence specified by a mask in a preceding parameter.", new Object[]{Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        Class flagsClass = getFlagsClass(findNamedParameter.getType());
        Assert.notNull(flagsClass, String.format("The parameter '%s' specified as a presence marker mask to the parameter %d of %s must be of type type Mask<>.", new Object[]{optional.mask(), Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        final FlagsEnum enumValue = getEnumValue(flagsClass, optional.bit());
        Assert.notNull(enumValue, String.format("Invalid flag name '%s' in the specification of the parameter %d of the request handler method %s.", new Object[]{optional.bit(), Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()}));
        final int index = findNamedParameter.getIndex();
        return new ParameterReader() {
            public void readParameter(ParametersCollectionContext parametersCollectionContext) throws XProtocolError {
                if (((Mask) parametersCollectionContext.getCollectedParameter(index)).isSet(enumValue)) {
                    parameterReader.readParameter(parametersCollectionContext);
                } else {
                    parametersCollectionContext.parameterCollected(null);
                }
            }
        };
    }

    public static Class<? extends Enum<? extends FlagsEnum>> getFlagsClass(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        return (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    private static FlagsEnum getEnumValue(Class<? extends Enum<? extends FlagsEnum>> cls, String str) {
        Enum[] enumArr;
        for (Enum enumR : (Enum[]) cls.getEnumConstants()) {
            if (enumR.name().equals(str)) {
                return (FlagsEnum) enumR;
            }
        }
        return null;
    }
}
