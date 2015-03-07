package com.g10.gauchogrub.services;


import android.app.Service;
import android.content.Intent;

import java.util.logging.Logger;

public class DataAutomationService {

    public static Logger logger = Logger.getLogger("DataAutomationService");

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }

}
