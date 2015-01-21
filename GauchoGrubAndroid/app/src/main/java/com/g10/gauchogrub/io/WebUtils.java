package com.g10.gauchogrub.io;


import java.io.BufferedReader;
import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebUtils{

    public static final int DEFAULT_TIMEOUT = 10000;

    /**
     *
     * @param url
     * @param method
     * @param timeout
     * @param headers
     * @return
     * @throws IOException
     */
    public String httpRequest(URL url, httpMethod method, int timeout, HashMap<String, String> headers) throws IOException{
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        if(headers != null){
            for (Map.Entry <String, String> pair: headers.entrySet()) {
                connection.setRequestProperty(pair.getKey(), pair.getValue());
            }
        }

        connection.setRequestMethod(method.toString());
        if(timeout == 0){
            connection.setConnectTimeout(DEFAULT_TIMEOUT);
            connection.setReadTimeout(DEFAULT_TIMEOUT);
        }
        else{
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
        }
        connection.connect();
        return readResponse(connection);
    }

    /**
     *
     * @param connection
     * @return
     * @throws IOException
     */
    private String readResponse(HttpURLConnection connection) throws IOException{
        int responseCode = connection.getResponseCode();
        String response = "";
        if (responseCode == 200 || responseCode == 204){ //OK or no content
            response = readInputStream(connection.getInputStream());
            connection.disconnect();
        }
        else {
            response = readInputStream(connection.getErrorStream());
            connection.disconnect();
            throw new httpException(response);
        }
        return response;
    }

    /* Reads data (String) from an input stream */

    /**
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private String readInputStream(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return response.toString();
    }

}