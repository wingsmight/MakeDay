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

    private ArrayList<TimeTrackingDay<EmotionEvent>> buildItemList() {
        ArrayList<TimeTrackingDay<EmotionEvent>> itemList = new ArrayList<>();
        for (int i=1; i<10; i++) {
            TimeTrackingDay<EmotionEvent> item = new TimeTrackingDay<EmotionEvent>(i, 12, 2019, buildSubItemList());
            itemList.add(item);
        }
        return itemList;
    }

    private ArrayList<EmotionEvent> buildSubItemList() {
        ArrayList<EmotionEvent> subItemList = new ArrayList<>();
        Random random = new Random();
        int randomCount = random.nextInt(7) + 3;
        int randomEmotion;
        for (int i=1; i < randomCount; i++) {
            randomEmotion = random.nextInt(6) - 3;
            EmotionEvent subItem = new EmotionEvent(i, 11, 2019, i, 0, i+1, 0, String.valueOf(randomEmotion));
            subItemList.add(subItem);
        }
        return subItemList;
    }
}
