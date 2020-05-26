package com.example.calendarproject;

import android.location.Address;

import java.util.ArrayList;
import java.util.Calendar;

public class Event {

    private String title;
    private Calendar startDate;
    private Calendar endDate;
    private ArrayList<Alarm> alarms;
    private double longitude;
    private double latitude;
    private int repeatId;
    private int repeatCode;
    private String notes;
    private String adress;


    public Event(String title, Calendar startDate, Calendar endDate, ArrayList<Alarm> alarms,
                    double longitude, double latitude, int repeatId, int repeatCode,String notes, String address) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.alarms = alarms;
        this.latitude = latitude;
        this.longitude = longitude;
        this.repeatId = repeatId;
        this.notes = notes;
        this.repeatCode = repeatCode;
        this.adress = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(int repeatId) {
        this.repeatId = repeatId;
    }

    public int getRepeatCode() {
        return repeatCode;
    }

    public void setRepeatCode(int repeatCode) {
        this.repeatCode = repeatCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAdress() { return adress; }

    public void setAdress(String adress) { this.adress = adress; }
}
