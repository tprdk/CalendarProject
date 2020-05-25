package com.example.calendarproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class EventList extends AppCompatActivity {

    private RecyclerView eventRV;
    private EventAdapter eventAdapter;
    private RecyclerView.LayoutManager eventLayoutManager;
    private static ArrayList<Event> events;
    private boolean recreate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        events = SharedPref.loadEventList(this);
        initView();

        eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                recreate = true;
                Intent setEventIntent = new Intent(EventList.this, SetEvent.class);
                setEventIntent.putExtra("Position", position);
                startActivity(setEventIntent);
                Toast.makeText(EventList.this, "Pos = " + position , Toast.LENGTH_LONG).show();
            }
        });

    }

    public void initView(){
        eventRV = findViewById(R.id.recyclerView);
        eventRV.setHasFixedSize(true);

        eventLayoutManager = new LinearLayoutManager(this);
        eventAdapter = new EventAdapter(events);

        eventRV.setLayoutManager(eventLayoutManager);
        eventRV.setAdapter(eventAdapter);
    }

    @Override
    protected void onResume() {
        if(recreate){
            recreate();
            recreate = false;
        }
        super.onResume();
    }
}
