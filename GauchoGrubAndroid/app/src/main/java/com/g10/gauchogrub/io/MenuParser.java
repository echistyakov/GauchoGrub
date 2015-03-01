package com.g10.gauchogrub.io;

import com.g10.gauchogrub.menu.DailyMenuList;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import java.util.logging.Logger;
import java.lang.reflect.Type;
import com.google.gson.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class MenuParser{

    public final static Logger logger = Logger.getLogger("MenuFragment");

    public DailyMenuList getDailyMenuList(String menuString) {

        DailyMenuList myDailyMenuList = new DailyMenuList("3","",0);
        try {


            myDailyMenuList = deserialize(menuString,DailyMenuList.class);

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println(myDailyMenuList.toString());
        return myDailyMenuList;
    }


    /* Deserializes given JSON into an object of type T */
    public static <T> T deserialize(String json, Type type) {
        Gson gson = getCustomGson();
        return gson.fromJson(json, type);
    }

    /* Custom Gson for (de)serializing custom classes */
    public static Gson getCustomGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }




}