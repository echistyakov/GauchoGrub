package com.g10.gauchogrub.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class CacheUtils {

    private final static Logger logger = Logger.getLogger("CachingUtils");

    /**
     *
     */
    public CacheUtils() {
    }

    public boolean cacheFile(Context context, String fileName, String data) {
        try {
            File file = new File(context.getApplicationContext().getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data.getBytes());
            outputStream.close();
            return true;
        }
        catch (IOException ex) {
            logger.info(ex.getMessage());
        }
        return false;
   }

    /**
     *
     * @param context
     * @param fileName
     * @return
     */
    public String readCachedFile(Context context, String fileName) {
        FileInputStream inputStream;
        String line;
        try {
            File file = new File(context.getApplicationContext().getCacheDir(), fileName);
            inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(streamReader);
            StringBuilder builder = new StringBuilder();
            while ((line = bufferReader.readLine()) != null) {
                builder.append(line);
            }
            bufferReader.close();
            streamReader.close();
            inputStream.close();
            return builder.toString();
        } catch (IOException|NullPointerException ex) {
            return "";
        }
    }
}
