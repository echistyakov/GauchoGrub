package com.g10.gauchogrub;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FavoritesFragment extends BaseTabbedFragment {

    HashSet<String> favoritesList;
    public final static Logger logger = Logger.getLogger("FavoritesFragment");
    private TableLayout favoritesTable;
    private String diningCommon = "Carillo";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesList = new HashSet<>();
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        favoritesTable = (TableLayout)rootView.findViewById(R.id.favorites_table);
        TabHost tabs = (TabHost) rootView.findViewById(R.id.tabHost2);
        this.setUpTabs(tabs, createTabContent(), 4);
        try {
            favoritesList = fillFavoritesList(diningCommon);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void inflateFavorites() {
        for(final String favorite : favoritesList) {
            TableRow favoriteRow = new TableRow(getActivity().getApplicationContext());
            favoriteRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.favorites_entry, null);

            TextView favoriteView = (TextView)entryView.findViewById(R.id.meal_cat);
            favoriteView.setText(favorite);
            setButtonListeners(entryView,favorite);

            favoriteRow.addView(entryView);
            favoritesTable.addView(favoriteRow);
        }
    }

    public TabHost.TabContentFactory createTabContent() {
        return new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                setDisplayContent(Integer.parseInt(tag));
                return favoritesTable;
            }
        };
    }

    public void setDisplayContent(int tag) {
        String[] commons = new String[] {"Carillo","De_La_Guerra","Ortega","Portola"};
        diningCommon = commons[tag];
        run();
    }

    public void run() {
        new AsyncTask<Void, Void, HashSet<String>>() {
            @Override
            protected HashSet<String> doInBackground(Void... v) {
                try {
                    fillFavoritesList(diningCommon);
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
                final Drawable current = getResources().getDrawable(R.drawable.ic_action_favorite_on);
                if (favoriteButton.getBackground().getConstantState().equals(current.getConstantState())) {
                    favoritesList.remove(favorite);
                    favoriteButton.setBackgroundResource(R.drawable.ic_action_favorite);
                } else {
                    favoritesList.add(favorite);
                    favoriteButton.setBackgroundResource(R.drawable.ic_action_favorite_on);
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