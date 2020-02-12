package com.eltechs.axs.proto.input;

public interface ConfigurableRequestsDispatcher extends RequestsDispatcher {
    void installRequestHandler(int i, OpcodeHandler opcodeHandler);
}
