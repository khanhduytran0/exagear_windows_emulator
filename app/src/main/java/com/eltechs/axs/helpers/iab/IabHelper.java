package com.eltechs.axs.helpers.iab;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
/*
import com.android.vending.billing.IInAppBillingService;
import com.android.vending.billing.IInAppBillingService.Stub;
*/
import com.eltechs.axs.helpers.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

public class IabHelper {
    public static final int BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5;
    public static final int BILLING_RESPONSE_RESULT_ERROR = 6;
    public static final int BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8;
    public static final int BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4;
    public static final int BILLING_RESPONSE_RESULT_OK = 0;
    public static final int BILLING_RESPONSE_RESULT_USER_CANCELED = 1;
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    public static final String GET_SKU_DETAILS_ITEM_TYPE_LIST = "ITEM_TYPE_LIST";
    public static final int IABHELPER_BAD_RESPONSE = -1002;
    public static final int IABHELPER_ERROR_BASE = -1000;
    public static final int IABHELPER_INVALID_CONSUMPTION = -1010;
    public static final int IABHELPER_MISSING_TOKEN = -1007;
    public static final int IABHELPER_REMOTE_EXCEPTION = -1001;
    public static final int IABHELPER_SEND_INTENT_FAILED = -1004;
    public static final int IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IABHELPER_UNKNOWN_ERROR = -1008;
    public static final int IABHELPER_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IABHELPER_USER_CANCELLED = -1005;
    public static final int IABHELPER_VERIFICATION_FAILED = -1003;
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";
    static final int NO_REQUEST_CODE = -1;
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_INAPP_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST = "INAPP_DATA_SIGNATURE_LIST";
    boolean mAsyncInProgress = false;
    String mAsyncOperation = "";
    ExecutorService mAsyncRunner = Executors.newFixedThreadPool(1);
    Context mContext;
    boolean mDebugLog = false;
    String mDebugTag = "IabHelper";
    boolean mDisposed = false;
    OnIabPurchaseFinishedListener mPurchaseListener;
    String mPurchasingItemType;
    int mRequestCode = -1;
    int mRequestCodeOld;
    // IInAppBillingService mService = null;
    ServiceConnection mServiceConn = null;
    boolean mSetupDone = false;
    String mSignatureBase64 = null;
    boolean mSubscriptionsSupported = false;

    public interface OnConsumeFinishedListener {
        void onConsumeFinished(Purchase purchase, IabResult iabResult);
    }

    public interface OnConsumeMultiFinishedListener {
        void onConsumeMultiFinished(List<Purchase> list, List<IabResult> list2);
    }

    public interface OnIabPurchaseFinishedListener {
        void onIabPurchaseFinished(IabResult iabResult, Purchase purchase);
    }

    public interface OnIabSetupFinishedListener {
        void onIabSetupFinished(IabResult iabResult);
    }

    public interface QueryInventoryFinishedListener {
        void onQueryInventoryFinished(IabResult iabResult, Inventory inventory);
    }

    public IabHelper(Context context, String str) {
        this.mContext = context.getApplicationContext();
        this.mSignatureBase64 = str;
        logDebug("IAB helper created.");
    }

    public void enableDebugLogging(boolean z, String str) {
        checkNotDisposed();
        this.mDebugLog = z;
        this.mDebugTag = str;
    }

    public void enableDebugLogging(boolean z) {
        checkNotDisposed();
        this.mDebugLog = z;
    }

    public boolean isSubscriptionsSupported() {
        checkNotDisposed();
        checkSetupDone("isSubscriptionsSupported");
        return this.mSubscriptionsSupported;
    }

    public boolean isSetupDone() {
        return this.mSetupDone;
    }

    public void startSetup(final OnIabSetupFinishedListener onIabSetupFinishedListener) {
        checkNotDisposed();
        if (this.mSetupDone) {
            throw new IllegalStateException("IAB helper is already set up.");
        } else if (onIabSetupFinishedListener == null) {
            throw new NullPointerException("null listener");
        } else {
            Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            intent.setPackage("com.android.vending");
            List queryIntentServices = this.mContext.getPackageManager().queryIntentServices(intent, 0);
            if (queryIntentServices == null || queryIntentServices.isEmpty()) {
                onIabSetupFinishedListener.onIabSetupFinished(new IabResult(3, "Billing service unavailable on device."));
                return;
            }
            logDebug("Starting in-app billing setup.");
            this.mServiceConn = new ServiceConnection() {
                public void onServiceDisconnected(ComponentName componentName) {
                    IabHelper.this.logDebug("Billing service disconnected.");
                    // IabHelper.this.mService = null;
                }

                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    if (!IabHelper.this.mDisposed) {
                        IabHelper.this.logDebug("Billing service connected.");
                        // IabHelper.this.mService = Stub.asInterface(iBinder);
                        String packageName = IabHelper.this.mContext.getPackageName();
                        try {
                            IabHelper.this.logDebug("Checking for in-app billing 3 support.");
                            int isBillingSupported = 0; // IabHelper.this.mService.isBillingSupported(3, packageName, IabHelper.ITEM_TYPE_INAPP);
                            if (isBillingSupported != 0) {
                                onIabSetupFinishedListener.onIabSetupFinished(new IabResult(isBillingSupported, "Error checking for billing v3 support."));
                                IabHelper.this.mSubscriptionsSupported = false;
                                return;
                            }
                            IabHelper iabHelper = IabHelper.this;
                            StringBuilder sb = new StringBuilder();
                            sb.append("In-app billing version 3 supported for ");
                            sb.append(packageName);
                            iabHelper.logDebug(sb.toString());
                            int isBillingSupported2 = 0; // IabHelper.this.mService.isBillingSupported(3, packageName, IabHelper.ITEM_TYPE_SUBS);
                            if (isBillingSupported2 == 0) {
                                IabHelper.this.logDebug("Subscriptions AVAILABLE.");
                                IabHelper.this.mSubscriptionsSupported = true;
                            } else {
                                IabHelper iabHelper2 = IabHelper.this;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Subscriptions NOT AVAILABLE. Response: ");
                                sb2.append(isBillingSupported2);
                                iabHelper2.logDebug(sb2.toString());
                            }
                            if (!IabHelper.this.mSetupDone) {
                                IabHelper.this.mSetupDone = true;
                                onIabSetupFinishedListener.onIabSetupFinished(new IabResult(0, "Setup successful."));
                            }
                        } catch (Exception e) {
                            onIabSetupFinishedListener.onIabSetupFinished(new IabResult(IabHelper.IABHELPER_REMOTE_EXCEPTION, "RemoteException while setting up in-app billing."));
                            e.printStackTrace();
                        }
                    }
                }
            };
            Assert.isTrue(this.mContext.bindService(intent, this.mServiceConn, Context.BIND_AUTO_CREATE));
        }
    }

    public void dispose() {
        checkUiThread();
        this.mAsyncRunner.shutdown();
        while (!this.mAsyncRunner.isTerminated()) {
            try {
                this.mAsyncRunner.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException unused) {
            }
        }
        logDebug("Disposing.");
        this.mSetupDone = false;
        if (this.mServiceConn != null) {
            logDebug("Unbinding from service.");
            if (this.mContext != null) {
                this.mContext.unbindService(this.mServiceConn);
            }
        }
        this.mDisposed = true;
        this.mContext = null;
        this.mServiceConn = null;
        // this.mService = null;
        this.mAsyncRunner = null;
        this.mPurchaseListener = null;
    }

    public void launchPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener) {
        launchPurchaseFlow(activity, str, i, onIabPurchaseFinishedListener, "");
    }

    public void launchPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener, String str2) {
        launchPurchaseFlow(activity, str, ITEM_TYPE_INAPP, i, onIabPurchaseFinishedListener, str2);
    }

    public void launchSubscriptionPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener) {
        launchSubscriptionPurchaseFlow(activity, str, i, onIabPurchaseFinishedListener, "");
    }

    public void launchSubscriptionPurchaseFlow(Activity activity, String str, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener, String str2) {
        launchPurchaseFlow(activity, str, ITEM_TYPE_SUBS, i, onIabPurchaseFinishedListener, str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x013d  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x016d  */
    public void launchPurchaseFlow(Activity activity, String str, String str2, int i, OnIabPurchaseFinishedListener onIabPurchaseFinishedListener, String str3) {
		/*
        SendIntentException sendIntentException;
        OnIabPurchaseFinishedListener onIabPurchaseFinishedListener2;
        int i2;
        Purchase purchase;
        RemoteException remoteException;
        String str4 = str;
        String str5 = str2;
        int i3 = i;
        OnIabPurchaseFinishedListener onIabPurchaseFinishedListener3 = onIabPurchaseFinishedListener;
        boolean z = true;
        Assert.state(i3 != -1);
        if (this.mRequestCode != -1) {
            z = false;
        }
        Assert.state(z);
        checkNotDisposed();
        checkSetupDone("launchPurchaseFlow");
        flagStartAsync("launchPurchaseFlow");
        if (!str5.equals(ITEM_TYPE_SUBS) || this.mSubscriptionsSupported) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("Constructing buy intent for ");
                sb.append(str4);
                sb.append(", item type: ");
                sb.append(str5);
                logDebug(sb.toString());
                Bundle buyIntent = this.mService.getBuyIntent(3, this.mContext.getPackageName(), str4, str5, str3);
                int responseCodeFromBundle = getResponseCodeFromBundle(buyIntent);
                if (responseCodeFromBundle != 0) {
                    try {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unable to buy item, Error response: ");
                        sb2.append(getResponseDesc(responseCodeFromBundle));
                        logError(sb2.toString());
                        flagEndAsync();
                        IabResult iabResult = new IabResult(responseCodeFromBundle, "Unable to buy item");
                        if (onIabPurchaseFinishedListener3 != null) {
                            onIabPurchaseFinishedListener3.onIabPurchaseFinished(iabResult, null);
                        }
                    } catch (SendIntentException e) {
                        sendIntentException = e;
                        purchase = null;
                        i2 = -1;
                        onIabPurchaseFinishedListener2 = onIabPurchaseFinishedListener3;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("SendIntentException while launching purchase flow for sku ");
                        sb3.append(str4);
                        logError(sb3.toString());
                        sendIntentException.printStackTrace();
                        flagEndAsync();
                        this.mRequestCode = i2;
                        IabResult iabResult2 = new IabResult(IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.");
                        if (onIabPurchaseFinishedListener2 != null) {
                            onIabPurchaseFinishedListener2.onIabPurchaseFinished(iabResult2, purchase);
                        }
                    } catch (RemoteException e2) {
                        remoteException = e2;
                        purchase = null;
                        i2 = -1;
                        onIabPurchaseFinishedListener2 = onIabPurchaseFinishedListener3;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("RemoteException while launching purchase flow for sku ");
                        sb4.append(str4);
                        logError(sb4.toString());
                        remoteException.printStackTrace();
                        flagEndAsync();
                        this.mRequestCode = i2;
                        IabResult iabResult3 = new IabResult(IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow");
                        if (onIabPurchaseFinishedListener2 != null) {
                            onIabPurchaseFinishedListener2.onIabPurchaseFinished(iabResult3, purchase);
                        }
                    }
                } else {
                    PendingIntent pendingIntent = (PendingIntent) buyIntent.getParcelable(RESPONSE_BUY_INTENT);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Launching buy intent for ");
                    sb5.append(str4);
                    sb5.append(". Request code: ");
                    sb5.append(i3);
                    logDebug(sb5.toString());
                    this.mRequestCode = i3;
                    this.mRequestCodeOld = i3;
                    this.mPurchaseListener = onIabPurchaseFinishedListener3;
                    this.mPurchasingItemType = str5;
                    IntentSender intentSender = pendingIntent.getIntentSender();
                    IntentSender intentSender2 = intentSender;
                    purchase = null;
                    i2 = -1;
                    onIabPurchaseFinishedListener2 = onIabPurchaseFinishedListener3;
                    try {
                        activity.startIntentSenderForResult(intentSender2, i3, new Intent(), Integer.valueOf(0).intValue(), Integer.valueOf(0).intValue(), Integer.valueOf(0).intValue());
                    } catch (SendIntentException e3) {
                        e = e3;
                    } catch (RemoteException e4) {
                        e = e4;
                        remoteException = e;
                        StringBuilder sb42 = new StringBuilder();
                        sb42.append("RemoteException while launching purchase flow for sku ");
                        sb42.append(str4);
                        logError(sb42.toString());
                        remoteException.printStackTrace();
                        flagEndAsync();
                        this.mRequestCode = i2;
                        IabResult iabResult32 = new IabResult(IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow");
                        if (onIabPurchaseFinishedListener2 != null) {
                        }
                    }
                }
            } catch (SendIntentException e5) {
                e = e5;
                purchase = null;
                i2 = -1;
                onIabPurchaseFinishedListener2 = onIabPurchaseFinishedListener3;
                sendIntentException = e;
                StringBuilder sb32 = new StringBuilder();
                sb32.append("SendIntentException while launching purchase flow for sku ");
                sb32.append(str4);
                logError(sb32.toString());
                sendIntentException.printStackTrace();
                flagEndAsync();
                this.mRequestCode = i2;
                IabResult iabResult22 = new IabResult(IABHELPER_SEND_INTENT_FAILED, "Failed to send intent.");
                if (onIabPurchaseFinishedListener2 != null) {
                }
            } catch (RemoteException e6) {
                e = e6;
                purchase = null;
                i2 = -1;
                onIabPurchaseFinishedListener2 = onIabPurchaseFinishedListener3;
                remoteException = e;
                StringBuilder sb422 = new StringBuilder();
                sb422.append("RemoteException while launching purchase flow for sku ");
                sb422.append(str4);
                logError(sb422.toString());
                remoteException.printStackTrace();
                flagEndAsync();
                this.mRequestCode = i2;
                IabResult iabResult322 = new IabResult(IABHELPER_REMOTE_EXCEPTION, "Remote exception while starting purchase flow");
                if (onIabPurchaseFinishedListener2 != null) {
                }
            }
        } else {
            IabResult iabResult4 = new IabResult(IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE, "Subscriptions are not available.");
            flagEndAsync();
            if (onIabPurchaseFinishedListener3 != null) {
                onIabPurchaseFinishedListener3.onIabPurchaseFinished(iabResult4, null);
            }
        }
		*/

        // MOD: Bypass In-app purchase
        if (onIabPurchaseFinishedListener != null) {
            onIabPurchaseFinishedListener.onIabPurchaseFinished(new IabResult(0, null), null);
        }
    }

    public boolean handleActivityResult(int i, int i2, Intent intent) {
        Assert.state(i != -1);
        Assert.state((this.mRequestCode == -1 && i == this.mRequestCodeOld) ? false : true, "not correct check, but we use it for temporary control");
        if (this.mRequestCode == -1 || i != this.mRequestCode) {
            return false;
        }
        checkNotDisposed();
        checkSetupDone("handleActivityResult");
        flagEndAsync();
        this.mRequestCode = -1;
        if (intent == null) {
            logError("Null data in IAB activity result.");
            IabResult iabResult = new IabResult(IABHELPER_BAD_RESPONSE, "Null data in IAB result");
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(iabResult, null);
            }
            return true;
        }
        int responseCodeFromIntent = getResponseCodeFromIntent(intent);
        String stringExtra = intent.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
        String stringExtra2 = intent.getStringExtra(RESPONSE_INAPP_SIGNATURE);
        if (i2 == -1 && responseCodeFromIntent == 0) {
            logDebug("Successful resultcode from purchase activity.");
            StringBuilder sb = new StringBuilder();
            sb.append("Purchase data: ");
            sb.append(stringExtra);
            logDebug(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Data signature: ");
            sb2.append(stringExtra2);
            logDebug(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Extras: ");
            sb3.append(intent.getExtras());
            logDebug(sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Expected item type: ");
            sb4.append(this.mPurchasingItemType);
            logDebug(sb4.toString());
            if (stringExtra == null || stringExtra2 == null) {
                logError("BUG: either purchaseData or dataSignature is null.");
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Extras: ");
                sb5.append(intent.getExtras().toString());
                logDebug(sb5.toString());
                IabResult iabResult2 = new IabResult(IABHELPER_UNKNOWN_ERROR, "IAB returned null purchaseData or dataSignature");
                if (this.mPurchaseListener != null) {
                    this.mPurchaseListener.onIabPurchaseFinished(iabResult2, null);
                }
                return true;
            }
            try {
                Purchase purchase = new Purchase(this.mPurchasingItemType, stringExtra, stringExtra2);
                String sku = purchase.getSku();
                if (!Security.verifyPurchase(this.mSignatureBase64, stringExtra, stringExtra2)) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Purchase signature verification FAILED for sku ");
                    sb6.append(sku);
                    logError(sb6.toString());
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Signature verification failed for sku ");
                    sb7.append(sku);
                    IabResult iabResult3 = new IabResult(IABHELPER_VERIFICATION_FAILED, sb7.toString());
                    if (this.mPurchaseListener != null) {
                        this.mPurchaseListener.onIabPurchaseFinished(iabResult3, purchase);
                    }
                    return true;
                }
                logDebug("Purchase signature successfully verified.");
                IabResult iabResult4 = new IabResult(0, "Success");
                if (this.mPurchaseListener != null) {
                    this.mPurchaseListener.onIabPurchaseFinished(iabResult4, purchase);
                }
            } catch (JSONException e) {
                logError("Failed to parse purchase data.");
                e.printStackTrace();
                IabResult iabResult5 = new IabResult(IABHELPER_BAD_RESPONSE, "Failed to parse purchase data.");
                if (this.mPurchaseListener != null) {
                    this.mPurchaseListener.onIabPurchaseFinished(iabResult5, null);
                }
                return true;
            }
        } else if (i2 == -1) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append("Result code was OK but in-app billing response was not OK: ");
            sb8.append(getResponseDesc(responseCodeFromIntent));
            logDebug(sb8.toString());
            IabResult iabResult6 = new IabResult(responseCodeFromIntent, "Problem purchashing item.");
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(iabResult6, null);
            }
        } else if (i2 == 0) {
            StringBuilder sb9 = new StringBuilder();
            sb9.append("Purchase canceled - Response: ");
            sb9.append(getResponseDesc(responseCodeFromIntent));
            logDebug(sb9.toString());
            IabResult iabResult7 = new IabResult(IABHELPER_USER_CANCELLED, "User canceled.");
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(iabResult7, null);
            }
        } else {
            StringBuilder sb10 = new StringBuilder();
            sb10.append("Purchase failed. Result code: ");
            sb10.append(Integer.toString(i2));
            sb10.append(". Response: ");
            sb10.append(getResponseDesc(responseCodeFromIntent));
            logError(sb10.toString());
            IabResult iabResult8 = new IabResult(IABHELPER_UNKNOWN_PURCHASE_RESPONSE, "Unknown purchase response.");
            if (this.mPurchaseListener != null) {
                this.mPurchaseListener.onIabPurchaseFinished(iabResult8, null);
            }
        }
        return true;
    }

    public Inventory queryInventory(boolean z, List<String> list, List<String> list2) throws IabException {
        checkNotDisposed();
        checkSetupDone("queryInventory");
        try {
            Inventory inventory = new Inventory();
            int queryPurchases = queryPurchases(inventory, ITEM_TYPE_INAPP);
            if (queryPurchases != 0) {
                throw new IabException(queryPurchases, "Error refreshing inventory (querying owned items).");
            }
            if (z) {
                int querySkuDetails = querySkuDetails(ITEM_TYPE_INAPP, inventory, list);
                if (querySkuDetails != 0) {
                    throw new IabException(querySkuDetails, "Error refreshing inventory (querying prices of items).");
                }
            }
            if (this.mSubscriptionsSupported) {
                int queryPurchases2 = queryPurchases(inventory, ITEM_TYPE_SUBS);
                if (queryPurchases2 != 0) {
                    throw new IabException(queryPurchases2, "Error refreshing inventory (querying owned subscriptions).");
                } else if (z) {
                    int querySkuDetails2 = querySkuDetails(ITEM_TYPE_SUBS, inventory, list);
                    if (querySkuDetails2 != 0) {
                        throw new IabException(querySkuDetails2, "Error refreshing inventory (querying prices of subscriptions).");
                    }
                }
            }
            return inventory;
        } catch (RemoteException e) {
            throw new IabException(IABHELPER_REMOTE_EXCEPTION, "Remote exception while refreshing inventory.", e);
        } catch (JSONException e2) {
            throw new IabException(IABHELPER_BAD_RESPONSE, "Error parsing JSON response while refreshing inventory.", e2);
        }
    }

    public void queryInventoryAsync(boolean z, List<String> list, QueryInventoryFinishedListener queryInventoryFinishedListener) {
        final Handler handler = new Handler();
        checkNotDisposed();
        checkSetupDone("queryInventory");
        checkUiThread();
        flagStartAsync("refresh inventory");
        ExecutorService executorService = this.mAsyncRunner;
        final boolean z2 = z;
        final List<String> list2 = list;
        final QueryInventoryFinishedListener queryInventoryFinishedListener2 = queryInventoryFinishedListener;
        Runnable r0 = new Runnable() {
            public void run() {
                IabResult iabResult = new IabResult(0, "Inventory refresh successful.");
                Inventory inventory = new Inventory();

                try {
                    inventory = IabHelper.this.queryInventory(z2, list2, null);
                    iabResult = new IabResult(0, "Inventory refresh successful.");
                } catch (IabException e) {
                    // Create a fake Inventory.

                }
/*
                IabHelper.this.flagEndAsync();
                if (!IabHelper.this.mDisposed && queryInventoryFinishedListener2 != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            if (!IabHelper.this.mDisposed) {
                                queryInventoryFinishedListener2.onQueryInventoryFinished(iabResult, inventory);
                            }
                        }
                    });
                }
 */
            }
        };
        executorService.submit(r0);
    }

    public void queryInventoryAsync(QueryInventoryFinishedListener queryInventoryFinishedListener) {
        queryInventoryAsync(true, null, queryInventoryFinishedListener);
    }

    public void queryInventoryAsync(boolean z, QueryInventoryFinishedListener queryInventoryFinishedListener) {
        queryInventoryAsync(z, null, queryInventoryFinishedListener);
    }

    /* access modifiers changed from: 0000 */
    public void consume(Purchase purchase) throws IabException {
        checkNotDisposed();
        checkSetupDone("consume");
        if (!purchase.mItemType.equals(ITEM_TYPE_INAPP)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Items of type '");
            sb.append(purchase.mItemType);
            sb.append("' can't be consumed.");
            throw new IabException((int) IABHELPER_INVALID_CONSUMPTION, sb.toString());
        }
        try {
            String token = purchase.getToken();
            String sku = purchase.getSku();
            if (token != null) {
                if (!token.equals("")) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Consuming sku: ");
                    sb2.append(sku);
                    sb2.append(", token: ");
                    sb2.append(token);
                    logDebug(sb2.toString());
                    int consumePurchase = 0; // this.mService.consumePurchase(3, this.mContext.getPackageName(), token);
                    if (consumePurchase == 0) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Successfully consumed sku: ");
                        sb3.append(sku);
                        logDebug(sb3.toString());
                        return;
                    }
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Error consuming consuming sku ");
                    sb4.append(sku);
                    sb4.append(". ");
                    sb4.append(getResponseDesc(consumePurchase));
                    logDebug(sb4.toString());
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Error consuming sku ");
                    sb5.append(sku);
                    throw new IabException(consumePurchase, sb5.toString());
                }
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Can't consume ");
            sb6.append(sku);
            sb6.append(". No token.");
            logError(sb6.toString());
            StringBuilder sb7 = new StringBuilder();
            sb7.append("PurchaseInfo is missing token for sku: ");
            sb7.append(sku);
            sb7.append(" ");
            sb7.append(purchase);
            throw new IabException((int) IABHELPER_MISSING_TOKEN, sb7.toString());
        } catch (Exception e) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append("Got an exception while consuming. PurchaseInfo: ");
            sb8.append(purchase);
            throw new IabException(IABHELPER_REMOTE_EXCEPTION, sb8.toString(), e);
        }
    }

    public void consumeAsync(Purchase purchase, OnConsumeFinishedListener onConsumeFinishedListener) {
        checkNotDisposed();
        checkSetupDone("consume");
        ArrayList arrayList = new ArrayList();
        arrayList.add(purchase);
        consumeAsyncInternal(arrayList, onConsumeFinishedListener, null);
    }

    public void consumeAsync(List<Purchase> list, OnConsumeMultiFinishedListener onConsumeMultiFinishedListener) {
        checkNotDisposed();
        checkSetupDone("consume");
        consumeAsyncInternal(list, null, onConsumeMultiFinishedListener);
    }

    public static String getResponseDesc(int i) {
        String[] split = "0:OK/1:User Canceled/2:Unknown/3:Billing Unavailable/4:Item unavailable/5:Developer Error/6:Error/7:Item Already Owned/8:Item not owned".split("/");
        String[] split2 = "0:OK/-1001:Remote exception during initialization/-1002:Bad response received/-1003:Purchase signature verification failed/-1004:Send intent failed/-1005:User cancelled/-1006:Unknown purchase response/-1007:Missing token/-1008:Unknown error/-1009:Subscriptions not available/-1010:Invalid consumption attempt".split("/");
        if (i <= -1000) {
            int i2 = -1000 - i;
            if (i2 >= 0 && i2 < split2.length) {
                return split2[i2];
            }
            StringBuilder sb = new StringBuilder();
            sb.append(String.valueOf(i));
            sb.append(":Unknown IAB Helper Error");
            return sb.toString();
        } else if (i >= 0 && i < split.length) {
            return split[i];
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(String.valueOf(i));
            sb2.append(":Unknown");
            return sb2.toString();
        }
    }

    /* access modifiers changed from: 0000 */
    public int getResponseCodeFromBundle(Bundle bundle) {
        Object obj = bundle.get(RESPONSE_CODE);
        if (obj == null) {
            logDebug("Bundle with null response code, assuming OK (known issue)");
            return 0;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            if (obj instanceof Long) {
                return (int) ((Long) obj).longValue();
            }
            logError("Unexpected type for bundle response code.");
            logError(obj.getClass().getName());
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected type for bundle response code: ");
            sb.append(obj.getClass().getName());
            throw new RuntimeException(sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public int getResponseCodeFromIntent(Intent intent) {
        Object obj = intent.getExtras().get(RESPONSE_CODE);
        if (obj == null) {
            logError("Intent with no response code, assuming OK (known issue)");
            return 0;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        } else {
            if (obj instanceof Long) {
                return (int) ((Long) obj).longValue();
            }
            logError("Unexpected type for intent response code.");
            logError(obj.getClass().getName());
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected type for intent response code: ");
            sb.append(obj.getClass().getName());
            throw new RuntimeException(sb.toString());
        }
    }

    /* access modifiers changed from: 0000 */
    public int queryPurchases(Inventory inventory, String str) throws JSONException, RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("Querying owned items, item type: ");
        sb.append(str);
        logDebug(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Package name: ");
        sb2.append(this.mContext.getPackageName());
        logDebug(sb2.toString());
        int i = 0;
        String str2 = null;
        boolean z = false;
        while (true) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Calling getPurchases with continuation token: ");
            sb3.append(str2);
            logDebug(sb3.toString());
            Bundle purchases = new Bundle(); // this.mService.getPurchases(3, this.mContext.getPackageName(), str, str2);
            int responseCodeFromBundle = getResponseCodeFromBundle(purchases);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Owned items response: ");
            sb4.append(String.valueOf(responseCodeFromBundle));
            logDebug(sb4.toString());
            if (responseCodeFromBundle != 0) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("getPurchases() failed: ");
                sb5.append(getResponseDesc(responseCodeFromBundle));
                logDebug(sb5.toString());
                return responseCodeFromBundle;
            } else if (!purchases.containsKey(RESPONSE_INAPP_ITEM_LIST) || !purchases.containsKey(RESPONSE_INAPP_PURCHASE_DATA_LIST) || !purchases.containsKey(RESPONSE_INAPP_SIGNATURE_LIST)) {
                logError("Bundle returned from getPurchases() doesn't contain required fields.");
            } else {
                ArrayList stringArrayList = purchases.getStringArrayList(RESPONSE_INAPP_ITEM_LIST);
                ArrayList stringArrayList2 = purchases.getStringArrayList(RESPONSE_INAPP_PURCHASE_DATA_LIST);
                ArrayList stringArrayList3 = purchases.getStringArrayList(RESPONSE_INAPP_SIGNATURE_LIST);
                boolean z2 = z;
                for (int i2 = 0; i2 < stringArrayList2.size(); i2++) {
                    String str3 = (String) stringArrayList2.get(i2);
                    String str4 = (String) stringArrayList3.get(i2);
                    String str5 = (String) stringArrayList.get(i2);
                    if (Security.verifyPurchase(this.mSignatureBase64, str3, str4)) {
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("Sku is owned: ");
                        sb6.append(str5);
                        logDebug(sb6.toString());
                        Purchase purchase = new Purchase(str, str3, str4);
                        if (TextUtils.isEmpty(purchase.getToken())) {
                            logWarn("BUG: empty/null token!");
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("Purchase data: ");
                            sb7.append(str3);
                            logDebug(sb7.toString());
                        }
                        inventory.addPurchase(purchase);
                    } else {
                        logWarn("Purchase signature verification **FAILED**. Not adding item.");
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("   Purchase data: ");
                        sb8.append(str3);
                        logDebug(sb8.toString());
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("   Signature: ");
                        sb9.append(str4);
                        logDebug(sb9.toString());
                        z2 = true;
                    }
                }
                str2 = purchases.getString(INAPP_CONTINUATION_TOKEN);
                StringBuilder sb10 = new StringBuilder();
                sb10.append("Continuation token: ");
                sb10.append(str2);
                logDebug(sb10.toString());
                if (TextUtils.isEmpty(str2)) {
                    if (z2) {
                        i = IABHELPER_VERIFICATION_FAILED;
                    }
                    return i;
                }
                z = z2;
            }
        }
		/*
        logError("Bundle returned from getPurchases() doesn't contain required fields.");
        return IABHELPER_BAD_RESPONSE;
		*/
    }

    /* access modifiers changed from: 0000 */
    public int querySkuDetails(String str, Inventory inventory, List<String> list) throws RemoteException, JSONException {
        logDebug("Querying SKU details.");
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(inventory.getAllOwnedSkus(str));
        if (list != null) {
            for (String str2 : list) {
                if (!arrayList.contains(str2)) {
                    arrayList.add(str2);
                }
            }
        }
        if (arrayList.size() == 0) {
            logDebug("querySkuDetails: nothing to do because there are no SKUs.");
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, arrayList);
        Bundle skuDetails = new Bundle(); // this.mService.getSkuDetails(3, this.mContext.getPackageName(), str, bundle);
        if (!skuDetails.containsKey(RESPONSE_GET_SKU_DETAILS_LIST)) {
            int responseCodeFromBundle = getResponseCodeFromBundle(skuDetails);
            if (responseCodeFromBundle != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("getSkuDetails() failed: ");
                sb.append(getResponseDesc(responseCodeFromBundle));
                logDebug(sb.toString());
                return responseCodeFromBundle;
            }
            logError("getSkuDetails() returned a bundle with neither an error nor a detail list.");
            return IABHELPER_BAD_RESPONSE;
        }
        Iterator it = skuDetails.getStringArrayList(RESPONSE_GET_SKU_DETAILS_LIST).iterator();
        while (it.hasNext()) {
            SkuDetails skuDetails2 = new SkuDetails(str, (String) it.next());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Got sku details: ");
            sb2.append(skuDetails2);
            logDebug(sb2.toString());
            inventory.addSkuDetails(skuDetails2);
        }
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public void consumeAsyncInternal(List<Purchase> list, OnConsumeFinishedListener onConsumeFinishedListener, OnConsumeMultiFinishedListener onConsumeMultiFinishedListener) {
        final Handler handler = new Handler();
        checkUiThread();
        flagStartAsync("consume");
        ExecutorService executorService = this.mAsyncRunner;
        final List<Purchase> list2 = list;
        final OnConsumeFinishedListener onConsumeFinishedListener2 = onConsumeFinishedListener;
        final OnConsumeMultiFinishedListener onConsumeMultiFinishedListener2 = onConsumeMultiFinishedListener;
        Runnable r0 = new Runnable() {
            public void run() {
                final ArrayList arrayList = new ArrayList();
                for (Purchase purchase : list2) {
                    try {
                        IabHelper.this.consume(purchase);
                        StringBuilder sb = new StringBuilder();
                        sb.append("Successful consume of sku ");
                        sb.append(purchase.getSku());
                        arrayList.add(new IabResult(0, sb.toString()));
                    } catch (IabException e) {
                        arrayList.add(e.getResult());
                    }
                }
                IabHelper.this.flagEndAsync();
                if (!IabHelper.this.mDisposed && onConsumeFinishedListener2 != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            if (!IabHelper.this.mDisposed) {
                                onConsumeFinishedListener2.onConsumeFinished((Purchase) list2.get(0), (IabResult) arrayList.get(0));
                            }
                        }
                    });
                }
                if (!IabHelper.this.mDisposed && onConsumeMultiFinishedListener2 != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            if (!IabHelper.this.mDisposed) {
                                onConsumeMultiFinishedListener2.onConsumeMultiFinished(list2, arrayList);
                            }
                        }
                    });
                }
            }
        };
        executorService.submit(r0);
    }

    private void checkUiThread() {
        Assert.isTrue(Looper.getMainLooper().getThread() == Thread.currentThread());
    }

    private void checkSetupDone(String str) {
        if (!this.mSetupDone) {
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal state for operation (");
            sb.append(str);
            sb.append("): IAB helper is not set up.");
            logError(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("IAB helper is not set up. Can't perform operation: ");
            sb2.append(str);
            throw new IllegalStateException(sb2.toString());
        }
    }

    private void checkNotDisposed() {
        if (this.mDisposed) {
            throw new IllegalStateException("IabHelper was disposed of, so it cannot be used.");
        }
    }

    /* access modifiers changed from: 0000 */
    public void flagStartAsync(String str) {
        if (this.mAsyncInProgress) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't start async operation (");
            sb.append(str);
            sb.append(") because another async operation(");
            sb.append(this.mAsyncOperation);
            sb.append(") is in progress.");
            throw new IllegalStateException(sb.toString());
        }
        this.mAsyncOperation = str;
        this.mAsyncInProgress = true;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Starting async operation: ");
        sb2.append(str);
        logDebug(sb2.toString());
    }

    /* access modifiers changed from: 0000 */
    public void flagEndAsync() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ending async operation: ");
        sb.append(this.mAsyncOperation);
        logDebug(sb.toString());
        if (!this.mAsyncInProgress) {
            throw new IllegalStateException("Can't end async operation because async operation is NOT in progress.");
        }
        this.mAsyncOperation = "";
        this.mAsyncInProgress = false;
    }

    /* access modifiers changed from: 0000 */
    public void logDebug(String str) {
        if (this.mDebugLog) {
            Log.d(this.mDebugTag, str);
        }
    }

    /* access modifiers changed from: 0000 */
    public void logError(String str) {
        String str2 = this.mDebugTag;
        StringBuilder sb = new StringBuilder();
        sb.append("In-app billing error: ");
        sb.append(str);
        Log.e(str2, sb.toString());
    }

    /* access modifiers changed from: 0000 */
    public void logWarn(String str) {
        String str2 = this.mDebugTag;
        StringBuilder sb = new StringBuilder();
        sb.append("In-app billing warning: ");
        sb.append(str);
        Log.w(str2, sb.toString());
    }
}
