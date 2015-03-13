package com.g10.gauchogrub.menu;

public class DiningCommon extends NamedEnum {

    public static final String DATA_USE_CARILLO = "Carillo";
    public static final String DATA_USE_DLG = "De%20La%20Guerra";
    public static final String DATA_USE_ORTEGA = "ORTEGA";
    public static final String DATA_USE_PORTOLA = "Portola";
    public static final String[] DATA_USE_DINING_COMMONS = {DATA_USE_CARILLO, DATA_USE_DLG, DATA_USE_ORTEGA, DATA_USE_PORTOLA};

    public final static String READABLE_CARILLO = "Carillo";
    public final static String READABLE_DLG = "DLG";
    public final static String READABLE_ORTEGA = "ORTEGA";
    public final static String READABLE_PORTOLA = "Portola";
    public final static String[] READABLE_DINING_COMMONS = {READABLE_CARILLO, READABLE_DLG, READABLE_ORTEGA, READABLE_PORTOLA};

    public enum DiningCommons {
        DLG, Carillo, Portola, Ortega
    }
}
