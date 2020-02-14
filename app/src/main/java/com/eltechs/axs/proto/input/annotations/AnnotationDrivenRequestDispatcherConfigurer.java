package com.eltechs.axs.proto.input.annotations;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.ConfigurableRequestsDispatcher;
import com.eltechs.axs.proto.input.annotations.impl.AnnotationDrivenOpcodeHandler;
import com.eltechs.axs.proto.input.annotations.impl.AnnotationDrivenRequestParser;
import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;
import com.eltechs.axs.xserver.LocksManager.Subsystem;
import java.lang.reflect.Method;

public class AnnotationDrivenRequestDispatcherConfigurer {
    private final RequestContextParamReadersFactory reqCtxParamReadersFactory;
    private final RequestParamReadersFactory reqParamReadersFactory;
    private final ConfigurableRequestsDispatcher target;

    public AnnotationDrivenRequestDispatcherConfigurer(ConfigurableRequestsDispatcher configurableRequestsDispatcher, RequestContextParamReadersFactory requestContextParamReadersFactory, RequestParamReadersFactory requestParamReadersFactory) {
        this.target = configurableRequestsDispatcher;
        this.reqCtxParamReadersFactory = requestContextParamReadersFactory;
        this.reqParamReadersFactory = requestParamReadersFactory;
    }

    public void configureDispatcher(final Object... objArr) {
        // Method[] methods;
        for (Object obj : objArr) {
            for (Method method : obj.getClass().getMethods()) {
                RequestHandler requestHandler = method.getAnnotation(RequestHandler.class);
                if (requestHandler != null) {
                    processOneHandler(requestHandler.opcode(), obj, method);
                }
            }
        }
    }

    private void processOneHandler(final int i, final Object obj, final Method method) {
        this.target.installRequestHandler(i, new AnnotationDrivenOpcodeHandler(obj, method, getNeededLocks(method), buildRequestParser(method)));
    }

    private AnnotationDrivenRequestParser buildRequestParser(Method method) {
        ParameterDescriptor[] methodParameters = ParameterDescriptor.getMethodParameters(method);
        int length = methodParameters.length;
        ParameterReader[] parameterReaderArr = new ParameterReader[length];
        for (int i = 0; i < length; i++) {
            parameterReaderArr[i] = configureParameterReader(method, methodParameters, i);
        }
        return new AnnotationDrivenRequestParser(parameterReaderArr);
    }

    private ParameterReader configureParameterReader(final Method method, final ParameterDescriptor[] parameterDescriptorArr, int i) {
        ParameterReader parameterReader;
        ParameterDescriptor parameterDescriptor = parameterDescriptorArr[i];
        ConfigurationContext configurationContext = new ConfigurationContext() {
            public String getHandlerMethodName() {
                return String.format("%s::%s()", method.getDeclaringClass().getSimpleName(), method.getName());
            }

            public int getParametersCount() {
                return parameterDescriptorArr.length;
            }

            public ParameterDescriptor getParameter(int i) {
                return parameterDescriptorArr[i];
            }

            public ParameterDescriptor findNamedParameter(String str) {
                return AnnotationDrivenRequestDispatcherConfigurer.this.findNamedParameter(parameterDescriptorArr, str);
            }
        };

        if (parameterDescriptor.getAnnotation(RequestParam.class) == null) {
            parameterReader = this.reqCtxParamReadersFactory.createReader(parameterDescriptor, configurationContext);
        } else {
            parameterReader = this.reqParamReadersFactory.createReader(parameterDescriptor, configurationContext);
        }

		Assert.notNull(method, String.format("Resolved no parameter reader for the context parameter %d of the request handler method %s.", Integer.valueOf(parameterDescriptor.getIndex()), configurationContext.getHandlerMethodName()));
		
		return parameterReader;
    }

    /* access modifiers changed from: private */
    private ParameterDescriptor findNamedParameter(ParameterDescriptor[] parameterDescriptorArr, String str) {
        for (ParameterDescriptor parameterDescriptor : parameterDescriptorArr) {
            ParamName paramName = parameterDescriptor.getAnnotation(ParamName.class);
            if (paramName != null && str.equals(paramName.value())) {
                return parameterDescriptor;
            }
        }
        return null;
    }

    private Subsystem[] getNeededLocks(Method method) {
        if (method.getAnnotation(GiantLocked.class) != null) {
            return Subsystem.values();
        }
        Locks locks = method.getAnnotation(Locks.class);
        if (locks == null) {
            return new Subsystem[0];
        }
        String[] value = locks.value();
        int length = value.length;
        Subsystem[] subsystemArr = new Subsystem[length];
        for (int i = 0; i < length; i++) {
            subsystemArr[i] = Subsystem.valueOf(value[i]);
        }
        return subsystemArr;
    }
}
