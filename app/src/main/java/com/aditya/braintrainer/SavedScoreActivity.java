package com.aditya.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SavedScoreActivity extends AppCompatActivity {

    /*
         in order to send data onto the next Activity we need to define the list as a static
         this ArrayList can be accessed from any activity
          */
    static ArrayList<String> savedScore  = new ArrayList<>();

    //defining ArrayAdapter
    static ArrayAdapter arrayAdapter;

    //**
    private static int REQUEST_CODE = 6384;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_score);
        savedScore.clear();
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.aditya.braintrainer", Context.MODE_PRIVATE);

        try{

            //getting stored data from the sharedPreferences and deserializing them and placing them into their respective arrayList
            savedScore = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("scoreList",ObjectSerializer.serialize(new ArrayList<String>())));

        }catch (Exception e){
            e.printStackTrace();
        }

        //creating ListView to hold data
        ListView SavedScoreListView = (ListView)findViewById(R.id.SavedScoreListView);
        //now we are setting up the places ArrayList to be shown via ArrayAdapter inside of the ListView
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,savedScore);
        //connecting our ListView and Our ArrayAdapter
        SavedScoreListView.setAdapter(arrayAdapter);

        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                remember it rill only work with solid Explorer and Root Explorer no other file manager supports opening of directory
                using intent calls anymore
                 */
                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/Brain_Trainer_game/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "*/*");

                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(intent);
                }
                else
                {
                    // if you reach this place, it means there is no any file
                    // explorer app installed on your device
                    Toast.makeText(getApplicationContext(), "No such directory",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}