package com.g10.gauchogrub;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import com.g10.gauchogrub.utils.MenuParser;
import com.g10.gauchogrub.utils.WebUtils;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainMenuFragment extends Fragment {

    public final static Logger logger = Logger.getLogger("MainMenuFragment");
    ArrayList<ArrayList<Integer>> allRankings;
    String[] commons = new String[] {"Carillo","De_La_Guerra","Ortega","Portola"};
    WebUtils w = new WebUtils();
    MenuParser mp = new MenuParser();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_fragment, container, false);
        allRankings = new ArrayList<>();

        //for(int i = 0; i <= 3; i++){
        //    getTodaysRankings(commons[i]);
        // }
        getTodaysRankings("Carillo");
        //logger.info("Breakfast Rating at Carillo = " + allRankings.get(0).get(0));

        return rootView;
    }

    public void getTodaysRankings(final String diningCommon) {
        new AsyncTask<Void, Void, ArrayList<Integer>>() {
            @Override
            protected ArrayList<Integer> doInBackground(Void... v) {
                try {
                    String menuString = w.createMenuString(diningCommon, "03/10/2015");
                    ArrayList<Menu> todaysMenus = mp.getDailyMenuList(menuString);
                    logger.info("Got Menu List");
                    ArrayList<Integer> todaysRankings = getRankings(todaysMenus);
                    logger.info("Got todays Rankings");
                    return todaysRankings;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(ArrayList<Integer> todaysRankings) {
                addRanking(todaysRankings);
            }

        }.execute();

    }

    public ArrayList<Integer> getRankings(ArrayList<Menu> menus) {
        int totalRating = 0, itemCount = 0;
        ArrayList<Integer> dayRatings = new ArrayList<>();
        for(Menu menu : menus){
            for(MenuItem item : menu.menuItems) {
                itemCount++;
                totalRating = totalRating + getItemRating(item);
            }
            dayRatings.add((totalRating/itemCount));
            itemCount = 0;
            totalRating = 0;
        }
        return dayRatings;
    }

    public int getItemRating(MenuItem item){
        int totalPositiveRatings = item.totalPositiveRatings;
        int totalRating = item.totalRatings;
        int negativeRatings = totalRating - totalPositiveRatings;
        return totalPositiveRatings - negativeRatings;
    }

    public void addRanking(ArrayList<Integer> todaysRanking){
        allRankings.add(todaysRanking);
        logger.info("Added new day rankings: ");
        for(int i = 0; i < todaysRanking.size(); i++){
            logger.info(todaysRanking.get(i) + "");
        }
    }

}
