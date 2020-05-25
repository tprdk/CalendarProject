package com.example.calendarproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class Settings extends AppCompatActivity {

    public static final int REQUEST_CODE_ALARM_SOUND = 1;
    public static final int REQUEST_CODE_WRITE_SETTINGS = 2;
    public static String chosenRingtone;
    public static Ringtone ringtone = null;

    private Button save;
    private TextView alarmSound;
    private Spinner spinnerTheme, spinnerRepeat, spinnerAlarm;
    private ArrayAdapter<CharSequence> themeAdapter, repeatAdapter, alarmAdapter;
    private Defaults defaults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        setListeners();
        permission();

    }

    public void permission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                !android.provider.Settings.System.canWrite(this)) {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getApplication().getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
        }
    }

    public void initView(){
        defaults = SharedPref.getDefaults(Settings.this);
        alarmSound = findViewById(R.id.textView_default_sound);

        spinnerTheme = findViewById(R.id.spinner_default_theme);
        themeAdapter = ArrayAdapter.createFromResource(this, R.array.theme, android.R.layout.simple_spinner_item);
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTheme.setAdapter(themeAdapter);
        spinnerTheme.setSelection(defaults.getDefaultThemeId());

        spinnerAlarm = findViewById(R.id.spinner_default_alarm);
        alarmAdapter = ArrayAdapter.createFromResource(this, R.array.alarm, android.R.layout.simple_spinner_item);
        alarmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlarm.setAdapter(alarmAdapter);
        spinnerAlarm.setSelection(defaults.getDefaultAlarmId());

        spinnerRepeat = findViewById(R.id.spinner_default_repeat);
        repeatAdapter = ArrayAdapter.createFromResource(this, R.array.repeat_options, android.R.layout.simple_spinner_item);
        repeatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRepeat.setAdapter(repeatAdapter);
        spinnerRepeat.setSelection(defaults.getDefaultRepeatId());

        save = findViewById(R.id.button_save_settings);
    }

    public void setListeners(){

        alarmSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent, REQUEST_CODE_ALARM_SOUND);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ALARM_SOUND)
        {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

            if (uri != null)
            {
                ringtone = RingtoneManager.getRingtone(Settings.this, uri);
                String title = ringtone.getTitle(this);
                Toast.makeText(Settings.this,"Default Notification Sound is setted to " + title, Toast.LENGTH_LONG).show();
                RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION, uri);
            }
            else
            {
                chosenRingtone = null;
            }
        }
        else if(requestCode == REQUEST_CODE_WRITE_SETTINGS && resultCode == RESULT_OK){
            Log.d("Write","Permission Okey");
        }
    }

    protected void saveData() {
        Defaults defaultsNew = new Defaults(spinnerTheme.getSelectedItemPosition(),
                spinnerAlarm.getSelectedItemPosition(), spinnerRepeat.getSelectedItemPosition());
        SharedPref.saveDefaults(Settings.this, defaultsNew);
        if(spinnerTheme.getSelectedItemPosition() != defaults.getDefaultThemeId()){
            if(spinnerTheme.getSelectedItemPosition() == 0){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                startActivity(new Intent(Settings.this, Settings.class));
                finish();
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                startActivity(new Intent(Settings.this, Settings.class));
                finish();
            }
        }
        Toast.makeText(Settings.this, "All changes saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
