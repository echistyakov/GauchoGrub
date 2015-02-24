package com.g10.gauchogrub.dining_cams;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TabHost;

import com.g10.gauchogrub.R;


public class DiningCamsFragment extends Fragment implements Runnable {

    private DiningCam currentCam;
    private Handler handler;
    private ImageView imageView;
    private final int delay = 10 * 1000;  // Milliseconds (10 seconds)
    private boolean isOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dining_cams_fragment, container, false);
        this.imageView = (ImageView) rootView.findViewById(R.id.dining_cam_image_view);
        this.handler = new Handler();

        TabHost tabs = (TabHost)rootView.findViewById(R.id.tabHost);
        this.setUpTabs(tabs);

        return rootView;
    }

    public void setCam(String tag) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String[] camUrls = new String[]{DiningCam.Carrillo, DiningCam.DeLaGuerra, DiningCam.Ortega};
        String camUrl = camUrls[Integer.parseInt(tag)];
        this.currentCam = new DiningCam(camUrl);
        this.startCam();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(this.currentCam != null) {
            this.stopCam();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.currentCam != null) {
            this.startCam();
        }
    }

    private void startCam() {
        this.isOn = true;
        this.handler.post(this); // Update image right now
    }

    private void stopCam() {
        this.isOn = false;
        this.handler.removeCallbacks(this);
    }

    @Override
    public void run() {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... v) {
                return currentCam.getCurrentImage(delay);
            }
            @Override
            protected void onPostExecute(Bitmap result){
                imageView.setImageBitmap(result);
                scheduleCamUpdate();
            }
        }.execute();
    }

    private void scheduleCamUpdate() {
        if(isOn) {
            handler.postDelayed(this, delay);
        }
    }

    public void setUpTabs(TabHost tabs){
        String[] commons = new String[] {"Carillo","DLG","Ortega","Portola"};

        //Set the initial tab content
        TabHost.TabContentFactory contentCreate = new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                if(!tag.equals("3")) { setCam(tag); }
                return (imageView);
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
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if(!tabId.equals("3")) { setCam(tabId); }
            }});
    }




}