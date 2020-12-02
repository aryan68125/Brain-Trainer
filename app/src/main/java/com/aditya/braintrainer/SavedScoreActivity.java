package com.aditya.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SavedScoreActivity extends AppCompatActivity {

    /*
        in order to send data onto the next Activity we need to define the list as a static
        this ArrayList can be accessed from any activity
         */
    static ArrayList<String> savedScore  = new ArrayList<>();

    //defining ArrayAdapter
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_score);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.aditya.braintrainer", Context.MODE_PRIVATE);
        savedScore.clear();
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

    }
}