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

    /**
     * onStartCommand is the sequence of actions followed when the service is started
     * @param intent the intent object that the service receives
     * @param flags not used
     * @param startId not used
     * @return Service.START_NOT_STICKY indicates the service will not attempt to restart itself
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new CleanCacheTask().execute();
        new UpdateDownloadsTask().execute();
        return Service.START_NOT_STICKY;
    }

    /**
     * onBind is a required method that will bind the service if desired
     * @param intent the intent object that the service receives
     * @return null prevents binding of the service
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * CleanCacheTask is a private inner class that is an AsyncTask to run
     * a cache-cleaning process in a separate thread.
     */
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


    /**
     * UpdateDownloadsTask is a private inner class that is an AsyncTask to run
     * a menu-downloading process in a separate thread.
     */
    private class UpdateDownloadsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            WebUtils w = new WebUtils();
            DateTime day = DateTime.now();
            DateFormat requestFormat = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat saveFormat = new SimpleDateFormat("MMddyyyy"); //TODO: Replace with static in CachingUtils class later

            for(int i = 0; i < 7; i++) {
                String requestDate = requestFormat.format(day.plusDays(i));
                try {
                    String menu = w.createMenuString("diningCommon", requestDate); //TODO: Replace "diningCommon" once merged with T-47
                    String saveDate = saveFormat.format(day.plusDays(i));
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
