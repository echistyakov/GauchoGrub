package com.g10.gauchogrub.utils;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.lang.reflect.Type;

import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.utils.gson.DateTimeDeserializer;
import com.g10.gauchogrub.utils.gson.PeriodDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class MenuParser {

    private final static Logger logger = Logger.getLogger("MenuParser");
    public static final Type listMenuType = new TypeToken<ArrayList<Menu>>() {}.getType();

    public ArrayList<Menu> getDailyMenuList(String menuString) {
        try {
            return deserialize(menuString, listMenuType);
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }

    /* Deserializes given JSON into an object of type T */
    public static <T> T deserialize(String json, Type type) {
        logger.info("Deserializing type " + type.toString());
        Gson gson = getCustomGson();
        return gson.fromJson(json, type);
    }

    /* Custom Gson for (de)serializing custom classes */
    public static Gson getCustomGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Period.class, new PeriodDeserializer())
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .setPrettyPrinting().create();
    }
}