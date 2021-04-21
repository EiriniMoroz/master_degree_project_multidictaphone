package com.example.multidictaphone_v10.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.multidictaphone_v10.R;
import com.example.multidictaphone_v10.adapter.ClickListenerTrack;
import com.example.multidictaphone_v10.adapter.TrackAdapter;
import com.example.multidictaphone_v10.models.Track;

import java.util.ArrayList;
import java.util.List;

public class EditModeActivity extends AppCompatActivity {

    TrackAdapter trackAdapter;
    RecyclerView trackRecycler;
    List<Track> trackList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mode);

        trackList.add(new Track(1));
        trackList.add(new Track(2));
        trackList.add(new Track(3));


        setTrackAdapter(trackList);
    }

    private void setTrackAdapter(List<Track> trackList){

        trackRecycler = findViewById(R.id.track_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        trackRecycler.setLayoutManager(layoutManager);
        trackAdapter = new TrackAdapter(this, trackList, new ClickListenerTrack() {
            @Override
            public void deleteClicked(int position) {
               Log.d("", "will delete on position "+position);
               trackList.remove(position);
               setTrackAdapter(trackList);
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

}