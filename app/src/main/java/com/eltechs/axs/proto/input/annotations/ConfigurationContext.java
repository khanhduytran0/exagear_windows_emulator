package com.eltechs.axs.proto.input.annotations;

import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;

public interface ConfigurationContext {
    ParameterDescriptor findNamedParameter(String str);

    String getHandlerMethodName();

    ParameterDescriptor getParameter(int i);

    int getParametersCount();
}
