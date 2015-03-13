package com.g10.gauchogrub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.logging.Logger;
import android.app.Fragment;
import android.webkit.WebView;

/**
 * Class to create and fill up a web view to display the web page where the user can view the number of swipes
 * they have left.
 */
public class SwipesFragment extends Fragment {

    public final static Logger logger = Logger.getLogger("FavoritesFragment");


    /**
     * Inflates the web page the UCSB Dining services site
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.swipes_fragment, container, false);
        WebView swipesView = (WebView)rootView.findViewById(R.id.webView);

        swipesView.loadUrl("https://webapps.ucen.ucsb.edu/plus/");
        return rootView;
    }
}