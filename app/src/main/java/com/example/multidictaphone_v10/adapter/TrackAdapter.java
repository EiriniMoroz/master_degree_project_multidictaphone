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
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public String externalCacheDir = "";


    public TrackAdapter(Context context,List<Track> trackList, ClickListenerTrack listener) {
        this.context = context;
        this.trackList = trackList;
        this.listener = listener;
        imageButtonRecordStopPlayCLICK_COUNTS = new ArrayList<Integer>(Collections.nCopies(trackList.size(), 0));

        recordeplayers.add(new Recordeplayer(externalCacheDir));
        recordeplayers.add(new Recordeplayer(externalCacheDir));
        recordeplayers.add(new Recordeplayer(externalCacheDir));

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

        private final ImageButton imageButtonDeletion;
        private final ImageButton imageButtonRecordStopPlay;

        private final WeakReference<ClickListenerTrack> listenerRef;
        TextView trackName;
        public TrackViewHolder(@NonNull View itemView, ClickListenerTrack listener) {
            super(itemView);

            trackName = itemView.findViewById(R.id.trackName);
            listenerRef = new WeakReference<>(listener);

            imageButtonDeletion = itemView.findViewById(R.id.deleteTrackBtn);
            imageButtonRecordStopPlay = itemView.findViewById(R.id.recordOrStopRecordOrPlayBtn);
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
                        //Log.d("1","11111");
                        new Thread(new Runnable() {
                            public void run() {
                                //Log.d("2","2222");

                                imageButtonRecordStopPlay.post(new Runnable() {
                                    public void run() {
                                        imageButtonRecordStopPlay.setImageResource(R.drawable.stoprecordingbutton); //не реагувати на це червоне
                                    }
                                });
                                recordeplayers.get(currentTrackPosition).RecordAudio("NewRecord" +
                                        String.valueOf((TrackAdapter.this.trackList.get(currentTrackPosition).getId())));
                               // Log.d("","state is " + recordeplayers.get(currentTrackPosition).recorder.);
                                System.out.println("record started");
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
                                System.out.println("record finished");

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
                                        System.out.println("play started");

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
                                        System.out.println("play stopped");

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void playAllTracks() {

        List<Path> pathes = new ArrayList<>();
        List<byte[]> bytes = new ArrayList<>();
        File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"records1");

        //28.04
        //Path path = Paths.get("C:", "temp", "test.txt");

        //28.04
        /*
        for(Recordeplayer recordeplayer:recordeplayers){
            //pathes.add(Paths.get(directory.getPath(),recordeplayer.getName()+".3gpp"));//functiona??
            pathes.add(FileSystems.getDefault().getPath(directory.getPath(),recordeplayer.getName()+".3gpp"));//functiona??
            System.out.println("the path is " + FileSystems.getDefault().getPath(directory.getPath(),recordeplayer.getName()+".3gpp"));
        }

        for(Path s: pathes){
            try {
                bytes.add(Files.readAllBytes(s));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] out = new byte[bytes.get(0).length];

        //


        byte bytesum=0;
        for (int i=0; i<bytes.get(0).length; i++) {
            for (byte[] b : bytes) {
                bytesum += b[i];
                out[i] = (byte) (bytesum >> 1);
            }
            bytesum = 0;
        }

         */

        // from febr
        for (Recordeplayer r : recordeplayers) {
            System.out.println("status is " + r.getStatus());
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
        recordeplayers.add(new Recordeplayer(externalCacheDir));
        imageButtonRecordStopPlayCLICK_COUNTS.add(0);
    }

    public void deleteTrack(int position){

    }

}
