package com.example.calendarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeeklyMainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_main);

        floatingActionButton = findViewById(R.id.button_change_mode);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeeklyMainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        AgendaCalendarView mAgendaCalendarView = findViewById(R.id.agenda_calendar_view);
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        List<CalendarEvent> eventList = new ArrayList<>();
        mockList(eventList);

        CalendarPickerController controller = new CalendarPickerController() {
            @Override
            public void onDaySelected(DayItem dayItem) {
            }

            @Override
            public void onEventSelected(CalendarEvent event) {
                if(!event.getTitle().equals("No events")){
                    Intent setEventIntent = new Intent(WeeklyMainActivity.this, SetEvent.class);
                    long l = event.getId();
                    int position = (int) l;
                    setEventIntent.putExtra("Position", position);
                    startActivity(setEventIntent);
                }
            }

            @Override
            public void onScrollToDate(Calendar calendar) {

            }
        };

        mAgendaCalendarView.init(eventList, minDate, maxDate, Locale.getDefault(), controller);
    }

    private void mockList(List<CalendarEvent> eventList) {

        ArrayList<Event> events = SharedPref.loadEventList(WeeklyMainActivity.this);

        for(Event event : events){
            Calendar start = calculateDiff(event.getStartDate());
            Calendar end =   calculateDiff(event.getEndDate());
            BaseCalendarEvent newEvent = new BaseCalendarEvent(event.getTitle(), event.getNotes(), event.getAdress(),
                    ContextCompat.getColor(this, R.color.colorAccent), start, end, false);
            newEvent.setId(events.indexOf(event));
            eventList.add(newEvent);
        }
    }

    private Calendar calculateDiff(Calendar eventStart){
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int diff_year = eventStart.get(Calendar.YEAR) - year;
        int diff_month = eventStart.get(Calendar.MONTH) - month;
        int diff_day = eventStart.get(Calendar.DAY_OF_MONTH) - day;

        calendar.add(Calendar.YEAR, diff_year);
        calendar.add(Calendar.MONTH, diff_month);
        calendar.add(Calendar.DAY_OF_YEAR, diff_day);

        return calendar;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_search){
            Intent intent = new Intent(WeeklyMainActivity.this, EventList.class);
            startActivity(intent);
            return true;
        }else if (item.getItemId() == R.id.item_add){
            Intent intent = new Intent(WeeklyMainActivity.this, AddEvent.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.item_settings){
            Intent intent = new Intent(WeeklyMainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
