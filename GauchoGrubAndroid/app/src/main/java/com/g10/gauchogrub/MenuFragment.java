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
import java.net.URL;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import com.g10.gauchogrub.menu.DayMenu;


public class MenuFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private TableLayout menuTable;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);

        this.menuTable = (TableLayout) rootView.findViewById(R.id.schedule_table);

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

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        /*
        WE HAVE BUILT A FUNCTION TO MAKE THE API REQUEST AND WRITE THE RESPONSE TO A FILE. WE ALSO
        HAVE A FUNCTION THE WILL PARSE THE FILE INTO A DAYMENU DATA STRUCTURE AND RETURN THAT STRUCTURE.
        WE NOW NEED TO GET REFERNCES TO THE TWO SPINNERS OF THE MENU FRAGMENT AND CREATE THE API REQUEST
        ON THE TEXT IN THE SPINNERS. USING THE DATA FROM THE RETURNED DAYMENU STRUCTURE WE WILL USE THE
        DYNAMIC SCHEDULE FRAGMENT CODE AS A REFERENCE TO BUILD DYNAMIC MENU TABLES.

        DayMenu displayMenu = getDayMenu();

        for (Meal headerEntry : schedule) {
            String header = headerEntry.getKey();

            TableRow headerRow = new TableRow(getActivity().getApplicationContext());
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.schedule_header, null);
            TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
            headerTextView.setText(header);
            headerRow.addView(headerView);
            scheduleTable.addView(headerRow);

            for(AbstractMap.SimpleEntry<String, String> mealEntry : headerEntry.getValue()) {
                String mealTitle = mealEntry.getKey();
                String mealTime = mealEntry.getValue();

                TableRow entryRow = new TableRow(getActivity().getApplicationContext());
                entryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.schedule_entry, null);

                TextView mealTypeView = (TextView) entryView.findViewById(R.id.meal_type);
                mealTypeView.setText(mealTitle);

                TextView mealTimeView = (TextView) entryView.findViewById(R.id.meal_time);
                mealTimeView.setText(mealTime);

                entryRow.addView(entryView);
                scheduleTable.addView(entryRow);
                logger.info("child count " + scheduleTable.getChildCount());
            }
        }
    */
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public DayMenu getDayMenu() {
        MenuParser mp = new MenuParser();
        DayMenu displayMenu = mp.getDayMenu();
        return displayMenu;
    }

}
