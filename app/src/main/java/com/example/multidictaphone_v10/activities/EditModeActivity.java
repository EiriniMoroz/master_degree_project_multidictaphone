package com.example.multidictaphone_v10.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.multidictaphone_v10.R;
import com.example.multidictaphone_v10.adapter.ClickListenerTrack;
import com.example.multidictaphone_v10.adapter.TrackAdapter;
import com.example.multidictaphone_v10.audioprocessing.Recordeplayer;
import com.example.multidictaphone_v10.models.Track;

import java.util.ArrayList;
import java.util.List;

public class EditModeActivity extends AppCompatActivity {

    TrackAdapter trackAdapter;
    RecyclerView trackRecycler;
    List<Track> trackList = new ArrayList<>();

    //from off doc
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // Requesting permission to RECORD_AUDIO
    private final boolean permissionToRecordAccepted = false;
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;



    //1
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }
    */
    //2
    /*
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                      //      Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                   // shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);




        //2
        /*
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        */
        trackList.add(new Track(1));
        trackList.add(new Track(2));
        trackList.add(new Track(3));




        setTrackAdapter(trackList);

        // Record to the external cache directory for visibility
        trackAdapter.externalCacheDir = getExternalCacheDir().getAbsolutePath();

        //1 from doc
        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);




    }

    private void setTrackAdapter(List<Track> trackList){

        trackRecycler = findViewById(R.id.track_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        trackRecycler.setLayoutManager(layoutManager);
        trackAdapter = new TrackAdapter(this, trackList, new ClickListenerTrack() {
            @Override
            public void deleteClicked(int position) {
               Log.d("", "will delete on position "+position);
               //trackList.remove(position);

                //setTrackAdapter(trackList);
                trackAdapter.trackList.remove(position);
                trackAdapter.recordeplayers.remove(position);
                trackAdapter.currentTrackPosition = position;

                trackRecycler.setAdapter(trackAdapter);//?

            }

            //TODO
            @Override
            public void recordBtnClicked(int position) {
                trackAdapter.currentTrackPosition = position;
                //is executing??
                //System.out.println("Is executing");
                //ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                   //     EXTERNAL_STORAGE_PERMISSION_CODE);

            }
            //TODO
            @Override
            public void stopRecordBtnClicked(int position) {
                trackAdapter.currentTrackPosition = position;

            }
            //TODO
            @Override
            public void playTrackBtnClicked(int position) {
                trackAdapter.currentTrackPosition = position;

            }
            //TODO
            @Override
            public void stopPlayingTrackBtnClicked(int position) {
                trackAdapter.currentTrackPosition = position;

            }
        });

        trackRecycler.setAdapter(trackAdapter);



    }
    public void addNewTrack(View view){

        trackAdapter.addNewTrack();
        trackRecycler.setAdapter(trackAdapter);

    }
    public void deleteTrack(View view){

    }
    public void playAll(View view){
        trackAdapter.playAllTracks();
    }

}