package com.example.multidictaphone_v10.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Melody>  melodyList = new ArrayList<>();
        melodyList.add(new Melody(1,"name1", new Date(System.currentTimeMillis())));
        melodyList.add(new Melody(1,"name1", new Date(System.currentTimeMillis())));


        setMelodyRecycler(melodyList);
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
}