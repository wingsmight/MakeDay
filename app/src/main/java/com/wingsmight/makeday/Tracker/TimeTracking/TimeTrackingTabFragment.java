package com.wingsmight.makeday.Tracker.TimeTracking;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.R;

import java.util.ArrayList;

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
}
