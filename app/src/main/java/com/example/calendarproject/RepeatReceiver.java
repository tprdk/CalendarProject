package com.example.calendarproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class RepeatReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int repeatId = intent.getIntExtra("RepeatPattern", 0);
        int repeatCode = intent.getIntExtra("RepeatCode", -1);

        Log.d("Event" , "onReceive repeatPattern = " + repeatId );
        Log.d("Event" , "onReceive repeatCode = " + repeatCode );
        Calendar calendar;
        Event event;


        switch (repeatId){
            case 0:          // not repeating
                break;
            case 1:
                event = SharedPref.getEventwithCode(context,repeatCode);        //Günde 1
                updateEvent(context, event, Calendar.DAY_OF_YEAR);
                break;
            case 2:
                event = SharedPref.getEventwithCode(context,repeatCode);        //haftada 1
                updateEvent(context, event, Calendar.WEEK_OF_YEAR);
                break;
            case 3:
                //ayda 1
                event = SharedPref.getEventwithCode(context,repeatCode);           //ayda 1
                updateEvent(context, event, Calendar.MONTH);
                break;
            case 4:
                //yılda 1
                event = SharedPref.getEventwithCode(context,repeatCode);        //yılda 1
                updateEvent(context, event, Calendar.YEAR);
                break;
        }
    }

    public void updateEvent(Context context, Event event, int amount){

        ArrayList<Event> events = SharedPref.loadEventList(context);
        Calendar start = event.getStartDate();
        start.add(amount, 1);
        Calendar end = event.getEndDate();
        end.add(amount, 1);

        Log.d("Event" , "update amount = " + amount );

        Calendar c;
        ArrayList<Alarm> alarms = event.getAlarms();
        for(Alarm alarm : alarms){
            c = alarm.getTime();
            c.add(amount, 1);
            alarm.setTime(c);
        }
        Event newEvent = new Event(event.getTitle(), start, end, alarms, event.getLongitude(), event.getLatitude()
                                    ,event.getRepeatId(), event.getRepeatCode(), event.getNotes());

        String title = newEvent.getTitle();
        String content = newEvent.getNotes();
        for(Alarm alarm : alarms){
            AlarmFeatures.startAlarm(context, alarm.getTime(), alarm.getCode(), title, content);
        }

        for (Event e : events){
            Log.d("Event", e.getRepeatId() + " Before size = " + events.size());
        }
        int index = 0;
        boolean flag = false;
        for (Event e : events){
            if(e.getRepeatCode() == event.getRepeatCode()){
                index = events.indexOf(e);
                flag = true;
            }
        }
        if(flag){
            events.remove(index);
        }

        events.add(newEvent);
        for (Event e : events){
            Log.d("Event", e.getRepeatId() + " Added size = " + events.size());
        }
        SharedPref.saveEventList(context, events);
    }
}
