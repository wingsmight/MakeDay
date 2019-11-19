package com.wingsmight.makeday;

import android.content.Context;
import android.net.Uri;
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
import java.util.Random;


public class EmotionsTabFragment extends Fragment
{
    RecyclerView recyclerView;
    ArrayList<TimeTrackingDay<EmotionEvent>> TimeTrackingDays = new ArrayList<TimeTrackingDay<EmotionEvent>>();
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

        TimeTrackingDays = buildItemList();
        emotionsTabAdapter = new EmotionsTabAdapter(TimeTrackingDays);
        recyclerView.setAdapter(emotionsTabAdapter);
    }

    private ArrayList<TimeTrackingDay<EmotionEvent>> buildItemList() {
        ArrayList<TimeTrackingDay<EmotionEvent>> itemList = new ArrayList<>();
        for (int i=1; i<10; i++) {
            TimeTrackingDay<EmotionEvent> item = new TimeTrackingDay<EmotionEvent>(i, "ноябрь", 2019, buildSubItemList());
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
