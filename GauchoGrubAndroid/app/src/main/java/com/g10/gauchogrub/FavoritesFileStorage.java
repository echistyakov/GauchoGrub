package com.g10.gauchogrub;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.logging.Logger;

/**
 * Created by VictorPorter on 2/28/15.
 * This class reads/writes from/to a HashSet and reads/writes from/to a file called favorites, that is stored within the Application
 */
public abstract class FavoritesFileStorage extends Fragment {

    public final static Logger logger = Logger.getLogger("FavoritesFragment");

    public boolean writeFavorites(HashSet<String> favoritesList) throws IOException {
        OutputStreamWriter outStream;
        try {
            outStream = new OutputStreamWriter(getActivity().getApplicationContext().openFileOutput("favorites.ser", Context.MODE_PRIVATE)); //new FileOutputStream(favoritesFile);
            for (String thisfavorite : favoritesList) {
                outStream.write(thisfavorite + "\n");
                logger.info("Writing String " + thisfavorite);
            }
            outStream.close();
        } catch (IOException e) { return false; }
        return true;
    }

    public HashSet<String> fillFavoritesList() throws IOException, NullPointerException {
        String tempFavorite = "";
        FileInputStream inStream = null;
        HashSet<String> favoritesList = new HashSet<>();

        try {
            inStream = getActivity().getApplicationContext().openFileInput("favorites.ser");
            if (inStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while ((tempFavorite = bufferedReader.readLine()) != null ) {
                    favoritesList.add(tempFavorite);
                    logger.info("Reading String " + tempFavorite);
                }

                inStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (NullPointerException e) {
            Log.e("login activity", "List reached end: " + e.toString());
        }
        return favoritesList;
    }


}