package com.g10.gauchogrub.utils;

import android.content.Context;
import android.net.http.HttpResponseCache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheUtils {

    public final static Logger logger = Logger.getLogger("CachingUtils");

    public CacheUtils() {
    }

    //date = MMDDYYYY
    public boolean cacheFile(Context context, String fileName, String data) {
        try {
            File file = new File(context.getApplicationContext().getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data.getBytes());
            outputStream.close();
            return true;
        }
        catch (IOException ex) {
            logger.log(Level.INFO, ex.getMessage());
        }
        return false;
   }

    public void deleteOldMenus(){

    }

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
        } catch (FileNotFoundException ex) {
            return "";
        } catch (IOException ex) {
            return "";
        } catch (NullPointerException ex) {
            return "";
        }
    }


        /*File file = new File(context.getCacheDir(), fileName);
        if(file.exists() && file.isFile()) {
            char c;
            StringBuffer buffer = new StringBuffer();
            try {
                FileReader reader = new FileReader(file);
                while ( (c = (char) reader.read()) != -1) {
                    buffer.append(c);
                }
            }
            catch (Exception ex) {
                logger.log(Level.INFO, ex.getMessage());
            }
            return buffer.toString();
        }
        return "";
    }*/


}
