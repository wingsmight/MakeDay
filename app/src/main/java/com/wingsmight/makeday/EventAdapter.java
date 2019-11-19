package com.wingsmight.makeday;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
{
    private ArrayList<Event> events;

    public EventAdapter(ArrayList<Event> events)
    {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_event, viewGroup, false);
        EventViewHolder viewHolder = new EventViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder viewHolder, int i)
    {
        final Event event = events.get(i);

        String timeInterval = event.getHoursBefore() + ":" + event.getMinutesBefore() + " - " + event.getHoursAfter()+ ":" + event.getMinutesAfter();
        viewHolder.eventInterval.setText(timeInterval);
        viewHolder.eventDescription.setText(event.getEventText());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends  RecyclerView.ViewHolder
    {
        TextView eventInterval, eventDescription;

        public EventViewHolder(@NonNull View itemView)
        {
            super(itemView);

            eventInterval = itemView.findViewById(R.id.eventInterval);
            eventDescription = itemView.findViewById(R.id.eventDescription);
        }
    }
}
