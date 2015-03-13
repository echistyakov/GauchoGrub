package com.g10.gauchogrub.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * FileIOUtils is a class which handles writing files to the filesystem outside of the cache
 */
public class FileIOUtils {

    Logger logger = Logger.getLogger(FileIOUtils.class.getName());

    /**
     * writeFavorites writes favorites to a file.
     * @param context is an application context
     * @param favorites a HashSet of strings
     * @param diningCommon the name of the diningCommon the favorites hashset corresponds to
     * @return true if written, false if not written
     */
    public boolean writeFavorites(Context context, HashSet<String> favorites, String diningCommon) {
        OutputStreamWriter outStream;
        try {
            logger.info("Writing favorites to file");
            outStream = new OutputStreamWriter(context.openFileOutput("favorites_" + diningCommon, Context.MODE_PRIVATE)); //new FileOutputStream(favoritesFile);
            for (String favorite : favorites) {
                outStream.write(favorite + "\n");
            }
            outStream.close();
        } catch (IOException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * fillFavoritesList returns a HashSet of Strings containing the Favorites
     * @param context is an application context
     * @param diningCommon the name of the diningCommon the favorites hashset corresponds to
     * @return a hashset of strings containing favorites
     */
    public HashSet<String> fillFavoritesList(Context context, String diningCommon) {
        String tempFavorite;
        FileInputStream inStream;
        HashSet<String> favoritesList = new HashSet<>();

        try {
            logger.info("Reading favorites from file");
            inStream = context.openFileInput("favorites_" + diningCommon);
            if (inStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while ((tempFavorite = bufferedReader.readLine()) != null) {
                    favoritesList.add(tempFavorite);
                }
                inStream.close();
            }
        } catch (FileNotFoundException e) {
            logger.info("File not found: " + e.toString());
        } catch (IOException e) {
            logger.info("Can not read file: " + e.toString());
        } catch (NullPointerException e) {
            logger.info("List reached end: " + e.toString());
        }
        return favoritesList;
    }
}
