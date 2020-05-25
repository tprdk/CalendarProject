package com.example.calendarproject;

public class Defaults {

    private int defaultThemeId;
    private int defaultAlarmId;
    private int defaultRepeatId;

    public Defaults(int defaultThemeId, int defaultAlarmId, int defaultRepeatId) {
        this.defaultThemeId = defaultThemeId;
        this.defaultAlarmId = defaultAlarmId;
        this.defaultRepeatId = defaultRepeatId;
    }

    public int getDefaultThemeId() {
        return defaultThemeId;
    }

    public void setDefaultThemeId(int defaultThemeId) {
        this.defaultThemeId = defaultThemeId;
    }

    public int getDefaultAlarmId() {
        return defaultAlarmId;
    }

    public void setDefaultAlarmId(int defaultAlarmId) {
        this.defaultAlarmId = defaultAlarmId;
    }

    public int getDefaultRepeatId() {
        return defaultRepeatId;
    }

    public void setDefaultRepeatId(int defaultRepeatId) {
        this.defaultRepeatId = defaultRepeatId;
    }
}
