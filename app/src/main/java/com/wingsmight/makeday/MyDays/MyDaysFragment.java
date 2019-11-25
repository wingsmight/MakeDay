package com.wingsmight.makeday.MyDays;

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
import com.wingsmight.makeday.Tracker.RowDayModel;

import java.util.ArrayList;

public class MyDaysFragment extends Fragment
{
    TabName tabName = TabName.MY_DAYS;
    RecyclerView recyclerView;
    ArrayList<RowDayModel> days = new ArrayList<>();
    MyDaysAdapter myDaysAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_my_days, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = view.findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //FillRecyclerView();
        days = SaveLoad.load(tabName);

        myDaysAdapter = new MyDaysAdapter(view.getContext(), days);
        recyclerView.setAdapter(myDaysAdapter);
    }

    private void backgroundSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                SaveLoad.save(tabName, days);
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
