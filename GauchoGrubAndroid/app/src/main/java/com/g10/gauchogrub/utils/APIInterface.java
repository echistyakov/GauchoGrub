package com.g10.gauchogrub.utils;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Logger;

import com.g10.gauchogrub.utils.WebUtils.HttpMethod;

public class APIInterface {

    private final static Logger logger = Logger.getLogger("APIInterface");
    private WebUtils web = new WebUtils();
    private static String BASE_URL = "http://gauchogrub.azurewebsites.net/api/";
    private static int DEFAULT_TIMEOUT = 20 * 1000;  // 20 seconds
    private DateFormat dateFormat = new SimpleDateFormat(APIInterface.REQUEST_DATE_FORMAT);
    public static final String REQUEST_DATE_FORMAT = "MM/dd/yyyy";

    private final String MENUS_PATH = "Menus";
    private final String USER_RATINGS_PATH = "UserRatings";
    private final String RATINGS_PATH = "Ratings";

    /**
     * getMenuJson() queries the database, resulting in a string containing a set of JSON
     * menus for that entire day
     * @param diningCommon the name of the diningCommon related to the menus being looked up
     * @param date the date of the menu being looked up
     * @return a String containing a JSON object
     * @throws IOException
     */
    public String getMenuJson(String diningCommon, String date) throws IOException {
        Hashtable<String, String> queryMap = new Hashtable<>();
        queryMap.put("diningCommon", diningCommon);
        queryMap.put("date", date);

        String urlString = BASE_URL + MENUS_PATH + "?" + WebUtils.toQuery(queryMap);
        URL url = new URL(urlString);
        return web.httpRequest(url, HttpMethod.GET, DEFAULT_TIMEOUT); // 20 second timeout
    }

    /**
     * postRating() posts a rating to the database
     * @param userId the unique userID for each android device
     * @param menuId the ID of the specific menu that contains the menuitem being rated
     * @param menuItemId the ID of the menuItem being rated
     * @param rating the user's rating of the menuItem
     * @throws IOException
     */
    public void postRating(String userId, int menuId, int menuItemId, int rating) throws IOException {
        Hashtable<String, String> queryMap = new Hashtable<>();
        queryMap.put("userId", userId);
        queryMap.put("menuId", Integer.toString(menuId));
        queryMap.put("menuItemId", Integer.toString(menuItemId));
        queryMap.put("rating", Integer.toString(rating));

        String urlString = BASE_URL + USER_RATINGS_PATH + "?" + WebUtils.toQuery(queryMap);
        URL url = new URL(urlString);
        web.httpRequest(url, HttpMethod.POST, DEFAULT_TIMEOUT);
    }

    /**
     * getRating() gets a specific rating for an individual menuItem
     * @param menuItemID the ID of the relevant menuItem
     * @return a JSON string containing the rating information for the individual menuItem
     * @throws IOException
     */
    public String getRating(int menuItemID) throws IOException {
        Hashtable<String, String> queryMap = new Hashtable<>();
        queryMap.put("menuItemId", Integer.toString(menuItemID));

        String urlString = BASE_URL + RATINGS_PATH + "?" + WebUtils.toQuery(queryMap);
        URL url = new URL(urlString);
        return web.httpRequest(url, HttpMethod.GET, DEFAULT_TIMEOUT);
    }
}
