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
import android.widget.Spinner;

import com.g10.gauchogrub.R;


public class DiningCamsFragment extends Fragment implements AdapterView.OnItemSelectedListener, Runnable {

    private DiningCam currentCam;
    private Handler handler;
    private ImageView imageView;
    private final int delay = 2 * 1000;  // Milliseconds (2 seconds)
    private boolean isOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dining_cams_fragment, container, false);

        // Initialize Spinner
        Spinner spinner = (Spinner)rootView.findViewById(R.id.dining_cams_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dining_cams_array, R.layout.dining_cams_spinner_item);
        adapter.setDropDownViewResource(R.layout.dining_cams_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        this.imageView = (ImageView) rootView.findViewById(R.id.dining_cam_image_view);
        this.handler = new Handler();

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String[] camUrls = new String[]{DiningCam.Carrillo, DiningCam.DeLaGuerra, DiningCam.Ortega};
        String camUrl = camUrls[pos];
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
