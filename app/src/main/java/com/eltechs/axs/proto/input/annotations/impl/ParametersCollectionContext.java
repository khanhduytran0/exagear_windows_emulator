package com.eltechs.axs.proto.input.annotations.impl;

import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.xserver.XServer;

public class ParametersCollectionContext {
    private final Object[] collectedParameters;
    private int collectedParametersCount;
    private final Object connectionContext;
    private final RequestDataRetrievalContext dataRetrievalContext;
    private final XServer xServer;

    public ParametersCollectionContext(Object obj, XServer xServer2, RequestDataRetrievalContext requestDataRetrievalContext, int i) {
        this.connectionContext = obj;
        this.xServer = xServer2;
        this.dataRetrievalContext = requestDataRetrievalContext;
        this.collectedParameters = new Object[i];
    }

    public Object getConnectionContext() {
        return this.connectionContext;
    }

    public XServer getXServer() {
        return this.xServer;
    }

    public RequestDataRetrievalContext getDataRetrievalContext() {
        return this.dataRetrievalContext;
    }

    public <T> T getCollectedParameter(int i) {
        Assert.state(i < this.collectedParametersCount, "A request parser has made a forward reference");
        return (T) this.collectedParameters[i];
    }

    public Object[] getCollectedParameters() {
        return this.collectedParameters;
    }

    public void parameterCollected(Object obj) {
        Object[] objArr = this.collectedParameters;
        int i = this.collectedParametersCount;
        this.collectedParametersCount = i + 1;
        objArr[i] = obj;
    }
}
