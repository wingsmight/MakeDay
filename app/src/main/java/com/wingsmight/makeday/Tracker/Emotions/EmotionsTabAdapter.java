package com.wingsmight.makeday.Tracker.Emotions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wingsmight.makeday.R;
import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingDay;

import java.util.ArrayList;

public class EmotionsTabAdapter extends RecyclerView.Adapter<EmotionsTabAdapter.TimeTrackingDayViewHolder>
{
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<TimeTrackingDay<EmotionEvent>> timeTrackingDays;


    public EmotionsTabAdapter(ArrayList<TimeTrackingDay<EmotionEvent>> timeTrackingDays)
    {
        this.timeTrackingDays = timeTrackingDays;
    }

    @NonNull
    @Override
    public TimeTrackingDayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_time_tracking_day_, viewGroup, false);
        TimeTrackingDayViewHolder viewHolder = new TimeTrackingDayViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeTrackingDayViewHolder viewHolder, int i)
    {
        final TimeTrackingDay<EmotionEvent> timeTrackingDay = timeTrackingDays.get(i);
        viewHolder.eventsDate.setText(timeTrackingDay.getDayOfWeek() + ", " + timeTrackingDay.getDay() + " " + timeTrackingDay.getMonth());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                viewHolder.eventsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(timeTrackingDay.getTimeIntervals().size());

        // Create sub item view adapter
        EmotionEventAdapter subItemAdapter = new EmotionEventAdapter(timeTrackingDay.getTimeIntervals());

        viewHolder.eventsRecyclerView.setLayoutManager(layoutManager);
        viewHolder.eventsRecyclerView.setAdapter(subItemAdapter);
        viewHolder.eventsRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        if(timeTrackingDays == null)
        {
            return 0;
        }
        else
        {
            return timeTrackingDays.size();
        }
    }

    public class TimeTrackingDayViewHolder extends  RecyclerView.ViewHolder
    {
        private TextView eventsDate;
        private RecyclerView eventsRecyclerView;

        public TimeTrackingDayViewHolder(@NonNull View itemView)
        {
            super(itemView);

            eventsDate = itemView.findViewById(R.id.eventsDate);
            eventsRecyclerView = itemView.findViewById(R.id.eventsRecyclerView);
        }
    }
}
