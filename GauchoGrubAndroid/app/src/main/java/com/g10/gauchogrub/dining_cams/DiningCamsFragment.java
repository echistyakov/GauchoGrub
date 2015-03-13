package com.g10.gauchogrub.dining_cams;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import com.g10.gauchogrub.BaseTabbedFragment;
import com.g10.gauchogrub.R;

public class DiningCamsFragment extends BaseTabbedFragment implements Runnable {

    private DiningCam currentCam;
    private Handler handler;
    private ImageView imageView;
    private final int delay = 10 * 1000;  // Milliseconds (10 seconds)
    private boolean isOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        View rootView = inflater.inflate(R.layout.dining_cams_fragment, container, false);
        this.imageView = (ImageView) rootView.findViewById(R.id.dining_cam_image_view);
        this.handler = new Handler();

        TabHost tabs = (TabHost) rootView.findViewById(R.id.tabHost);
        this.setUpTabs(tabs, createTabContent(), 3);

        return rootView;
    }

    public void setDisplayContent(int index) {
        // An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos)
        String[] camUrls = new String[]{DiningCam.CARRILLO, DiningCam.DE_LA_GUERRA, DiningCam.ORTEGA};
        String camUrl = camUrls[index];
        this.currentCam = new DiningCam(camUrl);
        this.startCam();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.currentCam != null) {
            this.stopCam();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.currentCam != null) {
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
            protected void onPostExecute(Bitmap result) {
                imageView.setImageBitmap(result);
                scheduleCamUpdate();
            }
        }.execute();
    }

    private void scheduleCamUpdate() {
        if (isOn) {
            handler.postDelayed(this, delay);
        }
    }

    public TabContentFactory createTabContent() {
        return new TabContentFactory() {
            public View createTabContent(String tag) {
                setDisplayContent(Integer.parseInt(tag));
                return imageView;
            }
        };
    }
}