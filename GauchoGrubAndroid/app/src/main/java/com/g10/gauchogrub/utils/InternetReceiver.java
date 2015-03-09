package com.g10.gauchogrub.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.g10.gauchogrub.services.DataAutomationService;

public class InternetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Intent automationIntent = new Intent(context, DataAutomationService.class);
        if (netInfo != null && netInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if(wifiManager.isWifiEnabled())
                context.startService(automationIntent);
        }
        else {
            context.stopService(automationIntent);
        }
    }

}
