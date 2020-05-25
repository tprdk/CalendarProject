package com.example.calendarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class CreateAlarm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private ListView listView;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    private int alarmDay, alarmMonth, alarmYear;
    private String date;

    private ArrayList<String> alarmsStr;
    private ArrayList<Alarm> alarms;
    private ArrayAdapter arrayAdapter;

    private boolean flag = true;
    private boolean def = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        initView();
        setListeners();
    }

    public void initView(){

        int def_year = 0, def_month = 0, def_day = 0, def_hour = 0, def_minute = 0;
        String defaultAlarm = "";

        Defaults defaults = SharedPref.getDefaults(CreateAlarm.this);
        Calendar cal ;
        alarms = SharedPref.getAlarmList(CreateAlarm.this);
        alarmsStr = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();

        if(getIntent().getLongExtra("StartDate", -1) != -1){

            calendar.setTimeInMillis(getIntent().getLongExtra("StartDate", -1));

            Log.d("Date", "ıd = " +  defaults.getDefaultAlarmId());
            switch (defaults.getDefaultAlarmId()){
                case 0:
                    calendar.add(Calendar.DAY_OF_YEAR , -1);
                    break;
                case 1:
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    break;
                case 2:
                    calendar.add(Calendar.MINUTE, -30);
                    break;
                case 3:
                    calendar.add(Calendar.MINUTE, -15);
                    break;
            }

            def_year = calendar.get(Calendar.YEAR);
            def_month = calendar.get(Calendar.MONTH) + 1;
            def_day = calendar.get(Calendar.DAY_OF_MONTH);
            def_hour = calendar.get(Calendar.HOUR_OF_DAY);
            def_minute = calendar.get(Calendar.MINUTE);

            defaultAlarm = def_day + " / " + def_month + " / " + def_year + " - " + def_hour +" : " + def_minute;
            Log.d("Date", defaultAlarm);
        }

        for(Alarm alarm : alarms){
            cal = alarm.getTime();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            alarmsStr.add(day + " / " + month + " / " + year + " - " + hour +" : " + minute);
            if(def_year == year && def_month == month && def_day == day && def_hour == hour && def_minute == minute)
                def = false;
        }
        if(def && !defaultAlarm.equals("")){
            alarmsStr.add(defaultAlarm);
        }
        alarms.clear();
        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, alarmsStr);
        listView.setAdapter(arrayAdapter);
    }

    public void setListeners(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                deleteAlarm(position);
                return false;
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                date = dayOfMonth + " / " + month + " / " + year ;
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "Time Picker");
            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_alert, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item_add){
            showDatePicker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateAlarm.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                year,month,day);
        datePickerDialog.show();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        date += " - " + hourOfDay + " : " + minute;
        alarmsStr.add(date);
        arrayAdapter.notifyDataSetChanged();
    }

    private void deleteAlarm(final int position){
        new AlertDialog.Builder(CreateAlarm.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are You Sure?")
                .setMessage("Do you want to delete this alert?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CreateAlarm.this, "Şu" +getIntent().getIntExtra("Position", 0)
                                + " Silindi",Toast.LENGTH_SHORT).show();
                        alarmsStr.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }

    public int genereteCode(){
        int code =  SharedPref.getCode(CreateAlarm.this);
        return code;
    }

    public void recreateAlarms(){
        for (String str : alarmsStr){
            fillAlarms(str);
        }
    }

    public void fillAlarms(String str){
        String date = str.split("-")[0].trim();
        String time = str.split("-")[1].trim();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(date.split("/")[2].trim()));
        cal.set(Calendar.MONTH, Integer.parseInt(date.split("/")[1].trim()) - 1 );
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("/")[0].trim()));          //Calendar
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0].trim()));
        cal.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1].trim()));
        cal.set(Calendar.SECOND, 0);

        Alarm alarm = new Alarm(cal, genereteCode());
        alarms.add(alarm);
    }

    @Override
    protected void onStop() {
        super.onStop();
        save();
    }

    public void save(){
        while(flag) {
            recreateAlarms();
            SharedPref.saveAlarmTimes(CreateAlarm.this, alarms);
            Log.d("Mesaj" , "saved");
            Log.d("Mesaj" , "size = " + SharedPref.getAlarmList(CreateAlarm.this).size());
            flag = false;
        }
        Toast.makeText(CreateAlarm.this ,"Data saved", Toast.LENGTH_LONG).show();
    }

}
