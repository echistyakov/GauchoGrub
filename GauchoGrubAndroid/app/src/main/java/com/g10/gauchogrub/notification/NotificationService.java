package com.g10.gauchogrub.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {
    public NotificationService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
