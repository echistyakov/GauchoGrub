package com.g10.gauchogrub;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.os.AsyncTask;

import com.g10.gauchogrub.menu.DiningCommon;
import com.g10.gauchogrub.utils.APIInterface;
import com.g10.gauchogrub.utils.CacheUtils;
import com.g10.gauchogrub.utils.FileIOUtils;
import com.g10.gauchogrub.utils.MenuParser;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;

import java.io.File;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MenuFragment extends BaseTabbedFragment implements AdapterView.OnItemSelectedListener, Runnable {

    private final static Logger logger = Logger.getLogger("MenuFragment");
    private TableLayout menuTable;
    private static String diningCommon;
    private static String date;
    private ArrayList<String> dates;

    // Current Selected Menu Item
    private TableRow currentSelectedItem = null;
    // Current visible buttons bar
    private View currentBar;
    // Reference to the container that will hold different sets of buttons
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

        TabHost tabs = (TabHost) rootView.findViewById(R.id.tabHost);
        this.setUpTabs(tabs, createTabContent(), 4);

        this.fillSpinnerWithDates();
        FileIOUtils fio = new FileIOUtils();
        favoritesList = fio.fillFavoritesList(getActivity().getBaseContext(), diningCommon);

        // Initialize Spinner
        Spinner spinner = (Spinner) rootView.findViewById(R.id.schedule_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dining_cams_spinner_item, this.dates);
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

        if (menus == null) {
            return;
        }
        for (Menu menu : menus) {
            TableRow headerRow = new TableRow(getActivity().getApplicationContext());
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_header, null);
            TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
            headerTextView.setText(menu.event.meal.name);
            headerRow.addView(headerView);
            menuTable.addView(headerRow);

            String currentCategory = "Default";

            for (MenuItem item : menu.menuItems) {
                if (!item.menuCategory.name.equals(currentCategory)) {
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


                final TextView ratingTextView = (TextView) buttonBar.findViewById(R.id.ratingView);
                int totalPositiveRatings = item.totalPositiveRatings;
                int totalRating = item.totalRatings;
                int negativeRatings = totalRating - totalPositiveRatings;
                final int netRatings = totalPositiveRatings - negativeRatings;


                menuTypeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //A user click a different menu item than previously selected
                        if (currentSelectedItem != entryRow || currentSelectedItem == null) {

                            // display rating on buttonBar
                            if (netRatings >= 0) {
                                ratingTextView.setTextColor(Color.rgb(8, 124, 39));
                                ratingTextView.setText("+" + netRatings);

                            } else {
                                ratingTextView.setTextColor(Color.RED);
                                ratingTextView.setText(netRatings + "");
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
                if (favoritesList.contains(item.title)) {
                    favoriteButton.setBackgroundResource(R.drawable.favorite_on_xxhdpi);
                }

                //TODO set up like and dislike button listeners
                setButtonListeners(buttonBar, (String) menuTypeView.getText(), menu, item);

                TextView menuTimeView = (TextView) entryView.findViewById(R.id.meal_time);
                menuTimeView.setText(item.menuItemType.getShortVersion());

                entryRow.addView(entryView);
                menuTable.addView(entryRow);
            }
        }
        for (int i = 0; i <= 1; i++) {
            TableRow extraRow = new TableRow(getActivity().getApplicationContext());
            extraRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            final View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.meal_entry, null);
            extraRow.addView(entryView);
            menuTable.addView(extraRow);
        }

    }

    // This runnable begins a series of calls to get and display menus
    @Override
    public void run() {
        new AsyncTask<Void, Void, ArrayList<Menu>>() {
            @Override
            protected ArrayList<Menu> doInBackground(Void... v) {
                try {
                    APIInterface api = new APIInterface();
                    String menuString, title = diningCommon + date.replace("/", "");
                    CacheUtils c = new CacheUtils();
                    if (new File(getActivity().getBaseContext().getCacheDir(), title).exists())
                        menuString = c.readCachedFile(getActivity(), title);
                    else {
                        menuString = api.getMenuJson(diningCommon, date);
                        c.cacheFile(getActivity(), title, menuString);
                    }
                    MenuParser mp = new MenuParser();
                    return mp.getDailyMenuList(menuString);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<Menu> result) {
                inflateMenu(result);
            }
        }.execute();
    }

    @Override
    public void setDisplayContent(int tag) {
        //When switching Dining Commons, Remove button bar
        buttonLayout.removeView(currentBar);
        currentSelectedItem = null;
        currentBar = null;

        //Set Dining Common String to new Tab
        diningCommon = DiningCommon.DATA_USE_DINING_COMMONS[tag];

        //Update favorites list corresponding to the tabbed Dining Common
        FileIOUtils fio = new FileIOUtils();
        favoritesList = fio.fillFavoritesList(getActivity().getBaseContext(), diningCommon);
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
    public void fillSpinnerWithDates() {
        DateFormat dateFormat = new SimpleDateFormat(APIInterface.REQUEST_DATE_FORMAT);
        Date date = new Date();

        for (int i = 0; i < 7; i++) {
            Date tomorrow = new Date(date.getTime() + i * (1000 * 60 * 60 * 24));
            dates.add(dateFormat.format(tomorrow));
        }
    }

    public void setButtonListeners(View entryView, final String menuItemName, Menu menu, MenuItem item) {
        final ImageButton favorite = (ImageButton) entryView.findViewById(R.id.favoriteButton);
        final ImageButton like = (ImageButton) entryView.findViewById(R.id.thumbsUpButton);
        final ImageButton dislike = (ImageButton) entryView.findViewById(R.id.thumbsDownButton);

        final int menuId = menu.menuID;
        final int menuItemId = item.menuItemID;

        final String userId = BaseActivity.androidId;
        final TextView ratingView = (TextView) entryView.findViewById(R.id.ratingView);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                final Drawable current = getResources().getDrawable(R.drawable.favorite_off_xxhdpi);
                if (favorite.getBackground().getConstantState().equals(current.getConstantState())) {
                    favorite.setBackgroundResource(R.drawable.favorite_on_xxhdpi);
                    favoritesList.add(menuItemName);
                } else {
                    favorite.setBackgroundResource(R.drawable.favorite_off_xxhdpi);
                    favoritesList.remove(menuItemName);
                }
                FileIOUtils fio = new FileIOUtils();
                fio.writeFavorites(getActivity().getBaseContext(), favoritesList, diningCommon);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                Drawable current = getResources().getDrawable(R.drawable.upvote_off_xxhdpi);
                String temp = (String) ratingView.getText(), posOrNeg = "";
                int start1 = 0;
                if (ratingView.getCurrentTextColor() == Color.rgb(8, 124, 39)) {
                    posOrNeg = "+";
                    start1 = temp.indexOf('+') + 1;
                }
                int rating = Integer.parseInt(temp.substring(start1));

                try {
                    if (like.getBackground().getConstantState().equals(current.getConstantState())) {
                        like.setBackgroundResource(R.drawable.upvote_on_xxhdpi);
                        if (dislike.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.downvote_on_xxhdpi).getConstantState())) {
                            ratingView.setText(posOrNeg + (rating + 2));
                        } else {
                            ratingView.setText(posOrNeg + (rating + 1));
                        }
                        postRating(userId, menuId, menuItemId, 1);
                        dislike.setBackgroundResource(R.drawable.downvote_off_xxhdpi);
                        logger.info("Posted rating: positive ");
                    } else {
                        postRating(userId, menuId, menuItemId, 0);
                        like.setBackgroundResource(R.drawable.upvote_off_xxhdpi);
                        ratingView.setText(posOrNeg + (rating - 1));
                        logger.info("Posted rating: neutral ");
                    }
                } catch (Exception e) {
                    logger.info("Failed to like");
                    logger.warning(e.toString());
                }
            }
        });

        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View w) {
                Drawable current = getResources().getDrawable(R.drawable.downvote_off_xxhdpi);
                String temp = (String) ratingView.getText(), posOrNeg = "";
                int start1 = 0;
                if (ratingView.getCurrentTextColor() == Color.rgb(8, 124, 39)) {
                    posOrNeg = "+";
                    start1 = temp.indexOf('+') + 1;
                }
                int rating = Integer.parseInt(temp.substring(start1));

                try {
                    if (dislike.getBackground().getConstantState().equals(current.getConstantState())) {
                        postRating(userId, menuId, menuItemId, -1);
                        dislike.setBackgroundResource(R.drawable.downvote_on_xxhdpi);
                        if (like.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.upvote_on_xxhdpi).getConstantState())) {
                            ratingView.setText(posOrNeg + (rating - 2));
                        } else {
                            ratingView.setText(posOrNeg + (rating - 1));
                        }
                        like.setBackgroundResource(R.drawable.upvote_off_xxhdpi);
                        logger.info("Posted rating: negative ");
                    } else {
                        postRating(userId, menuId, menuItemId, 0);
                        dislike.setBackgroundResource(R.drawable.downvote_off_xxhdpi);
                        ratingView.setText(posOrNeg + (rating + 1));
                        logger.info("Posted rating: neutral ");
                    }
                } catch (Exception e) {
                    logger.info("Failed to dislike");
                }
            }
        });
    }

    public void postRating(final String userId, final int menuId, final int menuItemId, final int value) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... v) {
                try {
                    APIInterface api = new APIInterface();
                    api.postRating(userId, menuId, menuItemId, value);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
                return null;
            }
        }.execute();
    }
}
