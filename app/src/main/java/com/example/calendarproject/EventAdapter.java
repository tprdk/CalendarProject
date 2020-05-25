package com.example.calendarproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<Event> eventList;
    private OnItemClickListener eventListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        eventListener = listener;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView title, start_date, end_date;
        public EventViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            title = itemView.findViewById(R.id.text_activity_name);
            start_date = itemView.findViewById(R.id.text_activity_date);
            end_date = itemView.findViewById(R.id.text_activity_hours);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                           listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public EventAdapter (ArrayList<Event> list){
        eventList = list;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list, parent, false);
        EventViewHolder yearlyViewHolder = new EventViewHolder(view, eventListener);
        return yearlyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        Calendar cal = event.getStartDate();
        int start_year = cal.get(Calendar.YEAR);
        int start_month = cal.get(Calendar.MONTH) + 1;
        int start_day = cal.get(Calendar.DAY_OF_MONTH);
        int start_hour = cal.get(Calendar.HOUR_OF_DAY);
        int start_minute = cal.get(Calendar.MINUTE);

        cal = event.getEndDate();
        int end_year = cal.get(Calendar.YEAR);
        int end_month = cal.get(Calendar.MONTH) + 1;
        int end_day = cal.get(Calendar.DAY_OF_MONTH);
        int end_hour = cal.get(Calendar.HOUR_OF_DAY);
        int end_minute = cal.get(Calendar.MINUTE);

        holder.title.setText(event.getTitle());
        holder.start_date.setText(start_day + " / " + start_month + " / " + start_year + " - " + start_hour +" : " + start_minute);
        holder.end_date.setText(end_day + " / " + end_month + " / " + end_year + " - " + end_hour +" : " + end_minute);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

}

