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

    /**
     *
     * @param index
     */
    public void setDisplayContent(int index) {
        // An item was selected. You can retrieve the selected item using parent.getItemAtPosition(pos)
        String[] camUrls = new String[]{DiningCam.CARRILLO, DiningCam.DE_LA_GUERRA, DiningCam.ORTEGA};
        String camUrl = camUrls[index];
        this.currentCam = new DiningCam(camUrl);
        this.startCam();
    }

    /**
     * onPause() is an internally called method overridden to prevent the page
     * from loading data while the screen is off
     */
    @Override
    public void onPause() {
        super.onPause();
        if (this.currentCam != null) {
            this.stopCam();
        }
    }

    /**
     * onResume() restarts the page-loading when the screen is turned back on,
     * or does an initial load as a background check.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (this.currentCam != null) {
            this.startCam();
        }
    }

    /**
     * startCam() updates the image immediately
     */
    private void startCam() {
        this.isOn = true;
        this.handler.post(this); // Update image right now
    }

    /**
     * stopCam() stops the camera from being loaded
     */
    private void stopCam() {
        this.isOn = false;
        this.handler.removeCallbacks(this);
    }

    /**
     * Asynchronous task to load the image on a separate thread
     */
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

    /**
     * scheduleCamUpdate() handles the delay period for the camera refresh
     */
    private void scheduleCamUpdate() {
        if (isOn) {
            handler.postDelayed(this, delay);
        }
    }

    /**
     * inherited method from BaseTabbedFragment that handles creation of content within a
     * dining common tab
     * @return the current imageView after updates
     */
    public TabContentFactory createTabContent() {
        return new TabContentFactory() {
            public View createTabContent(String tag) {
                setDisplayContent(Integer.parseInt(tag));
                return imageView;
            }
        };
    }
}