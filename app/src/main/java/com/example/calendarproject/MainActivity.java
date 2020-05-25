package com.example.calendarproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private ArrayList<CalendarDay> days;
    private ArrayList<Event> events;
    private MaterialCalendarView calendarView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Defaults defaults = SharedPref.getDefaults(MainActivity.this);
        Log.d("Mode", AppCompatDelegate.getDefaultNightMode() + " ");
        if(defaults.getDefaultThemeId() == 1 && AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        calendarView = findViewById(R.id.calendarView);

        DecorateToday decorator = new DecorateToday(Color.GRAY);
        calendarView.addDecorator(decorator);

    }

    @Override
    protected void onResume() {
        super.onResume();

        days = new ArrayList<CalendarDay>();
        events = SharedPref.loadEventList(MainActivity.this);

        CalendarDay calendarDay = CalendarDay.today();
        for(Event event : events){
            Log.d("Date", "before day = " + calendarDay.getDay() + " month = " + calendarDay.getMonth() + " year = " + calendarDay.getYear());
            days.add(calendarDay.from(event.getStartDate()));
            Log.d("Date", "after day = " + calendarDay.getDay() + " month = " + calendarDay.getMonth() + " year = " + calendarDay.getYear());
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
