package com.g10.gauchogrub;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g10.gauchogrub.FavoritesList;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.logging.Logger;


public class FavoritesFragment extends Fragment {

    HashSet<String> favoritesList;
    public final static Logger logger = Logger.getLogger("FavoritesFragment");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);

        favoritesList = new HashSet<>();

        try {
            fillFavoritesList();
        } catch (IOException e){ e.printStackTrace();
        } catch (NullPointerException e) { e.printStackTrace(); }

        TableLayout displayedFavorites = (TableLayout)rootView.findViewById(R.id.favorites_table);

        for(String favorite : favoritesList) {
            TableRow favoriteRow = new TableRow(getActivity().getApplicationContext());
            favoriteRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_category, null);

            TextView favoriteView = (TextView)entryView.findViewById(R.id.meal_cat);
            favoriteView.setText(favorite);

            favoriteRow.addView(entryView);
            try {
                displayedFavorites.addView(favoriteRow);
            } catch(NullPointerException e){ logger.info("Null Row"); }
        }

        return rootView;
    }

    public void fillFavoritesList() throws IOException, NullPointerException {
        String tempFavorite = "";
        FileInputStream inStream = null;

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
    }

}