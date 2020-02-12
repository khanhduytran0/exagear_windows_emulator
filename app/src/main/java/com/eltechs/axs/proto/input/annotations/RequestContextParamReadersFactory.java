package com.eltechs.axs.proto.input.annotations;

import com.eltechs.axs.proto.input.annotations.impl.ParameterDescriptor;
import com.eltechs.axs.proto.input.parameterReaders.ParameterReader;

public interface RequestContextParamReadersFactory {
    ParameterReader createReader(ParameterDescriptor parameterDescriptor, ConfigurationContext configurationContext);
}
