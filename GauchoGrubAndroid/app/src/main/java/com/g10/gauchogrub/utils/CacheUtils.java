package com.g10.gauchogrub.utils;

import android.content.Context;
import android.net.http.HttpResponseCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CacheUtils {

    public final static Logger logger = Logger.getLogger("CachingUtils");

    Context context;
    String fileName;
    String data;

    public CacheUtils(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public CacheUtils(Context context, String fileName, String data) {
        this.context = context;
        this.fileName = fileName;
        this.data = data;
    }

    //date = MMDDYYYY
    public void cacheFile() {
        Thread workThread = new Thread() {
            public void run() {
                File file = new File(context.getCacheDir(), fileName);
                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    outputStream.write(data.getBytes());
                }
                catch (IOException ex) {
                    logger.log(Level.INFO, ex.getMessage());
                }
            }
        };
        workThread.start();
    }

    public void deleteOldMenus(){

    }

    public String readCachedFile() {
        File file = new File(context.getCacheDir(), fileName);
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
    }


}
