package com.g10.gauchogrub;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import com.g10.gauchogrub.utils.WebUtils;
import com.g10.gauchogrub.utils.MenuParser;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Logger;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class MenuFragment extends BaseTabbedFragment implements AdapterView.OnItemSelectedListener, Runnable {

    public final static Logger logger = Logger.getLogger("MenuFragment");
    private TableLayout menuTable;
    private static String diningCommon;
    private static String date;
    private ArrayList<String> dates;

    //Current Selected Menu Item
    private TableRow currentSelectedItem = null;
    //Current visible buttons bar
    private View currentBar;
    //Reference to the container that will hold different sets of buttons
    private RelativeLayout buttonLayout;

    private HashSet<String> favoritesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
        setHasOptionsMenu(true);

        this.favoritesList = new HashSet<>();
        this.menuTable = (TableLayout) rootView.findViewById(R.id.menu_table);
        this.buttonLayout = (RelativeLayout) rootView.findViewById(R.id.bottombar);
        this.dates = new ArrayList<>();

        TabHost tabs = (TabHost)rootView.findViewById(R.id.tabHost);
        this.setUpTabs(tabs, createTabContent(), 4);

        this.fillSpinnerWithDates();

        try {
            favoritesList = fillFavoritesList(diningCommon);
        } catch(IOException e){
            e.printStackTrace();
        } catch(NullPointerException e){
            e.printStackTrace();
        }

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
        buttonLayout.removeAllViews();
        run();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void inflateMenu(ArrayList<Menu> menus) {

        menuTable.removeAllViews();

        if(menus == null) return;
        for (Menu menu : menus) {
            TableRow headerRow = new TableRow(getActivity().getApplicationContext());
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_header, null);
            TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
            headerTextView.setText(menu.event.meal.name);
            headerRow.addView(headerView);
            menuTable.addView(headerRow);

            String currentCategory = "Default";

            for(MenuItem item : menu.menuItems) {
                if(!item.menuCategory.name.equals(currentCategory)) {
                    TableRow categoryRow = new TableRow(getActivity().getApplicationContext());
                    categoryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    View categoryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_category, null);

                    TextView categoryTypeView = (TextView) categoryView.findViewById(R.id.meal_cat);
                    categoryTypeView.setText(item.menuCategory.name);
                    categoryRow.addView(categoryView);
                    menuTable.addView(categoryRow);

                    currentCategory = item.menuCategory.name;
                }

                final TableRow entryRow = new TableRow(getActivity().getApplicationContext());
                entryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                final View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_entry, null);
                final View buttonBar = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_entry_buttons, null);

                TextView menuTypeView = (TextView) entryView.findViewById(R.id.meal_type);
                menuTypeView.setText(item.title);

                //final int Id = item.menuItemID;

                Log.d("MenuFragment","hello" + item.title);
                final TextView ratingTextView = (TextView) buttonBar.findViewById(R.id.ratingView);
                final int id = item.menuItemID;


                menuTypeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //A user click a different menu item than previously selected
                        if (currentSelectedItem != entryRow || currentSelectedItem == null) {
                            // get rating to display on buttonBar

                            try {
                                getRating(ratingTextView,id);
                            } catch (Exception e) {
                                ratingTextView.setText("Rating not found");
                                e.printStackTrace();
                            }


                            //Switching from one selected item to another
                            if (currentSelectedItem != null) {
                                currentSelectedItem.setBackgroundColor(0);
                                buttonLayout.removeView(currentBar);
                            }
                            //This happens every time a new item is selected
                            entryRow.setBackgroundColor(Color.LTGRAY);
                            currentSelectedItem = entryRow;
                            currentBar = buttonBar;
                            buttonLayout.addView(buttonBar);
                        }
                        //A user clicks the same menu item that was already selected
                        else if (currentSelectedItem == entryRow) {
                            entryRow.setBackgroundColor(0);
                            buttonLayout.removeView(currentBar);
                            currentSelectedItem = null;
                            currentBar = null;
                        }
                    }
                });

                //When loading Menu, if items already exist in favorites list the button needs to be activated
                ImageButton favoriteButton = (ImageButton) buttonBar.findViewById(R.id.favoriteButton);
                if(favoritesList.contains(item.title)) { favoriteButton.setBackgroundResource(R.drawable.ic_action_favorite_on); }

                //TODO set up like and dislike button listeners
                setButtonListeners(buttonBar, (String) menuTypeView.getText());

                TextView menuTimeView = (TextView) entryView.findViewById(R.id.meal_time);
                menuTimeView.setText(item.menuItemType.getShortVersion());

                entryRow.addView(entryView);
                menuTable.addView(entryRow);

            }
        }
    }

    //This runnable begins a series of calls to get and display menus
    @Override
    public void run() {
        new AsyncTask<Void, Void, ArrayList<Menu>>() {
            @Override
            protected ArrayList<Menu> doInBackground(Void... v) {
                try {
                    WebUtils w = new WebUtils();
                    String menuString = w.createMenuString(diningCommon, date);
                    MenuParser mp = new MenuParser();
                    return mp.getDailyMenuList(menuString);
                } catch(Exception e) {
                    logger.log(Level.INFO, e.getMessage());
                }
                return null;
        }
            @Override
            protected void onPostExecute(ArrayList<Menu> result) {
                inflateMenu(result);
            }
        }.execute();
    }


    public void setDisplayContent(int tag) {
        //When switching Dining Commons, Remove button bar
        buttonLayout.removeView(currentBar);
        currentSelectedItem = null;
        currentBar = null;

        //Set Dining Common String to new Tab
        String[] commons = new String[] {"Carillo","De_La_Guerra","Ortega","Portola"};
        diningCommon = commons[tag];

        //Update favorites list corresponding to the tabbed Dining Common
        try {
            favoritesList = fillFavoritesList(diningCommon);
        } catch(IOException e){
            e.printStackTrace();
        } catch(NullPointerException e){
            e.printStackTrace();
        }

        run();
    }

    //This function is called when a user clicks a different tab
    public TabHost.TabContentFactory createTabContent() {
        return new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                setDisplayContent(Integer.parseInt(tag));
                return (menuTable);
            }
        };
    }

    //function to fill drop down menu with the next seven dates
    public void fillSpinnerWithDates(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        //TO BE DELETED EVENTUALLY
        dates.add("2/17/2015");

        for(int i = 0; i < 7 ; i++){
            Date tomorrow = new Date(date.getTime() + i*(1000 * 60 * 60 * 24));
            dates.add(dateFormat.format(tomorrow));
        }
    }

    public void setButtonListeners(View entryView, final String menuItemName){
        final ImageButton favorite = (ImageButton) entryView.findViewById(R.id.favoriteButton);
        final ImageButton like = (ImageButton) entryView.findViewById(R.id.thumbsUpButton);
        final ImageButton dislike = (ImageButton) entryView.findViewById(R.id.thumbsDownButton);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                final Drawable current = getResources().getDrawable(R.drawable.ic_action_favorite);
                if (favorite.getBackground().getConstantState().equals(current.getConstantState())) {
                    favorite.setBackgroundResource(R.drawable.ic_action_favorite_on);
                    favoritesList.add(menuItemName);
                } else {
                    favorite.setBackgroundResource(R.drawable.ic_action_favorite);
                    favoritesList.remove(menuItemName);
                }
                try {
                    writeFavorites(favoritesList, diningCommon);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                Drawable current = getResources().getDrawable(R.drawable.ic_action_good);
                if(like.getBackground().getConstantState().equals(current.getConstantState())) {
                    like.setBackgroundResource(R.drawable.ic_action_good_on);
                }
                else { like.setBackgroundResource(R.drawable.ic_action_good); }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                Drawable current = getResources().getDrawable(R.drawable.ic_action_bad);
                if(dislike.getBackground().getConstantState().equals(current.getConstantState())) {
                    dislike.setBackgroundResource(R.drawable.ic_action_bad_on);
                }
                else { dislike.setBackgroundResource(R.drawable.ic_action_bad); }
            }
        });
    }

        public int getOverallRating(String ratingString){
            logger.info("hi" + ratingString);
            int start1 = ratingString.indexOf(':')+1;
            int end1 = ratingString.indexOf(',');
            int start2 = ratingString.indexOf(':',end1)+1;
            int end2 = ratingString.indexOf('}',start2);
            int total = Integer.parseInt(ratingString.substring(start1, end1));
            int positive = Integer.parseInt(ratingString.substring(start2,end2));
            int negative = total - positive;
            return positive - negative;
        }


    public void getRating(final TextView ratingView, final int menuItemID) throws Exception{
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... v) {
                try {
                    WebUtils util = new WebUtils();
                    return getOverallRating(util.getRating(menuItemID)) + "";
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    return "rating not found";
                }
            }
            @Override
            protected void onPostExecute(String rating){
                int x = Integer.parseInt(rating);
                if (x >= 0) {
                    ratingView.setTextColor(Color.rgb(8,124,39));
                    rating = "+" + rating;
                }
                else { ratingView.setTextColor(Color.RED); }
                ratingView.setText(rating);
            }
        }.execute();
    }



}
