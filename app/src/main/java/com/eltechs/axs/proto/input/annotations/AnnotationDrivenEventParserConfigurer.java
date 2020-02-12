package com.eltechs.axs.proto.input.annotations;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.EventParsersRegistry;
import com.eltechs.axs.proto.input.annotations.impl.EventParser;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.impl.EventParameterReaderFactory;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import android.util.*;
import com.eltechs.ed.*;

public abstract class AnnotationDrivenEventParserConfigurer {
    private AnnotationDrivenEventParserConfigurer() {
    }

    public static EventParsersRegistry createParserRegistry(Class cls) {
        // Method[] methods;
        EventParsersRegistry eventParsersRegistry = new EventParsersRegistry();
        for (Method method : cls.getMethods()) {
            EventId eventId = (EventId) method.getAnnotation(EventId.class);
            if (eventId != null) {
                if (!Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
					Log.d(EDApplicationState.TAG, String.format("Warning: Event parser method %s must be public and static (It might be a bug)", method.toString()));
				}
                eventParsersRegistry.installEventParser(eventId.id(), processOneMethod(method));
            }
        }
        return eventParsersRegistry;
    }

    private static EventParser processOneMethod(Method method) {
        ParameterDescriptor[] methodParameters = ParameterDescriptor.getMethodParameters(method);
        int length = methodParameters.length;
        ParameterReader[] parameterReaderArr = new ParameterReader[length];
        for (int i = 0; i < length; i++) {
            parameterReaderArr[i] = configureParameterReader(methodParameters, i);
        }
        return new EventParser(parameterReaderArr, method);
    }

    private static ParameterReader configureParameterReader(ParameterDescriptor[] parameterDescriptorArr, int i) {
        return EventParameterReaderFactory.INSTANCE.createReader(parameterDescriptorArr[i], null);
    }
}
