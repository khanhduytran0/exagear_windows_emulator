package com.eltechs.axs.helpers.iab;

public class IabException extends Exception {
    IabResult mResult;

    public IabException(IabResult iabResult) {
        this(iabResult, (Exception) null);
    }

    public IabException(int i, String str) {
        this(new IabResult(i, str));
    }

    public IabException(IabResult iabResult, Exception exc) {
        super(iabResult.getMessage(), exc);
        this.mResult = iabResult;
    }

    public IabException(int i, String str, Exception exc) {
        this(new IabResult(i, str), exc);
    }

    public IabResult getResult() {
        return this.mResult;
    }
}
