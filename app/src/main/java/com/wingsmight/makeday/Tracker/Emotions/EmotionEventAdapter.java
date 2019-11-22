package com.wingsmight.makeday.Tracker.Emotions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wingsmight.makeday.R;

import java.util.ArrayList;

public class EmotionEventAdapter extends RecyclerView.Adapter<EmotionEventAdapter.EventViewHolder>
{
    private ArrayList<EmotionEvent> events;

    public EmotionEventAdapter(ArrayList<EmotionEvent> events)
    {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_event, viewGroup, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder viewHolder, int i)
    {
        final EmotionEvent event = events.get(i);

        viewHolder.eventInterval.setText(event.getTimeInterval24());
        viewHolder.eventDescription.setText(event.getEmotion());
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
