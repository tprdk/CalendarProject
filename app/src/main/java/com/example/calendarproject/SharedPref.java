package com.example.calendarproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class SharedPref {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static Gson gson;
    private static String json;
    private static final String sharedPref = "SharedPref";
    private static final String eventKey = "Events";

    private static SharedPreferences preferencesAlarm;
    private static SharedPreferences.Editor editorAlarm;
    private static Gson gsonAlarm;
    private static String jsonAlarm;
    private static final String sharedPrefAlarm = "SharedPrefAlarm";
    private static final String eventKeyAlarm = "AlarmTimes";

    private static SharedPreferences preferencesCode;
    private static SharedPreferences.Editor editorCode;
    private static final String sharedPrefCode = "SharedPrefCode";
    private static final String eventKeyCode = "Code";

    private static SharedPreferences preferencesDefault;
    private static SharedPreferences.Editor editorDefault;
    private static Gson gsonDefault;
    private static String jsonDefault;
    private static final String sharedPrefDefault = "SharedPrefDefault";
    private static final String eventKeyDefault = "Default";

    private static SharedPreferences preferencesCalendarDay;
    private static SharedPreferences.Editor editorCalendarDay;
    private static Gson gsonCalendarDay;
    private static String jsonCalendarDay;
    private static final String sharedPrefCalendarDay = "SharedPrefCalendarDay";
    private static final String eventKeyCalendarDay = "CalendarDay";


    public static ArrayList<Event> loadEventList(Context context){
        preferences = context.getSharedPreferences(sharedPref, MODE_PRIVATE);
        gson = new Gson();
        json = preferences.getString(eventKey, null);
        Type type = new TypeToken<ArrayList<Event>>(){}.getType();
        ArrayList<Event> list = gson.fromJson(json, type);
        if (list == null)
            list = new ArrayList<Event>();
        return list;
    }

    public static void saveEventList(Context context, ArrayList<Event> list){
        preferences = context.getSharedPreferences(sharedPref, MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        json = gson.toJson(list);
        editor.putString(eventKey, json);
        editor.apply();
    }


    public static void updateEvent(Context context, ArrayList<Event> list, int index, Event event){
        list.set(index, event);
        saveEventList(context, list);
    }

    public static void deleteEvent(Context context, ArrayList<Event> list, int index){
        list.remove(list.get(index));
        saveEventList(context, list);
    }

    public static Event getEvent(Context context, int index){
        preferences = context.getSharedPreferences(sharedPref, MODE_PRIVATE);
        gson = new Gson();
        json = preferences.getString(eventKey, null);
        Type type = new TypeToken<ArrayList<Event>>(){}.getType();
        ArrayList<Event> list = gson.fromJson(json, type);
        return list.get(index);
    }


    public static Event getEventwithCode(Context context, int repeatCode){
        preferences = context.getSharedPreferences(sharedPref, MODE_PRIVATE);
        gson = new Gson();
        json = preferences.getString(eventKey, null);
        Type type = new TypeToken<ArrayList<Event>>(){}.getType();
        ArrayList<Event> list = gson.fromJson(json, type);
        Event eventRet = null;
        for(Event event : list){
            if(repeatCode == event.getRepeatCode()){
                eventRet = event;
            }
        }
        return eventRet;
    }

    public static ArrayList<Alarm> getAlarmList(Context context){
        preferencesAlarm = context.getSharedPreferences(sharedPrefAlarm, MODE_PRIVATE);
        gsonAlarm = new Gson();
        jsonAlarm = preferencesAlarm.getString(eventKeyAlarm, null);
        Type type = new TypeToken<ArrayList<Alarm>>(){}.getType();

        ArrayList<Alarm> list = gsonAlarm.fromJson(jsonAlarm, type);
        if (list == null)
            list = new ArrayList<Alarm>();
        return list;
    }

    public static void saveAlarmTimes(Context context, ArrayList<Alarm> list){
        preferencesAlarm = context.getSharedPreferences(sharedPrefAlarm, MODE_PRIVATE);
        editorAlarm = preferencesAlarm.edit();
        gsonAlarm = new Gson();
        jsonAlarm = gsonAlarm.toJson(list);
        editorAlarm.putString(eventKeyAlarm, jsonAlarm);
        editorAlarm.apply();
    }

    public static int getCode(Context context){
        preferencesCode = context.getSharedPreferences(sharedPrefCode, MODE_PRIVATE);
        int myIntValue = preferencesCode.getInt(eventKeyCode, 0);
        editorCode = preferencesCode.edit();
        editorCode.putInt(eventKeyCode, myIntValue + 1);
        editorCode.commit();
        return myIntValue;
    }

    public static Defaults getDefaults(Context context){
        preferencesDefault = context.getSharedPreferences(sharedPrefDefault, MODE_PRIVATE);
        gsonDefault = new Gson();
        jsonDefault = preferencesDefault.getString(eventKeyDefault, null);
        Type type = new TypeToken<Defaults>(){}.getType();

        Defaults defaults = gsonDefault.fromJson(jsonDefault, type);
        if (defaults == null)
        {
            defaults = new Defaults(0, 0, 0);
        }
        return defaults;
    }

    public static void saveDefaults(Context context, Defaults defaults){
        preferencesDefault = context.getSharedPreferences(sharedPrefDefault, MODE_PRIVATE);
        editorDefault = preferencesDefault.edit();
        gsonDefault = new Gson();
        jsonDefault = gsonDefault.toJson(defaults);
        editorDefault.putString(eventKeyDefault, jsonDefault);
        editorDefault.apply();
    }

    public static void addWillDeleteDecorate(Context context, CalendarDay calendarDay){
        ArrayList<CalendarDay> calendarDayList = getWillDeleteDecorate(context);
        calendarDayList.add(calendarDay);
        saveWillDeleteDecorate(context, calendarDayList);
    }

    public static void saveWillDeleteDecorate(Context context, ArrayList<CalendarDay> calendarDayList){
        preferencesCalendarDay = context.getSharedPreferences(sharedPrefCalendarDay, MODE_PRIVATE);
        editorCalendarDay = preferencesCalendarDay.edit();
        gsonCalendarDay = new Gson();
        jsonCalendarDay = gsonCalendarDay.toJson(calendarDayList);
        editorCalendarDay.putString(eventKeyCalendarDay, jsonCalendarDay);
        editorCalendarDay.apply();
    }

    public static ArrayList<CalendarDay> getWillDeleteDecorate(Context context){
        preferencesCalendarDay = context.getSharedPreferences(sharedPrefCalendarDay, MODE_PRIVATE);
        gsonCalendarDay = new Gson();
        jsonCalendarDay = preferencesCalendarDay.getString(eventKeyCalendarDay, null);
        Type type = new TypeToken<ArrayList<CalendarDay>>(){}.getType();

        ArrayList<CalendarDay> calendarDayList = gsonCalendarDay.fromJson(jsonCalendarDay, type);
        if (calendarDayList == null)
            calendarDayList = new ArrayList<CalendarDay>();

        return calendarDayList;
    }

    public static void deleteEvents(Context context){
        context.getSharedPreferences(sharedPref, 0).edit().clear().commit();
    }

    public static void deleteAlarmTimes(Context context){
        context.getSharedPreferences(sharedPrefAlarm, 0).edit().clear().commit();
    }
}

