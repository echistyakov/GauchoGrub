package com.g10.gauchogrub.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.g10.gauchogrub.BaseActivity;
import com.g10.gauchogrub.BaseTabbedFragment;
import com.g10.gauchogrub.R;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import com.g10.gauchogrub.utils.CacheUtils;
import com.g10.gauchogrub.utils.MenuParser;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationService extends Service {

    public static Logger logger = Logger.getLogger("NotificationService");
    private PowerManager.WakeLock myWakeLock;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return Service.START_NOT_STICKY;
    }

    private void handleIntent(Intent intent) {
        //Gets the wakelock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        myWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "gauchogrub.notification");
        myWakeLock.acquire();

        //Runs task in AsyncTask
        new NotificationTask().execute();
    }


    private class NotificationTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //Creates string with all favorites that appear today, names, dining commons, and meals
            //in format; "MenuItemName is being served at DiningCommonName during MealName"
            ArrayList<String> favorites = getFavoritesToday();
            String fullMessage = "";
            for (String s : favorites) {
                fullMessage = fullMessage.concat(s + "\n");
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Favorite Foods Served Today")
                    .setContentText("")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(fullMessage));
            Intent targetIntent = new Intent(getBaseContext(), BaseActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0,
                    targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(1, builder.build());
            stopSelf();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            stopSelf();
        }
    }

    private ArrayList<String> getFavoritesToday() {
        //Gets all cached files
        CacheUtils c = new CacheUtils();
        DateTime date = new DateTime();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
        File file = new File(getBaseContext().getApplicationContext().getCacheDir().toURI());
        File[] files = file.listFiles();

        //List of DiningCommons
        ArrayList<String> notifications = new ArrayList<String>();
        for(int i = 0; i < 4; i++) {
            HashSet<String> favorites = fillFavoritesList(BaseTabbedFragment.diningCommons[i]);
            //For each cached file
            for (File f : files) {
                //if JSON File is for today && right diningCommon
                if (f.getName().contains(dateFormat.format(date)) &&
                        f.getName().contains(BaseTabbedFragment.diningCommons[i])) {
                    //Parse Menus
                    MenuParser menuParser = new MenuParser();
                    ArrayList<Menu> menus = menuParser.getDailyMenuList(c.readCachedFile(this, f.getName()));
                    //For each Menu in JSON File
                    for (Menu m : menus) {
                        //For each menuItem in menu
                        for (MenuItem menuItem : m.menuItems) {
                            //For each favoritedItem
                            for (String favorite : favorites) {
                                //Checks that the item is in the favorites
                                if (menuItem.title.equals(favorite)) {
                                    //Adds to the ArrayList
                                    notifications.add("Favorite " + favorite
                                            + " is being served at " + BaseTabbedFragment.diningCommons[i]
                                            + " during " + m.event.meal);
                                }
                            }
                        }
                    }
                }
            }
        }
        return notifications;
    }

    public HashSet<String> fillFavoritesList(String diningCommon) {
        String tempFavorite = "";
        FileInputStream inStream = null;
        HashSet<String> favoritesList = new HashSet<>();

        try {
            inStream = getApplicationContext().openFileInput("favorites" + diningCommon);
            if (inStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while ((tempFavorite = bufferedReader.readLine()) != null ) {
                    favoritesList.add(tempFavorite);
                    logger.info("Reading String " + tempFavorite);
                }
                inStream.close();
            }
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, "login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            logger.log(Level.INFO, "login activity", "Can not read file: " + e.toString());
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "login activity", "List reached end: " + e.toString());
        }
        return favoritesList;
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
