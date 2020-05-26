package com.example.calendarproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;

public class DecorateEvent implements DayViewDecorator {

    private final ArrayList<CalendarDay> dates;
    private final Context context;
    private final boolean BOOL_OPT;
    public static final boolean BOOL_DRAW = true;
    public static final boolean BOOL_CLEAR = false;

    public DecorateEvent(Context context, ArrayList<CalendarDay> dates, boolean BOOL_OPT) {
        this.dates = dates;
        this.context = context;
        this.BOOL_OPT = BOOL_OPT;
}

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        //view.addSpan(new DotSpan(5, Color.BLACK));
        Drawable drawable;
        if(BOOL_OPT){
            drawable = ContextCompat.getDrawable(context,R.drawable.circle_selector);
        }else{
            drawable = ContextCompat.getDrawable(context,R.drawable.clear_selector);
        }
        assert drawable != null;
        view.setBackgroundDrawable(drawable);
    }
}

