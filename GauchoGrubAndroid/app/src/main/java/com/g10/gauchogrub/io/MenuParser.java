package com.g10.gauchogrub.io;

import com.g10.gauchogrub.menu.DailyMenuList;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.logging.Logger;

public class MenuParser{

    public final static Logger logger = Logger.getLogger("MenuFragment");

    public DailyMenuList getDailyMenuList(String menuString) {

        DailyMenuList myDailyMenuList = new DailyMenuList("3","",0);
        try {
            //Initialize Parser Object
            JSONParser jsonParser = new JSONParser();

            //Parse API String into Event Array
            JSONArray eventList = (JSONArray) jsonParser.parse(menuString);
            int eventCount = eventList.size();

            //Obtain a reference to the first Event and get General Information
            JSONObject event = (JSONObject) eventList.get(0);
            JSONObject eventInfo = (JSONObject) event.get("Event");

            //Get Dining Common Name
            JSONObject diningCommonJ = (JSONObject) eventInfo.get("DiningCommon");
            String diningCommon = (String) diningCommonJ.get("Name");

            //Get Day of Week
            Long dayOfWeekL = (Long) eventInfo.get("DayOfWeek");
            int dayOfWeek = Integer.parseInt(dayOfWeekL.toString());

            //Get a Menu Date
            String date = (String) event.get("Date");
            date = date.split("T")[0];

            //Begin Creation of Data Structures for a single API CALL
            myDailyMenuList.setData(date,diningCommon,dayOfWeek);

            for(int x = 0; x < eventCount; x++) {
                JSONObject currentEvent = (JSONObject) eventList.get(x);
                Menu menu = getMenu(currentEvent);
                JSONArray menuItemList = (JSONArray) currentEvent.get("MenuItems");
                int count = menuItemList.size();

                //Create ArrayList for a single Menu at a Dining Common
                for (int i = 0; i < count; i++) {
                    JSONObject menuItem = (JSONObject) menuItemList.get(i);
                    MenuItem food = getMenuItem(menuItem);
                    menu.addMenuItem(food);
                }

                //Insert Menu into DailyMenuList
                myDailyMenuList.addMenu(menu);
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return myDailyMenuList;
    }

    public Menu getMenu(JSONObject currentEvent){
        JSONObject currentEventInfo = (JSONObject) currentEvent.get("Event");
        JSONObject menuJ = (JSONObject) currentEventInfo.get("Meal");
        String menuName = (String) menuJ.get("Name");

        return new Menu(menuName);
    }

    public MenuItem getMenuItem(JSONObject menuItem){
        JSONObject menuCategoryJ = (JSONObject) menuItem.get("MenuCategory");
        JSONObject menuItemTypeJ = (JSONObject) menuItem.get("MenuItemType");
        String title = (String) menuItem.get("Title");
        String menuCategory = (String) menuCategoryJ.get("Name");
        String menuItemType = (String) menuItemTypeJ.get("Name");
        MenuItem food = new MenuItem(title,menuCategory,menuItemType);

        return food;
    }



}