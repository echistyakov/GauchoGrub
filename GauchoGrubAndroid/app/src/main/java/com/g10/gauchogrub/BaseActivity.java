package com.g10.gauchogrub;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.g10.gauchogrub.dining_cams.DiningCamsFragment;
import com.g10.gauchogrub.notification.NotificationService;
import com.g10.gauchogrub.utils.CacheUtils;

import java.util.logging.Logger;

/*
 * BaseActivity - is the main activity that gets launched.
 * It contains a navigation drawer that wraps Fragments.
 * Based on Android tutorial: "Creating a Navigation Drawer"
 */
public class BaseActivity extends ActionBarActivity {

    private static String[] navDrawerArray;
    private static DrawerLayout navDrawerLayout;
    private static ListView navDrawerList;
    private static ActionBarDrawerToggle navDrawerToggle;
    private static CharSequence navTitle;
    private static CharSequence navDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        // Create a drawer
        navTitle = navDrawerTitle = getTitle();
        navDrawerArray = getResources().getStringArray(R.array.nav_drawer_array);
        navDrawerLayout = (DrawerLayout) findViewById(R.id.nav_drawer_layout);
        navDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        navDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, navDrawerArray));
        // Set the list's click listener
        navDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                navDrawerLayout,       /* DrawerLayout object */
                //R.drawable.ic_drawer,/* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(navTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(navDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        navDrawerLayout.setDrawerListener(navDrawerToggle);
        if(!notificationServiceRunning()) {
            Intent serviceIntent = new Intent("com.g10.gauchogrub.notification.NotificationService");
            this.startService(serviceIntent);
        }
        CacheUtils.deleteOldMenus(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        navDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }  else {
            return super.onOptionsItemSelected(item) ||
                   navDrawerToggle.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        navTitle = title;
        getSupportActionBar().setTitle(title);
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).info(String.format("Item {} selected in NavDrawer", position));
        // Create a new fragment
        Fragment fragment = null;
        if (position == 0) {
            fragment = new MenuFragment();
        } else if (position == 1){
            fragment = new ScheduleFragment();
        } else if (position == 2){
            fragment = new DiningCamsFragment();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        navDrawerList.setItemChecked(position, true);
        setTitle(navDrawerArray[position]);
        navDrawerLayout.closeDrawer(navDrawerList);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private boolean notificationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}