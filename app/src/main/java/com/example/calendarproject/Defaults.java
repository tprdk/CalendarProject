package com.example.calendarproject;

public class Defaults {

    private int defaultThemeId;
    private int defaultAlarmId;
    private int defaultRepeatId;
    private boolean hasAlertBox;

    public Defaults(int defaultThemeId, int defaultAlarmId, int defaultRepeatId, boolean hasAlertBox) {
        this.defaultThemeId = defaultThemeId;
        this.defaultAlarmId = defaultAlarmId;
        this.defaultRepeatId = defaultRepeatId;
        this.hasAlertBox = hasAlertBox;
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

    public boolean getHasAlertBox() { return hasAlertBox; }

    public void setHasAlertBox(boolean hasAlertBox) {
        this.hasAlertBox = hasAlertBox;
    }
}
