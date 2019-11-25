package com.wingsmight.makeday.Tracker.TimeTracking;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wingsmight.makeday.Tracker.EventAdapter;
import com.wingsmight.makeday.R;

import java.util.ArrayList;

public class TimeTrackingTabAdapter extends RecyclerView.Adapter<TimeTrackingTabAdapter.TimeTrackingDayViewHolder>
{
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private ArrayList<TimeTrackingDay> timeTrackingDays;


    public TimeTrackingTabAdapter(ArrayList<TimeTrackingDay> timeTrackingDays)
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
        final TimeTrackingDay timeTrackingDay = timeTrackingDays.get(i);
        viewHolder.eventsDate.setText(timeTrackingDay.getDay() + " " + timeTrackingDay.getMonth());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                viewHolder.eventsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(timeTrackingDay.getTimeIntervals().size());

        // Create sub item view adapter
        EventAdapter subItemAdapter = new EventAdapter(timeTrackingDay.getTimeIntervals());

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
