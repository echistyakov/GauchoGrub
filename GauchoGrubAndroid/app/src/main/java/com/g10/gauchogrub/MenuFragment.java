package com.g10.gauchogrub;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import com.g10.gauchogrub.io.WebUtils;
import com.g10.gauchogrub.io.MenuParser;
import com.g10.gauchogrub.menu.Meal;
import com.g10.gauchogrub.menu.MenuItem;
import java.net.URL;
import java.util.logging.Logger;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.AsyncTask;
import java.io.File;


import java.util.ArrayList;

import com.g10.gauchogrub.menu.DayMenu;


public class MenuFragment extends Fragment implements AdapterView.OnItemSelectedListener, Runnable {


    private TableLayout menuTable;

    public final static Logger logger = Logger.getLogger("MenuFragment");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);

        this.menuTable = (TableLayout) rootView.findViewById(R.id.menu_table);

        // Initialize Spinner (DC)
        Spinner spinner = (Spinner)rootView.findViewById(R.id.dining_common_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dining_commons_array, R.layout.dining_cams_spinner_item);
        adapter.setDropDownViewResource(R.layout.dining_cams_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        // Initialize Spinner (date)
        Spinner dateSpinner = (Spinner)rootView.findViewById(R.id.date_spinner);
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.date_nav_array, R.layout.dining_cams_spinner_item);
        dateAdapter.setDropDownViewResource(R.layout.dining_cams_spinner_item);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(this);
        dateSpinner.setSelection(0);

        return rootView;
    }

    private void inflateMenu(DayMenu displayMenu) {

        logger.info("Spinner Position 1: ");

        //menuTable.removeAllViews();

        ArrayList<Meal> menuMeals = displayMenu.getMeals();

        String test = displayMenu.getDate();
        logger.info("Test Date: " + test);
        if(displayMenu.getMeals() == null) return;
        for (Meal headerEntry : menuMeals) {
            String mealName = headerEntry.getMealName();

            TableRow headerRow = new TableRow(getActivity().getApplicationContext());
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.schedule_header, null);
            TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
            headerTextView.setText(mealName);
            headerRow.addView(headerView);
            menuTable.addView(headerRow);

            for(MenuItem itemEntry : headerEntry.getMenuItems()) {
                String itemTitle = itemEntry.getTitle();
                String itemCategory = itemEntry.getMenuCategory();
                String itemType = itemEntry.getMenuItemType();

                TableRow entryRow = new TableRow(getActivity().getApplicationContext());
                entryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.schedule_entry, null);

                TextView mealTypeView = (TextView) entryView.findViewById(R.id.meal_type);
                mealTypeView.setText(itemTitle);

                TextView mealTimeView = (TextView) entryView.findViewById(R.id.meal_time);
                mealTimeView.setText(itemType);

                entryRow.addView(entryView);
                menuTable.addView(entryRow);

            }
        }
    }

    @Override
    public void run() {
        new AsyncTask<Void, Void, DayMenu>() {
            @Override
            protected DayMenu doInBackground(Void... v) {
                try {
                    WebUtils w = new WebUtils();
                    String menuString = w.createMenuString();
                    MenuParser mp = new MenuParser();
                    return mp.getDayMenu(menuString);
                }catch(Exception e){};

                return new DayMenu("999","",0);
        }
            @Override
            protected void onPostExecute(DayMenu result) {
                inflateMenu(result);
            }
        }.execute();
    }





    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        logger.info("item selected");
        run();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
