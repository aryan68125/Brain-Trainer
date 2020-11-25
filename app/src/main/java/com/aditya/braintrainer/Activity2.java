package com.aditya.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Activity2 extends AppCompatActivity {

    ListView listView;
    TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        textView4 = findViewById(R.id.textView4);
        listView = findViewById(R.id.listView);

        String [] info ={"Name","Branch","College Name", "college code", "Roll number", "course"};
        ListAdapter items = new ArrayAdapter<String>(this, R.layout.row, info);
        listView.setAdapter(items);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    textView4.setText("Aditya Kumar");
                }
                else if(position==1)
                {
                    textView4.setText("Computer science");
                }
                else if(position==2)
                {
                    textView4.setText("Saroj Institute of Technology");
                }
                else if(position == 3)
                {
                    textView4.setText("123");
                }
                else if(position==4)
                {
                    textView4.setText("1901230100001");
                }
                else if(position==5)
                {
                    textView4.setText("B.tech");
                }
            }
        });
    }
}