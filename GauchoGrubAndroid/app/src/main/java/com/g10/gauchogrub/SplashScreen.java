package com.g10.gauchogrub;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.g10.gauchogrub.services.DataAutomationService;
import com.g10.gauchogrub.services.NotificationService;

import java.util.Calendar;

public class SplashScreen extends Activity {

    private final static int SPLASH_INTERVAL = 2 * 1000;  // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Makes sure notifications are running
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Intent timedIntent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, timedIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        // Runs automation service
        Intent automationIntent = new Intent(this, DataAutomationService.class);
        PendingIntent pendingAutomationIntent = PendingIntent.getService(this, 0, automationIntent, 0);
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingAutomationIntent);
        }
        setContentView(R.layout.splash_layout);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, BaseActivity.class);
                startActivity(i);
                this.finish();
            }

            private void finish() {

            }
        }, SPLASH_INTERVAL);
    }
}