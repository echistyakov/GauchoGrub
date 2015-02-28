package com.g10.gauchogrub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import com.g10.gauchogrub.io.WebUtils;
import com.g10.gauchogrub.io.MenuParser;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;

import java.util.logging.Level;
import java.util.logging.Logger;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.g10.gauchogrub.menu.DailyMenuList;


public class MenuFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, Runnable {


    private TableLayout menuTable;

    public final static Logger logger = Logger.getLogger("MenuFragment");

    private static String diningCommon;
    private static String date;
    private ArrayList<String> dates;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);

        this.menuTable = (TableLayout) rootView.findViewById(R.id.menu_table);
        this.dates = new ArrayList<String>();

        TabHost tabs = (TabHost)rootView.findViewById(R.id.tabHost);
        this.setUpTabs(tabs, createTabContent(), 4);

        this.fillSpinnerWithDates();

        // Initialize Spinner
        Spinner spinner = (Spinner)rootView.findViewById(R.id.schedule_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.dining_cams_spinner_item, this.dates);
        adapter.setDropDownViewResource(R.layout.dining_cams_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        return rootView;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        TextView dateView = (TextView) view;
        date = dateView.getText().toString();
        run();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
                    String menuString = w.createMenuString(diningCommon,date);
                    MenuParser mp = new MenuParser();
                    return mp.getDailyMenuList(menuString);
                }catch(Exception e)
                    {logger.log(Level.INFO, e.getMessage());}

                return new DailyMenuList("","",0);
        }
            @Override
            protected void onPostExecute(DailyMenuList result) {
                inflateMenu(result);
            }
        }.execute();
    }

    public void setDisplayContent(int tag) {
        String[] commons = new String[] {"Carillo","De_La_Guerra","Ortega","Portola"};
        diningCommon = commons[tag];
        run();
    }

    public TabHost.TabContentFactory createTabContent() {
        return new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                setDisplayContent(Integer.parseInt(tag));
                return (menuTable);
            }
        };
    }


    public void fillSpinnerWithDates(){
        DateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
        Date date = new Date();

        //TO BE DELETED EVENTUALLY
        dates.add("2/17/2015");

        for(int i = 0; i < 7 ; i++){
            Date tomorrow = new Date(date.getTime() + i*(1000 * 60 * 60 * 24));
            dates.add(dateFormat.format(tomorrow));
        }
    }

}
