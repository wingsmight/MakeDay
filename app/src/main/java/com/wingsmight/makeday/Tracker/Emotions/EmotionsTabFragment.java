package com.wingsmight.makeday.Tracker.Emotions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.Tracker.TimeTracking.TimeTrackingDay;

import java.util.ArrayList;
import java.util.Random;


public class EmotionsTabFragment extends Fragment
{
    TabName tabName = TabName.EMOTIONS;
    RecyclerView recyclerView;
    ArrayList<TimeTrackingDay<EmotionEvent>> timeTrackingDays = new ArrayList<TimeTrackingDay<EmotionEvent>>();
    EmotionsTabAdapter emotionsTabAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_emotions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.timeTrackerRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        timeTrackingDays = SaveLoad.load(tabName);// buildItemList();
        emotionsTabAdapter = new EmotionsTabAdapter(timeTrackingDays);
        recyclerView.setAdapter(emotionsTabAdapter);
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
