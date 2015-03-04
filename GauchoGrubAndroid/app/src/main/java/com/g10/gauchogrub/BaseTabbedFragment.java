package com.g10.gauchogrub;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost.TabContentFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BaseTabbedFragment extends Fragment {


    public final static Logger logger = Logger.getLogger("BaseTabbedFragment");

    public abstract void setDisplayContent(int tag);

    public abstract TabContentFactory createTabContent();

    public void setUpTabs(TabHost tabs, TabContentFactory contentCreate, int numTabs){
        String[] commons = new String[] {"Carillo","DLG","Ortega","Portola"};
        tabs.setup();
        //Create tabs and set text & content
        for(int i = 0; i < numTabs ; i ++) {
            TabSpec tab = tabs.newTabSpec(i + "");
            tab.setContent(contentCreate);
            tab.setIndicator(commons[i]);
            tabs.addTab(tab);
        }
        //Set tab listeners to change content when triggered
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                setDisplayContent(Integer.parseInt(tabId));
            }});
    }


    public boolean writeFavorites(HashSet<String> favoritesList, String diningCommon) throws IOException {
        OutputStreamWriter outStream;
        try {
            outStream = new OutputStreamWriter(getActivity().getApplicationContext().openFileOutput("favorites" + diningCommon, Context.MODE_PRIVATE)); //new FileOutputStream(favoritesFile);
            for (String thisfavorite : favoritesList) {
                outStream.write(thisfavorite + "\n");
                logger.info("Writing String " + thisfavorite);
            }
            outStream.close();
        } catch (IOException e) { return false; }
        return true;
    }

    public HashSet<String> fillFavoritesList(String diningCommon) throws IOException, NullPointerException {
        String tempFavorite = "";
        FileInputStream inStream = null;
        HashSet<String> favoritesList = new HashSet<>();

        try {
            inStream = getActivity().getApplicationContext().openFileInput("favorites" + diningCommon);
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
            logger.log(Level.INFO, "login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            logger.log(Level.INFO, "login activity", "Can not read file: " + e.toString());
        } catch (NullPointerException e) {
            logger.log(Level.INFO, "login activity", "List reached end: " + e.toString());
        }
        return favoritesList;
    }

    public enum DiningCommons {
        DLG, Carillo, Portola, Ortega
    }
}
