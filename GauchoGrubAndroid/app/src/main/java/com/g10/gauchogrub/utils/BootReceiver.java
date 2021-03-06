package com.g10.gauchogrub.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.g10.gauchogrub.services.DataAutomationService;
import com.g10.gauchogrub.services.NotificationService;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    /**
     * BootReceiver() is an empty default
     */
    public BootReceiver() {
    }

    /**
     * onRecieve() takes a context and an intent defined in the AndroidManifest.xml file, defined
     * as an intent released by the system when the OS is booted up
     * it then sets specifically timed dataAutomation and notification services
     * @param context the application context
     * @param intent the android boot intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        // Prepares intent for data automation service
        Intent timedIntent = new Intent(context, DataAutomationService.class);
        PendingIntent pendingAutomationIntent = PendingIntent.getService(context, 0, timedIntent, 0);

        // sets up notification service
        Intent notificationIntent = new Intent(context, NotificationService.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getService(context, 0, notificationIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingNotificationIntent);

        // sets up data automation service
        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, 2 * 60 * 1000, pendingAutomationIntent);
            context.startService(timedIntent);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingAutomationIntent);
    }
}