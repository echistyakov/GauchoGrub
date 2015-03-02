package com.g10.gauchogrub.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebUtils {

    public static final int BASIC_TIMEOUT = 60 * 1000;        // 60 seconds
    public static final int DOWNLOAD_TIMEOUT = 5 * 60 * 1000; // 5 minutes
    public final static Logger logger = Logger.getLogger("WebUtils");

    public Bitmap getDrawable(URL url, int timeout){
        HttpURLConnection connection = null;
        Bitmap image = null;
        try {
            logger.info(url.toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.connect();

            byte[] response = readByteStream(connection.getInputStream());
            logger.info("GET image/jpeg: " + response.length + " bytes");
            image = BitmapFactory.decodeByteArray(response, 0, response.length);
        } catch (SocketTimeoutException e) {
            return null;
        } catch(IOException e){
            return null;
        } finally {
            connection.disconnect();
        }
        return image;
    }

    /* A synchronous method that performs an HTTP request returning data received from the sever as a String */
    public String httpRequest(URL url, HttpMethod method, int timeout) throws IOException {
        return httpRequest(url, method, timeout, new Hashtable<String, String>());
    }

    public String httpRequest(URL url, HttpMethod method, int timeout, Hashtable<String, String> headers) throws IOException {
        log(url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (headers != null) {
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
        connection.setRequestMethod(method.toString());
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.connect();

        return readResponse(connection);
    }

    /* Helper method */
    private String readResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == 200 || responseCode == 204) {
            String response = readInputStream(connection.getInputStream());
            connection.disconnect();
            return response;
        } else {
            throw new RuntimeException("Bad request");
        }
    }

    /* Reads data (String) from an input stream */
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

    /* Reads data (byte[]) from an input stream */
    private byte[] readByteStream(InputStream stream) throws IOException {
        BufferedInputStream reader = new BufferedInputStream(stream);
        ByteArrayBuffer response = new ByteArrayBuffer(100000);
        byte[] buffer = new byte[1024];
        int length;
        while((length = reader.read(buffer)) != -1){
            response.append(buffer, 0, length);
        }
        reader.close();
        return response.toByteArray();
    }

    /* Logs the URL */
    private void log(URL url) {
        logger.log(Level.INFO, url.toString());
    }

    public String createMenuString(String diningCommon, String date) throws Exception{
        //Make API Call
        String ur = "http://gauchogrub.azurewebsites.net/api/Menus?diningCommon=" + diningCommon + "&date=" + date;
        URL url = new URL(ur);
        String result = httpRequest(url,HttpMethod.GET,100000);
        return result;
    }

    public enum HttpMethod {
        GET, POST, DELETE, PUT
    }
}