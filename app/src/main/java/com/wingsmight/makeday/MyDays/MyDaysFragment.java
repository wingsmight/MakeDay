package com.wingsmight.makeday.MyDays;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.Tracker.RowDayModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public void backgroundSave(){
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

    public void addUndoneDealToToday(String undoneDeal)
    {
        Date todayDate = Calendar.getInstance().getTime();

        addUndoneDealToDay(todayDate, undoneDeal);
    }

    public void addUndoneDealToDay(Date day, String undoneDeal)
    {
        if(days.size() != 0)
        {
            if(!days.get(0).equalsToDate(day))
            {
                days.add(0, new RowDayModel(day.getDate(), day.getDay(), day.getMonth(), day.getYear() + 1900,
                        null, null, null));
            }

            days.get(0).addUndoneDeals(undoneDeal);
        }
        else
        {
            days.add(0, new RowDayModel(day.getDate(), day.getDay(), day.getMonth(), day.getYear() + 1900,
                    null, null, null));

            days.get(0).addUndoneDeals(undoneDeal);
        }

        myDaysAdapter.notifyDataSetChanged();
    }

    public void addDoneDealToDay(Date day, String doneDeal)
    {
        if(days.size() != 0)
        {
            if(!days.get(0).equalsToDate(day))
            {
                days.add(0, new RowDayModel(day.getDate(), day.getDay(),day.getMonth(), day.getYear() + 1900,
                        null, null, null));
            }

            days.get(0).addDoneDeals(doneDeal);
        }
        else
        {
            days.add(0, new RowDayModel(day.getDate(), day.getDay(), day.getMonth(), day.getYear() + 1900,
                    null, null, null));

            days.get(0).addDoneDeals(doneDeal);
        }

        myDaysAdapter.notifyDataSetChanged();
    }

    public void addDoneDealToToday(String doneDeal)
    {
        Date todayDate = Calendar.getInstance().getTime();

        addDoneDealToDay(todayDate, doneDeal);
    }

    public void addAlmostDealToToday(String almostDeal)
    {
        Date todayDate = Calendar.getInstance().getTime();

        addAlmostDealToDay(todayDate, almostDeal);
    }

    public void addAlmostDealToDay(Date day, String almostDeal)
    {
        if(days.size() != 0)
        {
            if(!days.get(0).equalsToDate(day))
            {
                days.add(0, new RowDayModel(day.getDate(), day.getDay(), day.getMonth(), day.getYear() + 1900,
                        null, null, null));
            }

            days.get(0).addAlmostDeals(almostDeal);
        }
        else
        {
            days.add(0, new RowDayModel(day.getDate(), day.getDay(), day.getMonth(), day.getYear() + 1900,
                    null, null, null));

            days.get(0).addAlmostDeals(almostDeal);
        }

        myDaysAdapter.notifyDataSetChanged();
    }
}
