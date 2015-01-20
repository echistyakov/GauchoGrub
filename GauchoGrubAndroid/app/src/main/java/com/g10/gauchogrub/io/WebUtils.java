package com.g10.gauchogrub.io;

import android.util.Pair;

import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Dictionary;
import java.util.Enumeration;

public class WebUtils{

    public String httpRequest(URL url, HttpMethod method, Dictionary<String, String> headers) throws IOException{
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        if(headers != null){
            for (Pair<String, String> pair: headers) {
                connection.setRequestProperty(pair.key, pair.value);
            }
        }

        connection.setRequestMethod(method.toString());
    }

    private String readResponse(HttpURLConnection connection){

    }

}