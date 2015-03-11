package com.g10.gauchogrub;

public class AndroidId {
    private static String android_id;

    public AndroidId(){}

    public AndroidId(String id){
        android_id = id;
    }
    public String getAndroidId(){
        return android_id;
    }
}
