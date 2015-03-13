package com.g10.gauchogrub;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g10.gauchogrub.menu.DiningCommon;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import com.g10.gauchogrub.utils.APIInterface;
import com.g10.gauchogrub.utils.MenuParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class MainMenuFragment extends Fragment {

    private final static Logger logger = Logger.getLogger("MainMenuFragment");
    ArrayList<ArrayList<Double>> allRankings;
    APIInterface api = new APIInterface();
    TableLayout ratingsTable;
    private SimpleEntry<String, Double> bestBreakfast = new SimpleEntry<>("", Double.MIN_VALUE);
    private SimpleEntry<String, Double> bestLunch = new SimpleEntry<>("", Double.MIN_VALUE);
    private SimpleEntry<String, Double> bestDinner = new SimpleEntry<>("", Double.MIN_VALUE);


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_fragment, container, false);
        allRankings = new ArrayList<>();
        ratingsTable = (TableLayout) rootView.findViewById(R.id.rankingsTable);

        getCurrentDiningCommonRankings();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return rootView;
    }

    public void inflateRatingsTable() {
        int count = 1;
        ArrayList<SimpleEntry<String,Double>> bestMealList = new ArrayList<SimpleEntry<String, Double>>();
        bestMealList.add(bestBreakfast);
        bestMealList.add(bestLunch);
        bestMealList.add(bestDinner);

        for (int i = 0; i <= 2; i++) {
            TableRow ratingRow = new TableRow(getActivity().getApplicationContext());
            ratingRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView ratingTextView = new TextView(getActivity().getApplicationContext());
            ratingTextView.setTextColor(Color.rgb(255, 108, 52));
            ratingTextView.setTextSize(16);
            if (i == 0 && !bestBreakfast.getKey().equals("")) {
                ratingTextView.setText(bestMealList.get(i).getKey() + " (" + String.format("%.2f", (bestMealList.get(i).getValue())) + " avg. likes per food item)");
            }
            else if (i == 1 && !bestLunch.getKey().equals("")) {
                ratingTextView.setText(bestMealList.get(i).getKey() + " (" + String.format("%.2f", (bestMealList.get(i).getValue())) + " avg. likes per food item)");
            }
            else if (i == 2 && !bestDinner.getKey().equals("")) {
                ratingTextView.setText(bestMealList.get(i).getKey() + " (" + String.format("%.2f", (bestMealList.get(i).getValue())) + " avg. likes per food item)");
            }
            else
                ratingTextView.setText("Rating data unavailable");
            ratingRow.addView(ratingTextView);
            ratingsTable.addView(ratingRow, count);
            count += 2;
    }
    }


    public void getCurrentDiningCommonRankings() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... v) {
                MenuParser menuParser = new MenuParser();
                for (int i = 0; i <= 3; i++) {
                    String menuString = "";
                    try {
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat(APIInterface.REQUEST_DATE_FORMAT);
                        menuString = api.getMenuJson(DiningCommon.DATA_USE_DINING_COMMONS[i], dateFormat.format(date));
                    } catch (Exception e) {
                        logger.info("Caught API exception");
                        logger.info(e.toString());
                    }
                    ArrayList<Menu> todaysMenus = menuParser.getDailyMenuList(menuString); //one diningCommon at a time
                    setHighestRankings(todaysMenus, DiningCommon.READABLE_DINING_COMMONS[i]);
                }
                return null;
            }
            /**
             * onPostExecute is an overridden method of the AsyncTask class, inflates the ratings table
             */
            @Override
            protected void onPostExecute(Void v) {
                logger.info("entered post execute");
                inflateRatingsTable();
            }
        }.execute();

    }

    private void setHighestRankings(ArrayList<Menu> menus, String diningCommon) {
        double totalRating = 0, itemCount = 0;
        if (menus == null)
            return;
        for (Menu menu : menus) {
            for (MenuItem item : menu.menuItems) {
                itemCount++;
                totalRating = totalRating + getItemRating(item);
            }
            double scaledRating = (totalRating / itemCount);
            if (menu.event.meal.name.toLowerCase().contains("breakfast") && scaledRating > bestBreakfast.getValue())
                bestBreakfast = new SimpleEntry<String, Double>(diningCommon, scaledRating);
            if (menu.event.meal.name.contains("unch") && scaledRating > bestLunch.getValue()) //if brunch or lunch, rating is higher
                bestLunch = new SimpleEntry<String, Double>(diningCommon, scaledRating);
            else if(menu.event.meal.name.toLowerCase().contains("dinner") && scaledRating > bestDinner.getValue())
                bestDinner = new SimpleEntry<String, Double>(diningCommon, scaledRating);
            itemCount = 0;
            totalRating = 0;
        }
    }

    private int getItemRating(MenuItem item) {
        int totalPositiveRatings = item.totalPositiveRatings;
        int totalRating = item.totalRatings;
        int negativeRatings = totalRating - totalPositiveRatings;
        return totalPositiveRatings - negativeRatings;
    }

}
