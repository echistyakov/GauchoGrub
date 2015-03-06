package com.g10.gauchogrub.utils;

import android.content.Context;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheUtils {

    public final static Logger logger = Logger.getLogger("CachingUtils");

    private final static long MAX_SIZE = 5244880L;  // 5MB

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

    private static long getDirSize(File dir) {
        int size = 0;
        File[] files = dir.listFiles();
        for(File file : files) {
            if(file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }


    public static void deleteOldMenus(Context context){
        try {
            File file = new File(context.getCacheDir().getAbsolutePath());
            File[] files = file.listFiles();
            for (File f : files) {
                String name = f.getName();
                DateTime deleteFrom = new DateTime().minusDays(1);
                DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
                if(name.contains(dateFormat.format(deleteFrom))) {
                    if(!f.delete())
                        logger.log(Level.SEVERE, "Failed to delete " + f.getName() + " from cache");
                }
            }
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }

}
