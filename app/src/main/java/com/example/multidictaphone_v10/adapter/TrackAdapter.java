package com.example.multidictaphone_v10.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multidictaphone_v10.R;
import com.example.multidictaphone_v10.audioprocessing.Recordeplayer;
import com.example.multidictaphone_v10.models.Track;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    Context context;
    public List<Track> trackList;
    private static int recordsCount = 3;
    public static List<Recordeplayer> recordeplayers = new ArrayList<Recordeplayer>();
    private final ClickListenerTrack listener;
    private static List<Integer> imageButtonRecordStopPlayCLICK_COUNTS;
    public static int currentTrackPosition;


    public TrackAdapter(Context context,List<Track> trackList, ClickListenerTrack listener) {
        this.context = context;
        this.trackList = trackList;
        this.listener = listener;
        imageButtonRecordStopPlayCLICK_COUNTS = new ArrayList<Integer>(Collections.nCopies(trackList.size(), 0));

        recordeplayers.add(new Recordeplayer());
        recordeplayers.add(new Recordeplayer());
        recordeplayers.add(new Recordeplayer());

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

    public final class TrackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageButton imageButtonDeletion;
        private ImageButton imageButtonRecordStopPlay;

        private WeakReference<ClickListenerTrack> listenerRef;
        TextView trackName;
        public TrackViewHolder(@NonNull View itemView, ClickListenerTrack listener) {
            super(itemView);

            trackName = itemView.findViewById(R.id.trackName);
            listenerRef = new WeakReference<>(listener);

            imageButtonDeletion = (ImageButton) itemView.findViewById(R.id.deleteTrackBtn);
            imageButtonRecordStopPlay = (ImageButton) itemView.findViewById(R.id.recordOrStopRecordOrPlayBtn);
            //          ..//..

            itemView.setOnClickListener(this);
            imageButtonDeletion.setOnClickListener(this);
            imageButtonRecordStopPlay.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            if (v.getId() == imageButtonDeletion.getId()) {
                Log.d("", "iconTextViewOnClick at position delete");

                listenerRef.get().deleteClicked(getAdapterPosition());
                //Log.d("currentTrackPosition", " " + currentTrackPosition);
                imageButtonRecordStopPlayCLICK_COUNTS.remove(currentTrackPosition);

            }
            if (v.getId() == imageButtonRecordStopPlay.getId()) {
                imageButtonRecordStopPlayCLICK_COUNTS.set(getAdapterPosition(), imageButtonRecordStopPlayCLICK_COUNTS.get(getAdapterPosition())+1);
                Log.d("", "iconTextViewOnClick at position imageButtonRecordStopPlay");

                switch(imageButtonRecordStopPlayCLICK_COUNTS.get(getAdapterPosition())) {
                    case 1: //start record
                        listenerRef.get().recordBtnClicked(getAdapterPosition());
                        new Thread(new Runnable() {
                            public void run() {
                                imageButtonRecordStopPlay.post(new Runnable() {
                                    public void run() {
                                        imageButtonRecordStopPlay.setImageResource(R.drawable.stoprecordingbutton); //не реагувати на це червоне
                                    }
                                });
                                recordeplayers.get(currentTrackPosition).RecordAudio("NewRecord" +
                                        String.valueOf((TrackAdapter.this.trackList.get(currentTrackPosition).getId())));
                            }
                        }).start();

                        break;
                    case 2:
                        listenerRef.get().stopRecordBtnClicked(getAdapterPosition());
                        new Thread(new Runnable() {
                            public void run() {
                                imageButtonRecordStopPlay.post(new Runnable() {
                                    public void run() {
                                        imageButtonRecordStopPlay.setImageResource(R.drawable.play); //не реагувати на це червоне
                                    }
                                });
                                 recordeplayers.get(currentTrackPosition).StopRecorder();
                            }
                        }).start();
                        break;
                    default:
                        switch (imageButtonRecordStopPlayCLICK_COUNTS.get(getAdapterPosition()) %2){
                            case 1:
                                listenerRef.get().playTrackBtnClicked(getAdapterPosition());
                                new Thread(new Runnable() {
                                    public void run() {
                                        imageButtonRecordStopPlay.post(new Runnable() {
                                            public void run() {
                                                imageButtonRecordStopPlay.setImageResource(R.drawable.stopplaying); //не реагувати на це червоне
                                            }
                                        });
                                        recordeplayers.get(currentTrackPosition).StartPlayer("NewRecord" +
                                        String.valueOf((TrackAdapter.this.trackList.get(currentTrackPosition).getId())));
                                    }
                                }).start();
                                break;
                            case 0:
                                listenerRef.get().stopPlayingTrackBtnClicked(getAdapterPosition());
                                new Thread(new Runnable() {
                                    public void run() {
                                        imageButtonRecordStopPlay.post(new Runnable() {
                                            public void run() {
                                                imageButtonRecordStopPlay.setImageResource(R.drawable.play); //не реагувати на це червоне
                                            }
                                        });
                                        recordeplayers.get(currentTrackPosition).StopPlayer();
                                    }
                                }).start();
                                break;
                        }
                        break;
                }
                listenerRef.get().recordBtnClicked(getAdapterPosition());


            }else {
            }

        }
    }

    public void playAllTracks() {
        for (Recordeplayer r : recordeplayers) {
            if (r.getStatus() == "stopped recorder") {
                new Thread(new Runnable() {
                    public void run() {
                        r.StartPlayer(r.getName());
                    }
                }).start();
            }
        }
    }

    public void addNewTrack(){

        trackList.add(new Track(++recordsCount));
        recordeplayers.add(new Recordeplayer());
        imageButtonRecordStopPlayCLICK_COUNTS.add(0);
    }

    public void deleteTrack(int position){

    }

}
