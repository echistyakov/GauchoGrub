package com.g10.gauchogrub;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    TableLayout ratingsTable;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_fragment, container, false);
        allRankings = new ArrayList<>();
        ratingsTable = (TableLayout)rootView.findViewById(R.id.rankingsTable);

        int maxBreakfastRating, maxLunchRating, maxDinnerRating;
        getTodaysRankings();

        return rootView;
    }

    public void inflateRatingsTable(ArrayList<ArrayList<Integer>> allRatings){
        final View ratingView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.top_ratings, null);
        logger.info("entered inflate ratings table");

        TableRow bestBreakFastRow = (TableRow)ratingView.findViewById(R.id.bestMeal);

        TextView bestBreakFastView = new TextView(getActivity().getApplicationContext());
        bestBreakFastView.setText("Hello");
        bestBreakFastRow.addView(bestBreakFastView);
        ratingsTable.addView(ratingView);

    }

    public void getTodaysRankings() {
        new AsyncTask<Void, Void, ArrayList<ArrayList<Integer>>>() {
            @Override
            protected ArrayList<ArrayList<Integer>> doInBackground(Void... v) {
                    for(int i = 0; i <=3; i++) {
                        String menuString;
                        try {
                            menuString = w.createMenuString(commons[i], "03/10/2015");
                        } catch (Exception e) { e.printStackTrace(); menuString = "";
                        logger.info("caught api exception");}

                        ArrayList<Menu> todaysMenus = mp.getDailyMenuList(menuString);
                        ArrayList<Integer> todaysRankings = getRankings(todaysMenus);
                        allRankings.add(todaysRankings);
                    }
                    return allRankings;
            }
            @Override
            protected void onPostExecute(ArrayList<ArrayList<Integer>> result) {
                logger.info("entered post execute");
                inflateRatingsTable(result);
            }
        }.execute();

    }

    public ArrayList<Integer> getRankings(ArrayList<Menu> menus) {
        int totalRating = 0, itemCount = 0;
        ArrayList<Integer> dayRatings = new ArrayList<>();
        if(menus == null){ return dayRatings; }

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
