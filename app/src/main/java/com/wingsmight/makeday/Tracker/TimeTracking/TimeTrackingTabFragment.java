package com.wingsmight.makeday.Tracker.TimeTracking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.Tracker.Event;
import com.wingsmight.makeday.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class TimeTrackingTabFragment extends Fragment
{
    TabName tabName = TabName.TIME_TRACKING;
    RecyclerView recyclerView;
    ArrayList<TimeTrackingDay> timeTrackingDays = new ArrayList<>();
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

        timeTrackingDays = SaveLoad.load(tabName);//buildItemList();
        //SaveLoad.save(tabName, timeTrackingDays);
        timeTrackingTabAdapter = new TimeTrackingTabAdapter(timeTrackingDays);
        recyclerView.setAdapter(timeTrackingTabAdapter);
    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                SaveLoad.save(tabName, timeTrackingDays);
            }
        };
        backgroundThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSave();
    }

    private ArrayList<TimeTrackingDay> buildItemList() {
        ArrayList<TimeTrackingDay> itemList = new ArrayList<>();
        for (int i=1; i<10; i++) {
            TimeTrackingDay<Event> item = new TimeTrackingDay<Event>(i, 11, 2019, buildSubItemList());
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
