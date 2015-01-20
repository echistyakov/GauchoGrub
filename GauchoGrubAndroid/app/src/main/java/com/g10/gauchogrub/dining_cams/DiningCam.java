package com.g10.gauchogrub.dining_cams;

import java.util.Date;

/**
 * DiningCams driver class.
 */
public class DiningCam {
    public final static String Carrillo = "http://diningcams.housing.ucsb.edu/Image/Carrillo?";
    public final static String DeLaGuerra = "http://diningcams.housing.ucsb.edu/Image/Carrillo?";
    public final static String Ortega = "http://diningcams.housing.ucsb.edu/Image/Carrillo?";
    public final static String Portolla = "http://diningcams.housing.ucsb.edu/Image/Carrillo?";
    private final static int delaySeconds = 2;

    private String url;

    public DiningCam(String diningCommon){
        this.url = diningCommon;
    }

    private String getCurrentImage(){
        // Returns a base URL concatinated with current UNIX time
        return this.url + new Date().getTime();
    }
}
