package com.example.calendarproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddEvent extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private Spinner spinnerRepeat;
    private TextView textViewStartDate, textViewEndDate, textViewStartTime, textViewEndTime, textViewLocation, textViewAlarm;
    private EditText editTextTitle, editTextNotes;
    private Button buttonSave;

    private Defaults defaults;
    private ArrayAdapter<CharSequence> repeatAdapter;
    private ArrayList<Alarm> alarms;
    private ArrayList<Alarm> willCancelAlarm;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    boolean start_time_bool = false;
    boolean end_time_bool = false;
    boolean start_date_bool = false;
    boolean end_date_bool = false;

    private double longitude;
    private double latitude;
    private static final int REQUEST_CODE_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initView();
        setListeners();

    }

    private void initView(){
        defaults = SharedPref.getDefaults(AddEvent.this);
        textViewStartDate = findViewById(R.id.textView_startDate);
        textViewEndDate = findViewById(R.id.textView_endDate);
        textViewStartTime = findViewById(R.id.textView_startTime);
        textViewEndTime = findViewById(R.id.textView_endTime);
        textViewLocation = findViewById(R.id.textView_location);
        textViewAlarm = findViewById(R.id.textView_alarm);

        editTextNotes = findViewById(R.id.textView_notes);
        editTextTitle = findViewById(R.id.textView_title);

        spinnerRepeat = findViewById(R.id.spinner_repeat);
        repeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_options, android.R.layout.simple_spinner_item);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(repeatAdapter);
        spinnerRepeat.setSelection(defaults.getDefaultRepeatId());

        buttonSave = findViewById(R.id.button_save);

        willCancelAlarm = new ArrayList<Alarm>();
        SharedPref.deleteAlarmTimes(AddEvent.this);       //alarm listesini sildik Add event için
    }


    private void setListeners(){

        textViewStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date_bool = true;
                showDatePicker();
            }
        });

        textViewEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_date_bool = true;
                showDatePicker();
            }
        });

        textViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time");
                start_time_bool = true;
            }
        });

        textViewEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end_time_bool = true;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time");
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                if(end_date_bool){
                    end_date_bool = false;
                    textViewEndDate.setText(dayOfMonth + " / " + month + " / "+year);
                }else if(start_date_bool){
                    start_date_bool = false;
                    textViewStartDate.setText(dayOfMonth + " / " + month + " / "+year);
                }
            }
        };


        spinnerRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        textViewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlarms();
            }
        });

        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(AddEvent.this, MapsActivity.class);
                mapIntent.putExtra("Longitude", longitude);
                mapIntent.putExtra("Latitude", latitude);
                startActivityForResult(mapIntent, REQUEST_CODE_LOCATION);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarms();
                setAlarms();
                saveEvent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_LOCATION && resultCode == RESULT_OK){
            longitude = data.getDoubleExtra("Longitude", 10000);
            latitude = data.getDoubleExtra("Latitude", 100000);
            if(!(data.getDoubleExtra("Longitude", 10000) == 10000)){
                textViewLocation.setText("Location Selected");
            }
        }
    }

    private void showDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddEvent.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                year,month,day);
        datePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(start_time_bool){
            start_time_bool = false;
            textViewStartTime.setText(hourOfDay + " : " + minute);
        }
        else if(end_time_bool){
            end_time_bool = false;
            textViewEndTime.setText(hourOfDay + " : " +minute);
        }
    }

    public void createAlarms(){
        Intent intent = new Intent(AddEvent.this, CreateAlarm.class);
        intent.putExtra("StartDate", getDay(textViewStartDate, textViewStartTime).getTimeInMillis());
        startActivity(intent);
    }

    public void cancelAlarms(){
        for (Alarm alarm : willCancelAlarm){
            AlarmFeatures.cancelAlarm(AddEvent.this ,alarm.getCode());
        }
    }

    public void setAlarms(){
        alarms = SharedPref.getAlarmList(this);             // Ayarlanan alarmları alıyoruz.
        String title = editTextTitle.getText().toString();
        String content = editTextNotes.getText().toString();
        for(Alarm alarm : alarms){
            AlarmFeatures.startAlarm(AddEvent.this, alarm.getTime(), alarm.getCode(), title, content);
        }
    }

    public void saveEvent(){
        Calendar cal = getDay(textViewStartDate, textViewStartTime);
        Calendar cal2 = getDay(textViewEndDate, textViewEndTime);

        String notes = editTextNotes.getText().toString().trim();
        int repeatId = spinnerRepeat.getSelectedItemPosition();

        int repeatCode = SharedPref.getCode(AddEvent.this);

        Event event = new Event(editTextTitle.getText().toString(), cal , cal2, alarms, longitude, latitude, repeatId ,repeatCode , notes);
        ArrayList<Event> events = SharedPref.loadEventList(AddEvent.this);
        events.add(event);
        SharedPref.saveEventList(AddEvent.this, events);

        RepeatEventFeatures.startRepeat(AddEvent.this, event.getEndDate(), repeatId, repeatCode);
        Toast.makeText(AddEvent.this, "Event is created successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    public Calendar getDay(TextView date, TextView time){
        int startDay =Integer.parseInt(date.getText().toString().split("/")[0].trim());
        int startMonth =Integer.parseInt(date.getText().toString().split("/")[1].trim());
        int startYear =Integer.parseInt(date.getText().toString().split("/")[2].trim());
        int startHour =Integer.parseInt(time.getText().toString().split(":")[0].trim());
        int startMinute =Integer.parseInt(time.getText().toString().split(":")[1].trim());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, startYear);
        cal.set(Calendar.MONTH, startMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, startDay);                                      //Calendar
        cal.set(Calendar.HOUR_OF_DAY, startHour);
        cal.set(Calendar.MINUTE, startMinute);
        cal.set(Calendar.SECOND, 0);

        return cal;
    }


}
