package com.example.multidictaphone_v10.audioprocessing;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.content.Context;


import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Recordeplayer { //на кожен трек свій рекордеплеєр
    protected MediaPlayer player;
    public MediaRecorder recorder;
    public String state;
    private String name;
    private final String externalCacheDir;

    public Recordeplayer(String externalCacheDir){
        this.externalCacheDir=externalCacheDir;
    }
    public String getName() {
        return name;
    }

    public void StartPlayer(String filePath)
    {
        try
        {
            if (player == null)
                player = new MediaPlayer();
            File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"records1");

            player.setDataSource(directory.getAbsolutePath()+ "/" + filePath + ".3gp");
            System.out.println("path is " + directory.getAbsolutePath()+ "/" + filePath + ".3gp");
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

    public void RecordAudio(String filePath)
    {
        try
        {
            if (recorder != null)
            {
                recorder.release();
            }
            else { //added 28.01.2022
                recorder = new MediaRecorder();
            }
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            //if (File.exists(filePath)) File.delete(filePath);

            //Files.deleteIfExists(Paths.get(filePath)); //this was commented

            //1
            /*
            File dir = Environment.getExternalStorageDirectory();
            File audiofile = null;
            Log.e("TAG", "filePath is " + filePath);
            Log.e("TAG", "dir is " + dir);
            try {
                audiofile = File.createTempFile(filePath, ".3gp", dir);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "external storage access error");
                return;
            }

             */

            ///2
            // Record to the external cache directory for visibility
            //String fileName = externalCacheDir;
            //fileName += "/" + filePath + ".3gp";

            //3
            /*File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES);
            File file = new File(path, "/" + filePath + ".3gp");


             */
            //System.out.println("audiofile.getAbsolutePath()"+ audiofile.getAbsolutePath());

            //from ExternalStorageTrialApp
            /*File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // Storing the data in file with name as geeksData.txt
            try {
                File file = new File("/storage/emulated/0/",  filePath + ".3gp");
                //System.out.println("file.getAbsolutePath()"+ file.getAbsolutePath());

            }
            catch (Exception e){
                e.printStackTrace();
            }

             */

            //simply
            //File audioDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
              //      "/audioRecords");
            File directory = new File(Environment.getExternalStorageDirectory()+File.separator+"records1");
            //directory.mkdirs();

            boolean flag;
            if (!directory.exists()) {
                System.out.println("not exists");
                try {
                    flag = directory.mkdirs();
                    System.out.println("flag is " + flag);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            /*
            String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
            // Get the date in simple format
            SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
            String date = s.format(new Date());

            // Add the date and file name to the path of the directory
            // The filename will look like: "audioRecord_ddMMyyyyhhnmmss.3gp"
            String appendToOutputFile = "/audioRecords/audioRecord_" + date + ".3gp";

            outputFile += appendToOutputFile;
*/


            System.out.println("res is " + directory+"/"+filePath+".3gp");
            recorder.setOutputFile(directory+"/"+filePath+".3gp"); // this was modified 20/05
            //Log.d("","savinjg in " + filePath);
            //System.out.println("saving2 in " + "/storage/emulated/0"+"/"+filePath+".3gp");//non functiona

            try {
                recorder.prepare();
            } catch (IOException e) {
                Log.e("LOG_TAG", "prepare() failed");
                e.printStackTrace();
            }
            recorder.start();
            state = "is recording";
            name = filePath;
        }
        catch (Exception ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }

}
