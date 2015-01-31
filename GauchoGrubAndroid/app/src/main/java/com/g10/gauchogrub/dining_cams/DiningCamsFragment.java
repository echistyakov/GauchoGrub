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
    private final int delay = 3 * 1000;  // Milliseconds (2 seconds)
    private boolean isOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dining_cams_fragment, container, false);

        // Initialize Spinner
        Spinner spinner = (Spinner)rootView.findViewById(R.id.dining_cams_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dining_commons_array, R.layout.dining_cams_spinner_item);
        adapter.setDropDownViewResource(R.layout.dining_cams_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        this.imageView = (ImageView) rootView.findViewById(R.id.dining_cam_image_view);
        this.handler = new Handler();

        return rootView;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        // TODO: change dining cam

        this.currentCam = new DiningCam(DiningCam.Carrillo);
        this.startCam();
        //this.run();
    }

    private void startCam() {
        this.isOn = true;
        this.handler.post(this); // Update image right now
    }

    private void stopCam() {
        this.isOn = false;
    }

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

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
