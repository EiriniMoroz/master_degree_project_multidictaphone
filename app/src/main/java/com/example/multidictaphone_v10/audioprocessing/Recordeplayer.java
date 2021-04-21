package com.example.multidictaphone_v10.audioprocessing;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Recordeplayer {
    private List<Object> tracks;
    protected MediaPlayer player;
    public MediaRecorder recorder;
    public String state;

    public void StartPlayer(String filePath)
    {
        try
        {
            if (player == null)
                player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            player.start();
            state = "playing";
        }
        catch(Exception ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }

    public void PausePlayer()
    {
        player.pause();
    }
    public void PlayPlayer()
    {
        player.start();
    }
    public void StopPlayer()
    {
        player.stop();
        player = null;
    }

    public String getStatus()
    {
        return state;
    }

    public void StopRecorder()
    {
        try
        {
            if (recorder != null)
            {
                recorder.stop();
                recorder.reset();    // set state to idle
                recorder.release();
                recorder = null;
                state = "stopped recorder";
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RecordAudio(String filePath)
    {
        try
        {
            if (recorder != null)
            {
                recorder.release();
            }
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            //if (File.Exists(filePath)) File.Delete(filePath);
            Files.deleteIfExists(Paths.get(filePath));

            recorder.setOutputFile(filePath);
            System.out.println("saving in " + filePath);

            recorder.prepare();
            recorder.start();
            state = "is recording";
        }
        catch (Exception ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }

}
