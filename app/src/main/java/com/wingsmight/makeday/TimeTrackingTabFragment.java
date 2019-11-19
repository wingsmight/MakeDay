package com.wingsmight.makeday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TimeTrackingTabFragment extends Fragment
{
    RecyclerView recyclerView;
    ArrayList<TimeTrackingDay> TimeTrackingDays = new ArrayList<>();
    TimeTrackingTabAdapter timeTrackingTabAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_time_tracking_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.timeTrackerRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        TimeTrackingDays = buildItemList();
        timeTrackingTabAdapter = new TimeTrackingTabAdapter(TimeTrackingDays);
        recyclerView.setAdapter(timeTrackingTabAdapter);
    }

    private ArrayList<TimeTrackingDay> buildItemList() {
        ArrayList<TimeTrackingDay> itemList = new ArrayList<>();
        for (int i=1; i<10; i++) {
            TimeTrackingDay<Event> item = new TimeTrackingDay<Event>(i, "ноябрь", 2019, buildSubItemList());

//            ArrayList<Event> events = buildSubItemList();
//            for (Event event : events)
//            {
//                item.AddTimeInterval(event);
//            }
//
//            List<Event> eventList = buildSubItemList();
//            item = new TimeTrackingDay(i, "ноябрь", 2019, eventList);
            itemList.add(item);
        }
        return itemList;
    }

    private ArrayList<Event> buildSubItemList() {
        ArrayList<Event> subItemList = new ArrayList<>();
        Random random = new Random();
        int randomCount = random.nextInt(7) + 3;
        for (int i=1; i < randomCount; i++) {
            Event subItem = new Event(i, 11, 2019, i, 0, i+1, 0, "Пример");
            subItemList.add(subItem);
        }
        return subItemList;
    }
}
