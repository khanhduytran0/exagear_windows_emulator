package com.eltechs.axs.proto.input.annotations;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.EventParsersRegistry;
import com.eltechs.axs.proto.input.annotations.impl.EventParser;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.impl.EventParameterReaderFactory;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class AnnotationDrivenEventParserConfigurer {
    private static ParameterReader configureParameterReader(ParameterDescriptor[] paramArrayOfParameterDescriptor, int paramInt) {
        ParameterDescriptor parameterDescriptor = paramArrayOfParameterDescriptor[paramInt];
        return EventParameterReaderFactory.INSTANCE.createReader(parameterDescriptor, null);
    }

    public static EventParsersRegistry createParserRegistry(Class paramClass) {
        EventParsersRegistry eventParsersRegistry = new EventParsersRegistry();
        for (Method method : paramClass.getMethods()) {
            EventId eventId = method.<EventId>getAnnotation(EventId.class);
            if (eventId != null)
//                if (Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                    eventParsersRegistry.installEventParser(eventId.id(), processOneMethod(method));
//                } else {
//                    Assert.state(false, String.format("Event parser method %s must be public and static", new Object[] { method.toString() }));
//                }
        }
        return eventParsersRegistry;
    }

    private static EventParser processOneMethod(Method paramMethod) {
        ParameterDescriptor[] arrayOfParameterDescriptor = ParameterDescriptor.getMethodParameters(paramMethod);
        int i = 0;
        int j = arrayOfParameterDescriptor.length;
        ParameterReader[] arrayOfParameterReader = new ParameterReader[j];
        while (i < j) {
            arrayOfParameterReader[i] = configureParameterReader(arrayOfParameterDescriptor, i);
            i++;
        }
        return new EventParser(arrayOfParameterReader, paramMethod);
    }
}
