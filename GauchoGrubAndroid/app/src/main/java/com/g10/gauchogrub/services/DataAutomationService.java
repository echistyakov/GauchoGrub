package com.g10.gauchogrub.services;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.MediaStore;

import com.g10.gauchogrub.utils.WebUtils;

import org.joda.time.DateTime;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataAutomationService extends Service{

    public static Logger logger = Logger.getLogger("DataAutomationService");

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new CleanCacheTask().execute();
        new UpdateDownloadsTask().execute();
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class CleanCacheTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            File cacheDir = new File(getApplicationContext().getCacheDir().getAbsolutePath());
            File[] cachedFiles = cacheDir.listFiles();
            for(File f : cachedFiles) {
                DateTime dateModified = new DateTime(f.lastModified());
                DateTime yesterday = DateTime.now().minusDays(1);
                if(dateModified.isBefore(yesterday)) {
                    try {
                        f.delete();
                    }
                    catch (Exception ex) {
                        logger.log(Level.INFO, ex.getMessage());
                    }
                }
            }
            return null;
        }
    }

    private class UpdateDownloadsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            WebUtils w = new WebUtils();
            DateTime day = DateTime.now();
            DateFormat requestFormat = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat saveFormat = new SimpleDateFormat("MMddyyyy"); //TODO: Replace with static in CachingUtils class later

            for(int i = 0; i < 7; i++) {
                String requestDate = requestFormat.format(day.minusDays(i));
                try {
                    String menu = w.createMenuString("diningCommon", requestDate); //TODO: Replace "diningCommon" once merged with T-47
                    String saveDate = saveFormat.format(day.minusDays(i));
                    //TODO: Cache menus

                }
                catch (Exception ex) {
                    logger.log(Level.INFO, ex.getMessage());
                }
            }
            return null;
        }
    }



}
