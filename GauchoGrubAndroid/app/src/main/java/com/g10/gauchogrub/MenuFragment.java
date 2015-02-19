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
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import java.util.logging.Logger;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.AsyncTask;
import java.util.ArrayList;

import com.g10.gauchogrub.menu.DailyMenuList;


public class MenuFragment extends Fragment implements AdapterView.OnItemSelectedListener, Runnable {


    private TableLayout menuTable;

    public final static Logger logger = Logger.getLogger("MenuFragment");

    private static String diningCommon;
    private String date;



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

    private void inflateMenu(DailyMenuList displayMenu) {

        menuTable.removeAllViews();

        ArrayList<Menu> menuMeals = displayMenu.getMenus();

        if(displayMenu.getMenus() == null) return;
        for (Menu headerEntry : menuMeals) {
            String MenuName = headerEntry.getMenuName();

            TableRow headerRow = new TableRow(getActivity().getApplicationContext());
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_header, null);
            TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
            headerTextView.setText(MenuName);
            headerRow.addView(headerView);
            menuTable.addView(headerRow);

            String currentCategory = "Default";

            for(MenuItem itemEntry : headerEntry.getMenuItems()) {
                String itemTitle = itemEntry.getTitle();
                String itemCategory = itemEntry.getMenuCategory();
                String itemType = itemEntry.getMenuItemType();

                if(itemType.equals("Regular")) itemType = "";
                if(itemType.equals("Vegetarian")) itemType = "(v)";
                if(itemType.equals("Vegan")) itemType = "(vgn)";

                if(!itemCategory.equals(currentCategory)) {
                    TableRow categoryRow = new TableRow(getActivity().getApplicationContext());
                    categoryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    View categoryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_category, null);

                    TextView categoryTypeView = (TextView) categoryView.findViewById(R.id.meal_cat);
                    categoryTypeView.setText(itemCategory);
                    categoryRow.addView(categoryView);
                    menuTable.addView(categoryRow);

                    currentCategory = itemCategory;
                }

                TableRow entryRow = new TableRow(getActivity().getApplicationContext());
                entryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_entry, null);

                TextView menuTypeView = (TextView) entryView.findViewById(R.id.meal_type);
                menuTypeView.setText(itemTitle);

                TextView menuTimeView = (TextView) entryView.findViewById(R.id.meal_time);
                menuTimeView.setText(itemType);

                entryRow.addView(entryView);
                menuTable.addView(entryRow);

            }
        }
    }

    @Override
    public void run() {
        new AsyncTask<Void, Void, DailyMenuList>() {
            @Override
            protected DailyMenuList doInBackground(Void... v) {
                try {
                    WebUtils w = new WebUtils();
                    String menuString = w.createMenuString(diningCommon,"2/17/2015");
                    MenuParser mp = new MenuParser();
                    return mp.getDailyMenuList(menuString);
                }catch(Exception e){};

                return new DailyMenuList("","",0);
        }
            @Override
            protected void onPostExecute(DailyMenuList result) {
                inflateMenu(result);
            }
        }.execute();
    }





    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        logger.info("item selected");
        if(pos == 0) diningCommon = "Carrillo";
        if(pos == 1) diningCommon = "De_La_Guerra";
        if(pos == 2) diningCommon = "Ortega";
        if(pos == 3) diningCommon = "Portolla";
        run();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}
