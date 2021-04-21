package com.example.multidictaphone_v10.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multidictaphone_v10.R;
import com.example.multidictaphone_v10.audioprocessing.Recordeplayer;
import com.example.multidictaphone_v10.models.Track;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    Context context;
    List<Track> trackList;
    private static int recordsCount = 3;
    private static List<Recordeplayer> recordeplayers = new ArrayList<Recordeplayer>();
    private final ClickListenerTrack listener;


    public TrackAdapter(Context context,List<Track> trackList, ClickListenerTrack listener) {
        this.context = context;
        this.trackList = trackList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.activity_track, parent, false);
        return new TrackViewHolder(view,listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {

        holder.trackName.setText("NewRecord" + String.valueOf((trackList.get(position).getId())));

    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public static final class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageButton imageButtonDeletion;
        private ImageButton imageButtonPlay;

        private WeakReference<ClickListenerTrack> listenerRef;
        TextView trackName;
        public TrackViewHolder(@NonNull View itemView, ClickListenerTrack listener) {
            super(itemView);

            trackName = itemView.findViewById(R.id.trackName);
            listenerRef = new WeakReference<>(listener);

            imageButtonDeletion = (ImageButton) itemView.findViewById(R.id.deleteTrackBtn);
            //          ..//..

            itemView.setOnClickListener(this);
            imageButtonDeletion.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == imageButtonDeletion.getId()) {
                Log.d("", "iconTextViewOnClick at position delete");

                listenerRef.get().deleteClicked(getAdapterPosition());


                //Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }

        }
    }

    void playAllTracks(){

    }
    public void addNewTrack(){
        trackList.add(new Track(++recordsCount));
    }

    public void deleteTrack(int position){

    }

}
