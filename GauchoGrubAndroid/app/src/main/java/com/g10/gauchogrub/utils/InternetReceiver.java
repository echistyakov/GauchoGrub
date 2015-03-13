package com.g10.gauchogrub.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Intent automationIntent = new Intent(context, DataAutomationService.class);
        PendingIntent pendingAutomationIntent = PendingIntent.getService(context, 0, automationIntent, 0);
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager.isWifiEnabled()){
                alarmManager.set(AlarmManager.RTC_WAKEUP, 30 * 1000, pendingAutomationIntent);
            }
        }
    }
}
