package com.g10.gauchogrub.dining_cams;

import android.graphics.Bitmap;

import com.g10.gauchogrub.utils.WebUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Logger;

/**
 * DiningCams driver class.
 */
public class DiningCam {

    private final static Logger logger = Logger.getLogger("DiningCam");

    private final static String BASE_CAM_URL = "http://diningcams.housing.ucsb.edu/Image/";
    public final static String CARRILLO = BASE_CAM_URL + "Carrillo?";
    public final static String DE_LA_GUERRA = BASE_CAM_URL + "DeLaGuerra?";
    public final static String ORTEGA = BASE_CAM_URL + "Ortega?";
    // No DiningCam for Portola

    private String partialUrl;
    private WebUtils web;

    /**
     * default constructor that takes in a diningCommon URL, specifying a particular diningCommon
     * @param diningCommonUrl
     */
    public DiningCam(String diningCommonUrl) {
        this.partialUrl = diningCommonUrl;
        this.web = new WebUtils();
    }

    /**
     * getCurrentImageUrl() is a private helper method to get the properly formed URL
     * to use when accessingthe database
     * @return
     */
    private URL getCurrentImageUrl() {
        try {
            // Base URL concatenated with current UNIX time
            return new URL(this.partialUrl + new Date().getTime());
        } catch (MalformedURLException e) {
            // Will never happen, but just in case...
            logger.info(e.toString());
            return null;
        }
    }

    /**
     * returns a bitmap image from the diningCommons
     * @param delay the amount of delay in milliseconds
     * @return a bitmap image of the line from a specific diningCommon
     */
    public Bitmap getCurrentImage(int delay) {
        URL currentUrl = this.getCurrentImageUrl();
        return web.getBitmap(currentUrl, delay);
    }
}