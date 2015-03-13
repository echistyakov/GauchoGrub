package com.g10.gauchogrub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.logging.Logger;
import android.app.Fragment;
import android.webkit.WebView;

public class SwipesFragment extends Fragment {

    private final static Logger logger = Logger.getLogger("SwipesFragment");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.swipes_fragment, container, false);
        WebView swipesView = (WebView)rootView.findViewById(R.id.webView);

        swipesView.loadUrl("https://webapps.ucen.ucsb.edu/plus/");
        return rootView;
    }
}