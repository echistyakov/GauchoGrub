package com.g10.gauchogrub;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import com.g10.gauchogrub.io.WebUtils;
import com.g10.gauchogrub.io.MenuParser;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.logging.Logger;
import android.widget.TableRow;
import android.widget.TextView;
import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.g10.gauchogrub.menu.DailyMenuList;


public class MenuFragment extends Fragment implements AdapterView.OnItemSelectedListener, Runnable {


    private TableLayout menuTable;

    public final static Logger logger = Logger.getLogger("MenuFragment");

    private static String diningCommon;
    private static String date;
    private ArrayList<String> dates;
    private TableRow currentButtons = null;
    private HashSet<String> favoritesList;
    private File favoritesFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
        setHasOptionsMenu(true);

        this.favoritesList = new HashSet<>();
        this.menuTable = (TableLayout) rootView.findViewById(R.id.menu_table);
        this.dates = new ArrayList<>();
        this.favoritesFile = new File(getActivity().getApplicationContext().getFilesDir(),"favorites.ser");

        TabHost tabs = (TabHost)rootView.findViewById(R.id.tabHost);
        this.setUpTabs(tabs);

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

        final ArrayList<Menu> menuMeals = displayMenu.getMenus();

        if(displayMenu.getMenus() == null) return;
        for (Menu headerEntry : menuMeals) {
            String MenuName = headerEntry.getMenuName();

            TableRow headerRow = new TableRow(getActivity().getApplicationContext());
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
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

                final TableRow entryRow = new TableRow(getActivity().getApplicationContext());
                entryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                final View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_entry, null);

                final TextView menuTypeView = (TextView) entryView.findViewById(R.id.meal_type);
                menuTypeView.setText(itemTitle);

                entryRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentButtons != entryRow || currentButtons == null) {
                            //changeButtonVisibility(entryView,View.VISIBLE);
                            entryRow.setBackgroundColor(Color.LTGRAY);
                            if(currentButtons != null){
                                currentButtons.setBackgroundColor(0);
                                View buttonsToMakeInvisible = currentButtons.getVirtualChildAt(0);
                                //changeButtonVisibility(buttonsToMakeInvisible,View.INVISIBLE);
                            }
                            currentButtons = entryRow;
                        }

                        else if(currentButtons == entryRow) {
                            entryRow.setBackgroundColor(0);
                            //changeButtonVisibility(entryView,View.INVISIBLE);
                            currentButtons = null;
                        }
                    }
                });

                setButtonListeners(entryView, (String) menuTypeView.getText());

                TextView menuTimeView = (TextView) entryView.findViewById(R.id.meal_time);
                menuTimeView.setText(itemType);

                changeButtonVisibility(entryView,View.GONE);

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
                }catch(Exception e){};

                return new DailyMenuList("","",0);
            }
            @Override
            protected void onPostExecute(DailyMenuList result) {
                inflateMenu(result);
            }
        }.execute();
    }

    public void setMenuTable(String tag) {
        if (tag.equals("0")) {
            diningCommon = "Carrillo";
        } else if (tag.equals("1")) {
            diningCommon = "De_La_Guerra";
        } else if (tag.equals("2")) {
            diningCommon = "Ortega";
        } else if (tag.equals("3")) {
            diningCommon = "Portolla";
        }
        run();
    }

    public void setUpTabs(TabHost tabs){
        String[] commons = new String[] {"Carillo","DLG","Ortega","Portola"};

        //Set the initial tab content
        TabHost.TabContentFactory contentCreate = new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                setMenuTable(tag);
                return (menuTable);
            }
        };
        tabs.setup();
        //Create tabs and set text & content
        for(int i = 0; i < 4 ; i ++) {
            TabHost.TabSpec tab = tabs.newTabSpec(i + "");
            tab.setContent(contentCreate);
            tab.setIndicator(commons[i]);
            tabs.addTab(tab);
        }
        //Set tab listeners to change content when triggered
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                setMenuTable(tabId);
            }
        });
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

    public void changeButtonVisibility(View entryView,int visible) {
        ImageButton favorite = (ImageButton) entryView.findViewById(R.id.imageButton);
        ImageButton like = (ImageButton) entryView.findViewById(R.id.imageButton2);
        ImageButton dislike = (ImageButton) entryView.findViewById(R.id.imageButton3);
        favorite.setVisibility(visible);
        like.setVisibility(visible);
        dislike.setVisibility(visible);
    }

    public void setButtonListeners(View entryView, final String menuItemName){
        final ImageButton favorite = (ImageButton) entryView.findViewById(R.id.imageButton);
        final ImageButton like = (ImageButton) entryView.findViewById(R.id.imageButton2);
        final ImageButton dislike = (ImageButton) entryView.findViewById(R.id.imageButton3);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                final Drawable current = getResources().getDrawable(R.drawable.ic_action_favorite);
                if(favorite.getBackground().getConstantState().equals(current.getConstantState())) {
                    favorite.setBackgroundResource(R.drawable.ic_action_favorite);
                    favoritesList.add(menuItemName);
                    try {
                        writeFavorites(favoritesList);
                    } catch(IOException e){ e.printStackTrace(); }
                }
                else {
                    favorite.setBackgroundResource(R.drawable.ic_action_favorite);
                    favoritesList.remove(menuItemName);
                }

            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                Drawable current = getResources().getDrawable(R.drawable.ic_action_favorite);
                if(like.getBackground().getConstantState().equals(current.getConstantState())) {
                    like.setBackgroundResource(R.drawable.ic_action_good);
                }
                else { like.setBackgroundResource(R.drawable.ic_action_good); }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w){
                Drawable current = getResources().getDrawable(R.drawable.ic_action_bad);
                if(dislike.getBackground().getConstantState().equals(current.getConstantState())) {
                    dislike.setBackgroundResource(R.drawable.ic_action_bad);
                }
                else { dislike.setBackgroundResource(R.drawable.ic_action_bad); }
            }
        });
    }

    public boolean writeFavorites(HashSet<String> favoritesList) throws IOException {
        OutputStreamWriter outStream;
        try {
            outStream = new OutputStreamWriter(getActivity().getApplicationContext().openFileOutput("favorites.ser", Context.MODE_PRIVATE)); //new FileOutputStream(favoritesFile);
            for (String thisfavorite : favoritesList) {
                outStream.write(thisfavorite + "\n");
                logger.info("Writing String " + thisfavorite);
            }
            outStream.close();
        } catch (IOException e) { return false; }
        return true;
    }

}
