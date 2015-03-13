package com.g10.gauchogrub.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.g10.gauchogrub.BaseActivity;
import com.g10.gauchogrub.R;
import com.g10.gauchogrub.menu.DiningCommon;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import com.g10.gauchogrub.utils.CacheUtils;
import com.g10.gauchogrub.utils.FileIOUtils;
import com.g10.gauchogrub.utils.MenuParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

public class NotificationService extends Service {

    public static Logger logger = Logger.getLogger("NotificationService");

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new NotificationTask().execute();
        return Service.START_NOT_STICKY;
    }

    /**
     * NotificationTask is an AsyncTask that will run the notification data retrieval and notify the user.
     */
    private class NotificationTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Creates string with all favorites that appear today, names, dining commons, and meals
            // in format; "MenuItemName is being served at DiningCommonName during MealName"
            HashMap<String, ArrayList<String>> favorites = getFavoritesToday();
            NotificationCompat.Builder builder;
            int i = 0;
            if (favorites.size() <= 0) {
                stopSelf();
            }
            // Sends out up to 5 individual notifications (don't want to send the user
            // too many notifications)
            Intent baseIntent = new Intent(getBaseContext(), BaseActivity.class);
            PendingIntent bIntent = PendingIntent.getActivity(getBaseContext(), 0,
                    baseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            for (String meal : favorites.keySet()) {
                NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
                for (String line : favorites.get(meal)) {
                    style.addLine(line + "\n");
                    logger.info(line);
                }
                builder = new NotificationCompat.Builder(getBaseContext())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setGroup(meal)
                        .setGroupSummary(true)
                        .setContentTitle("Favorites Today - " + meal)
                        .setContentText("Expand for details")
                        .setStyle(style.setBigContentTitle("Favorites Today - " + meal));
                builder.setContentIntent(bIntent);
                NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nManager.notify(i++, builder.build());
            }
            stopSelf();
            return null;
        }
    }

    /**
     * Creates a HashMap where each key corresponds to a Meal name, and its value is an ArrayList of all notification strings for that meal.
     *
     * @return a HashMap of strings mapped to ArrayLists of strings.
     */
    private HashMap<String, ArrayList<String>> getFavoritesToday() {
        // Gets all cached files
        CacheUtils c = new CacheUtils();
        FileIOUtils fio = new FileIOUtils();
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
        File file = new File(getApplicationContext().getCacheDir().getAbsolutePath());
        File[] files = file.listFiles();
        // List of DiningCommons
        HashMap<String, ArrayList<String>> notifications = new HashMap<>();
        try {
            for (int i = 0; i < 4; i++) {
                HashSet<String> favorites = fio.fillFavoritesList(getBaseContext(),DiningCommon.DATA_USE_DINING_COMMONS[i]);
                // For each stored file
                for (File f : files) {
                    // if JSON File is for today && right diningCommon
                    if (f.getName().contains(dateFormat.format(date)) &&
                            f.getName().contains(DiningCommon.DATA_USE_DINING_COMMONS[i])) {
                        // Parse Menus
                        MenuParser menuParser = new MenuParser();
                        ArrayList<Menu> menus = menuParser.getDailyMenuList(c.readCachedFile(this, f.getName()));
                        // For each Menu in JSON File
                        for (Menu m : menus) {
                            // For each menuItem in menu
                            for (MenuItem menuItem : m.menuItems) {
                                // For each favoritedItem
                                for (String favorite : favorites) {
                                    // Checks that the item is in the favorites
                                    if (menuItem.title.equals(favorite)) {
                                        // Adds to the ArrayList
                                        String mealName = m.event.meal.name;
                                        String addString = favorite + " - " + DiningCommon.READABLE_DINING_COMMONS[i];
                                        // For first item of that meal type
                                        if (!notifications.containsKey(m.event.meal.name)) {
                                            notifications.put(mealName, new ArrayList<String>());
                                        }
                                        notifications.get(mealName).add(addString);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return notifications;
    }


    /**
     * onBind is a required method that will bind the service if desired
     *
     * @param intent the intent object that the service receives
     * @return null prevents binding of the service
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
