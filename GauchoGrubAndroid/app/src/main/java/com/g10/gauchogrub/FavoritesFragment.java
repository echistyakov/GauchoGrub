package com.g10.gauchogrub;

import android.graphics.drawable.Drawable;
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
import java.util.logging.Logger;


public class FavoritesFragment extends BaseFavoritesFragment {

    HashSet<String> favoritesList;
    public final static Logger logger = Logger.getLogger("FavoritesFragment");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        favoritesList = new HashSet<>();

        try {
            favoritesList = fillFavoritesList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        TableLayout displayedFavorites = (TableLayout)rootView.findViewById(R.id.favorites_table);

        for(final String favorite : favoritesList) {
            TableRow favoriteRow = new TableRow(getActivity().getApplicationContext());
            favoriteRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.favorites_entry, null);

            TextView favoriteView = (TextView)entryView.findViewById(R.id.meal_cat);
            favoriteView.setText(favorite);
            setButtonListeners(entryView,favorite);

            favoriteRow.addView(entryView);
            displayedFavorites.addView(favoriteRow);
        }

        return rootView;
    }

    public TabHost.TabContentFactory createTabContent() {
        return new TabHost.TabContentFactory() {
            /*
            @Override
            public View createTabContent(String tag) {
                setDisplayContent(Integer.parseInt(tag));
                return ;
            }*/
        };
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
                    writeFavorites(favoritesList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}