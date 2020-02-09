package com.wingsmight.makeday.MyDays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wingsmight.makeday.GoalNotificationService;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.Tracker.RowDayModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyDaysFragment extends Fragment
{
    TabName tabName = TabName.MY_DAYS;
    RecyclerView recyclerView;
    static ArrayList<RowDayModel> days = new ArrayList<>();
    MyDaysAdapter myDaysAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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


        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateLastDays();
                myDaysAdapter.notifyDataSetChanged();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        getActivity().registerReceiver(mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String message = intent.getStringExtra("lastDoneDeals");
            ArrayList<GoalNotificationService.GoalNotificationStruct> lastDoneDeals
                    = new Gson().fromJson(message, new TypeToken<List<GoalNotificationService.GoalNotificationStruct>>(){}.getType());

            if(lastDoneDeals != null)
            {
                for (GoalNotificationService.GoalNotificationStruct doneDeal :
                        lastDoneDeals)
                {
                    addDoneDealToDay(doneDeal.day, doneDeal.dealLabel);
                }
            }

            message = intent.getStringExtra("lastUndoneDeals");
            ArrayList<GoalNotificationService.GoalNotificationStruct> lastUndoneDeals
                    = new Gson().fromJson(message, new TypeToken<List<GoalNotificationService.GoalNotificationStruct>>(){}.getType());

            if(lastUndoneDeals != null)
            {
                for (GoalNotificationService.GoalNotificationStruct undoneDeal :
                        lastUndoneDeals)
                {
                    addUndoneDealToDay(undoneDeal.day, undoneDeal.dealLabel);
                }
            }

            message = intent.getStringExtra("lastAlmostDeals");
            ArrayList<GoalNotificationService.GoalNotificationStruct> lastAlmostDeals
                    = new Gson().fromJson(message, new TypeToken<List<GoalNotificationService.GoalNotificationStruct>>(){}.getType());

            if(lastAlmostDeals != null)
            {
                for (GoalNotificationService.GoalNotificationStruct almostDeal :
                        lastAlmostDeals)
                {
                    addAlmostDealToDay(almostDeal.day, almostDeal.dealLabel);
                }
            }

            clearLastDeals();
        }
    };

    @Override
    public void onResume()
    {
        updateLastDays();
        myDaysAdapter.notifyDataSetChanged();

        super.onResume();
    }

    private void updateLastDays()
    {
        ArrayList<GoalNotificationService.GoalNotificationStruct> lastDoneDeals = loadDoneDealToDay();
        ArrayList<GoalNotificationService.GoalNotificationStruct> lastUndoneDeals = loadUndoneDealToDay();
        ArrayList<GoalNotificationService.GoalNotificationStruct> lastAlmostDeals = loadAlmostDealToDay();

        if(lastDoneDeals != null)
        {
            for (GoalNotificationService.GoalNotificationStruct doneDeal :
                    lastDoneDeals)
            {
                addDoneDealToDay(doneDeal.day, doneDeal.dealLabel);
            }
        }

        if(lastUndoneDeals != null)
        {
            for (GoalNotificationService.GoalNotificationStruct undoneDeal :
                    lastUndoneDeals)
            {
                addUndoneDealToDay(undoneDeal.day, undoneDeal.dealLabel);
            }
        }

        if(lastAlmostDeals != null)
        {
            for (GoalNotificationService.GoalNotificationStruct almostDeal :
                    lastAlmostDeals)
            {
                addAlmostDealToDay(almostDeal.day, almostDeal.dealLabel);
            }
        }

        clearLastDeals();
    }

    private void clearLastDeals()
    {
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        editorSave.putString("lastDoneDeals", "");
        editorSave.putString("lastUndoneDeals", "");
        editorSave.putString("lastAlmostDeals", "");
        editorSave.apply();
    }

    private ArrayList<GoalNotificationService.GoalNotificationStruct> loadDoneDealToDay()
    {
        String json = loadString("lastDoneDeals", "");
        if(!json.equals(""))
        {
            try
            {
                return new Gson().fromJson(json, new TypeToken<List<GoalNotificationService.GoalNotificationStruct>>(){}.getType());
            }
            catch (Exception exception)
            {
                return new ArrayList<GoalNotificationService.GoalNotificationStruct>();
            }
        }
        else
        {
            return new ArrayList<GoalNotificationService.GoalNotificationStruct>();
        }
    }

    private ArrayList<GoalNotificationService.GoalNotificationStruct> loadUndoneDealToDay()
    {
        String json = loadString("lastUndoneDeals", "");
        if(!json.equals(""))
        {
            try
            {
                return new Gson().fromJson(json, new TypeToken<List<GoalNotificationService.GoalNotificationStruct>>(){}.getType());
            }
            catch (Exception exception)
            {
                return new ArrayList<GoalNotificationService.GoalNotificationStruct>();
            }
        }
        else
        {
            return new ArrayList<GoalNotificationService.GoalNotificationStruct>();
        }
    }

    private ArrayList<GoalNotificationService.GoalNotificationStruct> loadAlmostDealToDay()
    {
        String json = loadString("lastAlmostDeals", "");
        if(!json.equals(""))
        {
            try
            {
                return new Gson().fromJson(json, new TypeToken<List<GoalNotificationService.GoalNotificationStruct>>(){}.getType());
            }
            catch (Exception exception)
            {
                return new ArrayList<GoalNotificationService.GoalNotificationStruct>();
            }
        }
        else
        {
            return new ArrayList<GoalNotificationService.GoalNotificationStruct>();
        }
    }

    public String loadString(String key, String defaultValue)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(key, defaultValue);
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

    public static void backgrounStaticSave(){
        Thread backgroundThread = new Thread() {
            @Override
            public void run()
            {
                SaveLoad.save(TabName.MY_DAYS, days);
            }
        };
        backgroundThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        backgroundSave();
    }

    public void addUndoneDealToDay(Date day, String undoneDeal)
    {
        if(days == null)
        {
            days = new ArrayList<>();
        }

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

        if(myDaysAdapter != null)
        {
            myDaysAdapter.notifyDataSetChanged();
        }
    }

    public void addDoneDealToDay(Date day, String doneDeal)
    {
        if(days == null)
        {
            days = new ArrayList<>();
        }

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

        if(myDaysAdapter != null)
        {
            myDaysAdapter.notifyDataSetChanged();
        }
    }

    public void addAlmostDealToDay(Date day, String almostDeal)
    {
        if(days == null)
        {
            days = new ArrayList<>();
        }

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

        if(myDaysAdapter != null)
        {
            myDaysAdapter.notifyDataSetChanged();
        }
    }

    public void addUndoneDealToToday(String undoneDeal)
    {
        Date todayDate = Calendar.getInstance().getTime();

        addUndoneDealToDay(todayDate, undoneDeal);
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
}
