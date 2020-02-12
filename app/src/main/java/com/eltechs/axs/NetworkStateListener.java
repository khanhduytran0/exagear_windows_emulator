package com.eltechs.axs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import com.eltechs.axs.helpers.ArithHelpers;
import com.eltechs.axs.helpers.Assert;

public class NetworkStateListener {
    private final BroadcastReceiver broadcastReceiver;
    /* access modifiers changed from: private */
    public final ConnectivityManager connectivityManager;
    private final Context context;
    /* access modifiers changed from: private */
    public final OnNetworkStateChangedListener networkStateChangedListener;
    private final IntentFilter networkStateIntentFilter;
    /* access modifiers changed from: private */
    public final WifiManager wifiManager;

    public interface OnNetworkStateChangedListener {
        void onNetworkStateChanged(String str);
    }

    public NetworkStateListener(Context context2, OnNetworkStateChangedListener onNetworkStateChangedListener) {
		this.networkStateIntentFilter = new IntentFilter();
        this.context = context2.getApplicationContext();
		this.connectivityManager = ((ConnectivityManager) this.context.getSystemService("connectivity"));
		this.wifiManager =  ((WifiManager) this.context.getSystemService("wifi"));
        this.networkStateIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.networkStateChangedListener = onNetworkStateChangedListener;
        this.broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Assert.isTrue(intent.getAction() == "android.net.conn.CONNECTIVITY_CHANGE");
                String str = "127.0.0.1";
                NetworkInfo activeNetworkInfo = NetworkStateListener.this.connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1) {
                    int ipAddress = NetworkStateListener.this.wifiManager.getConnectionInfo().getIpAddress();
                    if (ipAddress != 0) {
                        byte[] bArr = {(byte) ipAddress, (byte) (ipAddress >> 8), (byte) (ipAddress >> 16), (byte) (ipAddress >> 24)};
                        str = String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(ArithHelpers.extendAsUnsigned(bArr[0])), Integer.valueOf(ArithHelpers.extendAsUnsigned(bArr[1])), Integer.valueOf(ArithHelpers.extendAsUnsigned(bArr[2])), Integer.valueOf(ArithHelpers.extendAsUnsigned(bArr[3]))});
                    }
                }
                NetworkStateListener.this.networkStateChangedListener.onNetworkStateChanged(str);
            }
        };
    }

    public void start() {
        this.context.registerReceiver(this.broadcastReceiver, this.networkStateIntentFilter);
    }

    public void stop() {
        this.context.unregisterReceiver(this.broadcastReceiver);
    }
}
