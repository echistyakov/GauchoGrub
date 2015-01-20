package com.g10.gauchogrub.io;

import android.util.Pair;

import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class WebUtils{

    public String httpRequest(URL url, httpMethod method, HashMap<String, String> headers) throws IOException{
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        if(headers != null){
            for (Map.Entry <String, String> pair: headers.entrySet()) {
                connection.setRequestProperty(pair.getKey(), pair.getValue());
            }
        }

        connection.setRequestMethod(method.toString());
    }

    private String readResponse(HttpURLConnection connection){

    }

}