package com.eltechs.axs.helpers.iab;

public class IabResult {
    String mMessage;
    int mResponse;

    public IabResult(int i, String str) {
        this.mResponse = i;
        if (str == null || str.trim().length() == 0) {
            this.mMessage = IabHelper.getResponseDesc(i);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" (response: ");
        sb.append(IabHelper.getResponseDesc(i));
        sb.append(")");
        this.mMessage = sb.toString();
    }

    public int getResponse() {
        return this.mResponse;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public boolean isSuccess() {
        return this.mResponse == 0;
    }

    public boolean isFailure() {
        return !isSuccess();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IabResult: ");
        sb.append(getMessage());
        return sb.toString();
    }
}
