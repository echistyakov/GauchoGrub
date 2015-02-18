package com.g10.gauchogrub.io;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.g10.gauchogrub.menu.DayMenu;
import com.g10.gauchogrub.menu.Meal;
import com.g10.gauchogrub.menu.MenuItem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.ArrayList;
import com.g10.gauchogrub.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.logging.Logger;

public class MenuParser{

    public final static Logger logger = Logger.getLogger("MenuFragment");

    public DayMenu getDayMenu(String menuString) {

        DayMenu myDayMenu = new DayMenu("3","",0);
        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray eventList = (JSONArray) jsonParser.parse(menuString);
            int eventCount = eventList.size();

            for(int x = 0; x < eventCount; x++) {
                // Retrieve a single Event from an API call
                JSONObject event = (JSONObject) eventList.get(x);
                JSONObject eventInfo = (JSONObject) event.get("Event");

                //Get Dining Common Name
                JSONObject diningCommonJ = (JSONObject) eventInfo.get("DiningCommon");
                String diningCommon = (String) diningCommonJ.get("Name");

                //Get Day of Week
                Long dayOfWeekL = (Long) eventInfo.get("DayOfWeek");
                int dayOfWeek = Integer.parseInt(dayOfWeekL.toString());

                //Get a meal Name
                JSONObject mealJ = (JSONObject) eventInfo.get("Meal");
                String mealName = (String) mealJ.get("Name");

                //Get a meal Date
                String date = (String) event.get("Date");
                date = date.split("T")[0];

                //Begin Creation of Data Structures for a single API CALL
                myDayMenu.setData(date,diningCommon,dayOfWeek);
                Meal meal = new Meal(mealName);

                //parse MenuItem array
                JSONArray menuItemList = (JSONArray) event.get("MenuItems");
                int count = menuItemList.size();

                //Create ArrayList for a single meal at a Dining Common
                for (int i = 0; i < count; i++) {
                    JSONObject menuItem = (JSONObject) menuItemList.get(i);
                    JSONObject menuCategoryJ = (JSONObject) menuItem.get("MenuCategory");
                    JSONObject menuItemTypeJ = (JSONObject) menuItem.get("MenuItemType");
                    String title = (String) menuItem.get("Title");
                    String menuCategory = (String) menuCategoryJ.get("Name");
                    String menuItemType = (String) menuItemTypeJ.get("Name");
                    MenuItem food = new MenuItem(title,menuCategory,menuItemType);
                    meal.addMenuItem(food);
                }

                //Insert meal into DayMenu
                myDayMenu.addMeal(meal);
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return myDayMenu;
    }

}