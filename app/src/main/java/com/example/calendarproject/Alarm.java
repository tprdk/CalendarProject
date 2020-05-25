package com.example.calendarproject;

import java.time.LocalTime;
import java.util.Calendar;

public class Alarm {
    private Calendar time;
    private int code;

    public Alarm(Calendar time, int code){
        this.time = time;
        this.code = code;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
