package com.g10.gauchogrub.utils;

import java.io.IOException;
import java.net.URL;

import com.g10.gauchogrub.utils.WebUtils.HttpMethod;

public class APIInterface {

    private WebUtils web = new WebUtils();
    public static String BASE_URL = "http://gauchogrub.azurewebsites.net/api";
    public static int DEFAULT_TIMEOUT = 20 * 1000;  // 20 seconds
    public static final String REQUEST_DATE_FORMAT = "MM/dd/yyyy";

    public String getMenuJson(String diningCommon, String date) throws IOException {
        //Make API Call
        String urlString = BASE_URL + "/Menus?diningCommon=" + diningCommon + "&date=" + date;
        URL url = new URL(urlString);
        return web.httpRequest(url, HttpMethod.GET, DEFAULT_TIMEOUT); // 20 second timeout
    }

    public void postRating(String userId, int menuId, int menuItemId, int rating) throws IOException {
        String urlString = BASE_URL + "/UserRatings?userId=" + userId + "&menuId=" + menuId + "&menuItemId=" + menuItemId + "&rating=" + rating;
        URL url = new URL(urlString);
        web.httpRequest(url, HttpMethod.POST, DEFAULT_TIMEOUT);
    }

    public String getRating(int menuItemID) throws IOException {
        String urlString = BASE_URL + "/Ratings?menuItemId=" + menuItemID;
        URL url = new URL(urlString);
        return web.httpRequest(url, HttpMethod.GET, DEFAULT_TIMEOUT);
    }
}
