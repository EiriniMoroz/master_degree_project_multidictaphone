package com.example.multidictaphone_v10.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

import com.example.multidictaphone_v10.R;
import com.example.multidictaphone_v10.adapter.MelodyAdapter;
import com.example.multidictaphone_v10.models.Melody;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    MelodyAdapter melodyAdapter;
    RecyclerView melodyRecycler;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int EXTERNAL_STORAGE_PERMISSION_CODE = 23;


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     *
     */
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Read perm is not good");
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Melody>  melodyList = new ArrayList<>();
        melodyList.add(new Melody(1,"name1", new Date(System.currentTimeMillis())));
        melodyList.add(new Melody(1,"name1", new Date(System.currentTimeMillis())));


        setMelodyRecycler(melodyList);
        verifyStoragePermissions();

        runDocPerm();

        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
          //      23);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
             //   EXTERNAL_STORAGE_PERMISSION_CODE);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        //storageMode();
    }

    private void runDocPerm() {
        // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
         ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            //performAction(...);
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void setMelodyRecycler(List<Melody> melodiesList){

        melodyRecycler = findViewById(R.id.melodyList_recycler);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.VERTICAL,
                false);
        melodyRecycler.setLayoutManager(layoutManager);
        melodyAdapter = new MelodyAdapter(this, melodiesList);
        melodyRecycler.setAdapter(melodyAdapter);
    }

    public void openEditView(View view) {
        Intent intent = new Intent(this, EditModeActivity.class);
        //EditText editText = (EditText) findViewById(R.id.editText);
        //String message = editText.getText().toString();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void storageMode(){
        boolean isAvailable= false;
        boolean isWritable= false;
        boolean isReadable= false;
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)) {
// Operation possible - Read and Write
            isAvailable= true;
            isWritable= true;
            isReadable= true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Operation possible - Read Only
            isAvailable= true;
            isWritable= false;
            isReadable= true;
        } else {
            // SD card not available
            isAvailable = false;
            isWritable= false;
            isReadable= false;
        }
        System.out.println(String.valueOf(isAvailable)+ isReadable + isWritable);

    }
}