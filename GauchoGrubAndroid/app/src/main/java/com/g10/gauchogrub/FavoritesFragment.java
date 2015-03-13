package com.g10.gauchogrub;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g10.gauchogrub.menu.DiningCommon;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FavoritesFragment extends BaseTabbedFragment {

    private final static Logger logger = Logger.getLogger("FavoritesFragment");

    private HashSet<String> favoritesList;
    private TableLayout favoritesTable;
    private RelativeLayout buttonLayout;
    private TableRow currentSelectedItem = null;
    private View currentButtonBar;
    private String diningCommon = DiningCommon.DATA_USE_CARILLO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesList = new HashSet<>();
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        favoritesTable = (TableLayout)rootView.findViewById(R.id.favorites_table);
        buttonLayout = (RelativeLayout)rootView.findViewById(R.id.bottombar);
        TabHost tabs = (TabHost) rootView.findViewById(R.id.tabHost2);
        this.setUpTabs(tabs, createTabContent(), 4);
        run();

        return rootView;
    }

    private void inflateFavorites() {
        favoritesTable.removeAllViews();

        for(String favorite : favoritesList) {
            final TableRow favoriteRow = new TableRow(getActivity().getApplicationContext());
            favoriteRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.favorites_entry, null);

            final View buttonBar = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_entry_buttons, null);
            TextView favoriteView = (TextView)entryView.findViewById(R.id.meal_cat);
            favoriteView.setText(favorite);

            favoriteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //A user click a different menu item than previously selected
                    if(currentSelectedItem != favoriteRow || currentSelectedItem == null) {
                        //Switching from one selected item to another
                        if(currentSelectedItem != null){
                            currentSelectedItem.setBackgroundColor(0);
                            buttonLayout.removeView(currentButtonBar);
                        }
                        //This happens every time a new item is selected
                        favoriteRow.setBackgroundColor(Color.LTGRAY);
                        currentSelectedItem = favoriteRow;
                        currentButtonBar = buttonBar;
                        buttonLayout.addView(buttonBar);
                    }
                    //A user clicks the same menu item that was already selected
                    else if(currentSelectedItem == favoriteRow) {
                        favoriteRow.setBackgroundColor(0);
                        buttonLayout.removeView(currentButtonBar);
                        currentSelectedItem = null;
                        currentButtonBar = null;
                    }
                }
            });

            setButtonListeners(buttonBar,favorite);

            ImageButton favButton = (ImageButton) buttonBar.findViewById(R.id.favoriteButton);
            favButton.setBackgroundResource(R.drawable.favorite_on_xxhdpi);


            favoriteRow.addView(entryView);
            favoritesTable.addView(favoriteRow);
        }
    }

    public TabHost.TabContentFactory createTabContent() {
        return new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                favoritesTable.removeAllViews();
                return favoritesTable;
            }
        };
    }

    @Override
    public void setDisplayContent(int index) {
        buttonLayout.removeView(currentButtonBar);
        currentSelectedItem = null;
        currentButtonBar = null;
        diningCommon = DiningCommon.DATA_USE_DINING_COMMONS[index];
        run();
    }

    public void run() {
        new AsyncTask<Void, Void, HashSet<String>>() {
            @Override
            protected HashSet<String> doInBackground(Void... v) {
                try {
                    favoritesList = fillFavoritesList(diningCommon);
                    for(String test : favoritesList){
                        logger.info("list contains: " + test);
                    }
                } catch(Exception e) {
                    logger.log(Level.INFO, e.getMessage());
                }
                return null;
            }
            @Override
            protected void onPostExecute(HashSet<String> result) {
                inflateFavorites();
            }
        }.execute();
    }

    public void setButtonListeners(View entryView, final String favorite){
        final ImageButton favoriteButton = (ImageButton) entryView.findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                final Drawable current = getResources().getDrawable(R.drawable.favorite_on_xxhdpi);
                if (favoriteButton.getBackground().getConstantState().equals(current.getConstantState())) {
                    favoritesList.remove(favorite);
                    favoriteButton.setBackgroundResource(R.drawable.favorite_off_xxhdpi);
                } else {
                    favoritesList.add(favorite);
                    favoriteButton.setBackgroundResource(R.drawable.favorite_on_xxhdpi);
                }
                try {
                    writeFavorites(favoritesList, diningCommon);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}