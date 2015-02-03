package com.g10.gauchogrub.dining_cams;

import android.graphics.Bitmap;
import com.g10.gauchogrub.io.WebUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DiningCams driver class.
 */
public class DiningCam {

    public final static String Carrillo = "http://diningcams.housing.ucsb.edu/Image/Carrillo?";
    public final static String DeLaGuerra = "http://diningcams.housing.ucsb.edu/Image/DeLaGuerra?";
    public final static String Ortega = "http://diningcams.housing.ucsb.edu/Image/Ortega?";
    // There is no DiningCam for Portolla

    public final static Logger logger = Logger.getLogger("DiningCam");

    private String partialUrl;
    private WebUtils web;

    public DiningCam(String diningCommonUrl) {
        this.partialUrl = diningCommonUrl;
        this.web = new WebUtils();
    }

    private URL getCurrentImageUrl(){
        // Base URL concatenated with current UNIX time
        try {
            return new URL(this.partialUrl + new Date().getTime());
        } catch(MalformedURLException e) {
            // Will never happen, but just in case...
            logger.log(Level.INFO, e.toString());
            return null;
        }
    }

    public Bitmap getCurrentImage(int delay) {
        URL currentUrl = this.getCurrentImageUrl();
        return web.getDrawable(currentUrl, delay);
    }
}
