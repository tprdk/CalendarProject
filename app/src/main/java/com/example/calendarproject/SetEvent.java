package com.example.calendarproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.util.ArrayList;
import java.util.Calendar;



public class SetEvent extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private Spinner spinnerRepeat;
    private TextView textViewStartDate, textViewEndDate, textViewStartTime, textViewEndTime, textViewLocation, textViewAlarm;
    private EditText editTextTitle, editTextNotes;
    private Button buttonSave;

    private ArrayAdapter<CharSequence> repeatAdapter;
    private ArrayList<Alarm> alarms;
    private ArrayList<Alarm> willCancelAlarm;
    private ArrayList<Event> events;
    private Event event;

    private int willCancelRepeatCode;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private int index ;
    private int repeatCode;
    private boolean start_time_bool = false;
    private boolean end_time_bool = false;
    private boolean start_date_bool = false;
    private boolean end_date_bool = false;

    private double longitude;
    private double latitude;
    private static final int REQUEST_CODE_LOCATION = 0;

    private ShareActionProvider  myShareActionProvider;
    public String activityText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initView();
        initFields();
        setListeners();
    }

    private void initView(){
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
        spinnerRepeat.setSelection(2);

        buttonSave = findViewById(R.id.button_save);

        index = getIntent().getIntExtra("Position", 0);
        events = SharedPref.loadEventList(SetEvent.this);
        event = SharedPref.getEvent(SetEvent.this,index);
        alarms = event.getAlarms();

        willCancelAlarm = new ArrayList<>(alarms);
        willCancelRepeatCode = event.getRepeatCode();
    }

    private void initFields(){
        Calendar calendar = event.getStartDate();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        int startHour = calendar.get(Calendar.HOUR_OF_DAY);
        int startMinute = calendar.get(Calendar.MINUTE);

        calendar = event.getEndDate();
        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        int endHour = calendar.get(Calendar.HOUR_OF_DAY);
        int endMinute = calendar.get(Calendar.MINUTE);

        longitude = event.getLongitude();
        latitude = event.getLatitude();
        editTextNotes.setText(event.getNotes());

        repeatCode = event.getRepeatCode();

        textViewStartDate.setText(startDay + " / " + (startMonth + 1) + " / " + startYear);
        textViewEndDate.setText(endDay + " / " + (endMonth + 1) + " / " + endYear);
        textViewStartTime.setText(startHour + " : " + startMinute);
        textViewEndTime.setText(endHour + " : " + endMinute);
        editTextTitle.setText(event.getTitle());
        textViewLocation.setText(event.getAdress());
        spinnerRepeat.setSelection(event.getRepeatId());

        activityText += "Title : " + event.getTitle();
        activityText += "\nNotes : " + event.getNotes();
        activityText += "\nStart Date : " + startDay + " / " + (startMonth + 1) + " / " + startYear ;
        activityText += "\nStart Time : " + startHour + " : " + startMinute ;
        activityText += "\nEnd Date : " + endDay + " / " + (endMonth + 1) + " / " + endYear ;
        activityText += "\nEnd Date : " + endHour + " : " + endMinute ;
        activityText += "\nLocation : " + event.getAdress();
        if(event.getLatitude() != 0 || event.getLongitude() != 0)
            activityText += "\nFor opening on the Maps : " + "http://maps.google.com/maps?saddr=" + event.getLatitude() + "," + event.getLongitude();
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
                Intent mapIntent = new Intent(SetEvent.this, MapsActivity.class);
                mapIntent.putExtra("Longitude", longitude);
                mapIntent.putExtra("Latitude", latitude);
                startActivityForResult(mapIntent, REQUEST_CODE_LOCATION);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields()){
                    updateEvent();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_LOCATION && resultCode == RESULT_OK){
            longitude = data.getDoubleExtra("Longitude", 10000);
            latitude = data.getDoubleExtra("Latitude", 100000);
            if(!(data.getDoubleExtra("Longitude", 10000) == 10000)){
                textViewLocation.setText(data.getStringExtra("Location"));
            }else{
                textViewLocation.setText("No Location");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.set_event, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        myShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (myShareActionProvider != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, activityText);
            shareIntent.setType("text/plain");
            myShareActionProvider.setShareIntent(shareIntent);
        }

        return true;
    }

    private void showDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SetEvent.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                year,month,day);
        datePickerDialog.show();
    }


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

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_delete){
            deleteEvent(index);
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteEvent(final int index){
        new AlertDialog.Builder(SetEvent.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are You Sure?")
                .setMessage("Do you want to delete this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPref.deleteEvent(SetEvent.this, events, index);
                        Toast.makeText(SetEvent.this, "Event deleted successfully.", Toast.LENGTH_SHORT).show();
                        CalendarDay calendarDay = CalendarDay.today();
                        SharedPref.addWillDeleteDecorate(SetEvent.this,calendarDay.from(event.getStartDate()));
                        finish();
                    }

                })
                .setNegativeButton("No",null)
                .show();
    }

    public void updateEvent(){
        new AlertDialog.Builder(SetEvent.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are You Sure?")
                .setMessage("Do you want to update this event?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData();
                    }

                })
                .setNegativeButton("No",null)
                .show();
    }

    public void updateData(){
        cancelAlarms();
        setAlarms();
        saveEvent();
    }

    public void createAlarms(){
        Intent intent = new Intent(SetEvent.this, CreateAlarm.class);
        startActivity(intent);
    }

    public void cancelAlarms(){
        for (Alarm alarm : willCancelAlarm){
            AlarmFeatures.cancelAlarm(SetEvent.this, alarm.getCode());
        }
    }

    public void setAlarms(){
        alarms.clear();
        alarms = SharedPref.getAlarmList(this);             // Ayarlanan alarmları alıyoruz.
        String title = editTextTitle.getText().toString();
        String content = editTextNotes.getText().toString();
        for(Alarm alarm : alarms){
            AlarmFeatures.startAlarm(SetEvent.this, alarm.getTime(), alarm.getCode(), title, content);
        }
    }

    public void saveEvent(){

        Calendar cal = getDay(textViewStartDate, textViewStartTime);
        Calendar cal2 = getDay(textViewEndDate, textViewEndTime);

        String notes = editTextNotes.getText().toString().trim();
        int repeatId = spinnerRepeat.getSelectedItemPosition();

        String location = textViewLocation.getText().toString().trim();

        Event event = new Event(editTextTitle.getText().toString(), cal , cal2, alarms, longitude, latitude, repeatId ,repeatCode , notes, location);
        ArrayList<Event> events = SharedPref.loadEventList(SetEvent.this);
        events.set(index, event);
        SharedPref.saveEventList(SetEvent.this, events);

        RepeatEventFeatures.cancelRepeat(SetEvent.this, willCancelRepeatCode);
        RepeatEventFeatures.startRepeat(SetEvent.this, event.getEndDate(), repeatId, event.getRepeatCode());
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

        return cal;
    }

    public boolean checkFields(){
        boolean flag = true;
        if(editTextTitle.getText().toString().trim().equals("") && flag){
            Toast.makeText(this, "Title can not be empty.", Toast.LENGTH_SHORT).show();
            flag = false;
        }if(textViewStartDate.getText().toString().trim().equals(getString(R.string.add_event_start_date)) && flag){
            Toast.makeText(this, "Start date must be selected.", Toast.LENGTH_SHORT).show();
            flag = false;
        }if(textViewEndDate.getText().toString().trim().equals(getString(R.string.add_event_end_date)) && flag){
            Toast.makeText(this, "End date must be selected.", Toast.LENGTH_SHORT).show();
            flag = false;
        }if(textViewStartTime.getText().toString().trim().equals(getString(R.string.add_event_start_time)) && flag){
            Toast.makeText(this, "Start time must be selected.", Toast.LENGTH_SHORT).show();
            flag = false;
        }if(textViewEndTime.getText().toString().trim().equals(getString(R.string.add_event_end_time)) && flag){
            Toast.makeText(this, "End time must be selected.", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }
}
