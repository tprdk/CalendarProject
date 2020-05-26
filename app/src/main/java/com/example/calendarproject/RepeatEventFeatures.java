package com.example.calendarproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class RepeatEventFeatures {

    public static void startRepeat(Context context, Calendar calendar, int repeatPattern, int repeatCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RepeatReceiver.class);
        intent.putExtra("RepeatPattern", repeatPattern);
        intent.putExtra("RepeatCode", repeatCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, repeatCode, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void cancelRepeat(Context context, int repeatCode){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RepeatReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, repeatCode, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
