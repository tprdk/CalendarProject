package com.example.calendarproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<CalendarDay> days;
    private ArrayList<Event> events;
    private MaterialCalendarView calendarView;
    private FloatingActionButton floatingActionButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Defaults defaults = SharedPref.getDefaults(MainActivity.this);

        if(defaults.getDefaultThemeId() == 1 && AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        floatingActionButton = findViewById(R.id.button_change_mode);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeeklyMainActivity.class);
                startActivity(intent);
            }
        });

        calendarView = findViewById(R.id.calendarView);
        DecorateToday decorator = new DecorateToday();
        calendarView.addDecorator(decorator);

    }

    @Override
    protected void onResume() {
        super.onResume();

        days = new ArrayList<CalendarDay>();
        events = SharedPref.loadEventList(MainActivity.this);

        CalendarDay calendarDay = CalendarDay.today();
        for(Event event : events){
            if(event.getStartDate().getTime() != calendarDay.getDate())
                days.add(calendarDay.from(event.getStartDate()));
        }

        DecorateEvent addEventDecorate = new DecorateEvent(this, days, DecorateEvent.BOOL_DRAW);
        calendarView.addDecorator(addEventDecorate);

        DecorateEvent clearEventDecorate = new DecorateEvent(this, SharedPref.getWillDeleteDecorate(MainActivity.this), DecorateEvent.BOOL_CLEAR);
        calendarView.addDecorator(clearEventDecorate);
        SharedPref.saveWillDeleteDecorate(MainActivity.this, new ArrayList<CalendarDay>());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_search){
            Intent intent = new Intent(MainActivity.this, EventList.class);
            startActivity(intent);
            return true;
        }else if (item.getItemId() == R.id.item_add){
            Intent intent = new Intent(MainActivity.this, AddEvent.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.item_settings){
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
