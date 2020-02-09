package com.wingsmight.makeday.Tracker.TimeTracking;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.TimePickerFragment;
import com.wingsmight.makeday.TimeTrackingService;
import com.wingsmight.makeday.Tracker.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeTrackingTabFragment extends Fragment
{
    public static int requestCode = 1111;
    private TabName tabName = TabName.TIME_TRACKING;
    private RecyclerView recyclerView;
    private static ArrayList<TimeTrackingDay> timeTrackingDays = new ArrayList<>();
    private static TimeTrackingTabAdapter timeTrackingTabAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;


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

        timeTrackingDays = SaveLoad.load(tabName);
        timeTrackingTabAdapter = new TimeTrackingTabAdapter(timeTrackingDays);
        recyclerView.setAdapter(timeTrackingTabAdapter);

        //Button setLaunchSettings
        ExtendedFloatingActionButton launchButton = view.findViewById(R.id.launchButton);
        launchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                setLaunchSettings(view);
            }
        });

        getActivity().registerReceiver(mMessageReceiver, new IntentFilter(TimeTrackingService.INTENT_FILTER));

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateLastItemsBySharedPref();
                clearLastEvents();

                timeTrackingTabAdapter.notifyDataSetChanged();

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

    }

    private void updateLastItemsByBroadcast(Intent intent)
    {
        String message = intent.getStringExtra("lastEvents");
        ArrayList<Event> lastEvents;
        if(message != "")
        {
            lastEvents = new Gson().fromJson(message, new TypeToken<ArrayList<Event>>(){}.getType());
        }
        else
        {
            lastEvents = null;
        }

        if(lastEvents != null)
        {
            addEvents(lastEvents);
        }
    }

    private void updateLastItemsBySharedPref()
    {
        ArrayList<Event> lastEvents = loadLastEvents();

        if(lastEvents != null)
        {
            addEvents(lastEvents);
        }
    }

    private ArrayList<Event> loadLastEvents()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        String jsonEvent = sharedPreferences.getString("lastEvents", "");
        if(jsonEvent == "")
        {
            return null;
        }
        else
        {
            return new Gson().fromJson(jsonEvent, new TypeToken<ArrayList<Event>>(){}.getType());
        }
    }

    private void clearLastEvents()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        editorSave.putString("lastEvents", "");
        editorSave.apply();

        lastEvents = new ArrayList<>();
    }

    private ArrayList<Event> lastEvents = new ArrayList<>();

    private TextView startTrackingTime;
    private TextView finishTrackingTime;
    private int startHour, startMinutes;
    private int finishHour, finishMinutes;

    private void setLaunchSettings(View view)
    {
        //Show popup
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.launch_tracking_dialog, null);
        builder.setView(dialogView);
        final AlertDialog popupDialog = builder.create();
        popupDialog.show();

        Button launchButton = dialogView.findViewById(R.id.launchButton);
        launchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if((startHour >= finishHour) && ((startHour != finishHour) || (startMinutes >= finishMinutes)))
                {
                    Toast.makeText(getContext(), "Конец учета должен быть позже начала!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    long trackingInterval = loadTrackingInterval();

                    TimeTrackingService.pullNotification(getContext(), trackingInterval, Calendar.getInstance().getTimeInMillis(), null);

                    Toast.makeText(getContext(), "Уведомления настроены", Toast.LENGTH_SHORT).show();
                    popupDialog.dismiss();
                }
            }
        });

        startTrackingTime = dialogView.findViewById(R.id.startTrackingTime);

        String startTime = startTrackingTime.getText().toString();
        startHour = Integer.valueOf(startTime.substring(0, startTime.indexOf(':')));
        startMinutes = Integer.valueOf(startTime.substring(startTime.indexOf(':') + 1));

        String startTrackingTimeString = SaveLoad.loadString(SaveLoad.defaultSavingPath, "startTrackingTime", "8:00");
        startTrackingTime.setText(startTrackingTimeString);
        startTrackingTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment timePickerDialog = new TimePickerFragment();
                timePickerDialog.setTargetFragment(TimeTrackingTabFragment.this, requestCode);
                timePickerDialog.addOnTimeSetListener(new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        startHour = hourOfDay;
                        startMinutes = minute;

                        String startTrackingTimeString = hourOfDay + ":" + toHourFormat(minute);
                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "startTrackingTime", startTrackingTimeString);
                        startTrackingTime.setText(startTrackingTimeString);

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editorSave = sharedPreferences.edit();

                        Date startDate = new Date(Calendar.getInstance().getTimeInMillis());
                        startDate.setHours(hourOfDay);
                        startDate.setMinutes(minute);
                        editorSave.putLong("startMillisec", startDate.getTime());
                        editorSave.apply();
                    }
                });
                timePickerDialog.show(getFragmentManager(), "time picker");
            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editorSave = sharedPreferences.edit();

        Date startDate = new Date(Calendar.getInstance().getTimeInMillis());
        startDate.setHours(8);
        startDate.setMinutes(0);
        editorSave.putLong("startMillisec", startDate.getTime());


        finishTrackingTime = dialogView.findViewById(R.id.finishTrackingTime);

        String finishTime = finishTrackingTime.getText().toString();
        finishHour = Integer.valueOf(finishTime.substring(0, finishTime.indexOf(':')));
        finishMinutes = Integer.valueOf(finishTime.substring(finishTime.indexOf(':') + 1));

        String FinishTrackingTimeString = SaveLoad.loadString(SaveLoad.defaultSavingPath, "finishTrackingTime", "22:00");
        finishTrackingTime.setText(FinishTrackingTimeString);
        finishTrackingTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TimePickerFragment timePickerDialog = new TimePickerFragment();
                timePickerDialog.setTargetFragment(TimeTrackingTabFragment.this, requestCode);
                timePickerDialog.addOnTimeSetListener(new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        finishHour = hourOfDay;
                        finishMinutes = minute;

                        String FinishTrackingTimeString = hourOfDay + ":" + toHourFormat(minute);
                        SaveLoad.saveString(SaveLoad.defaultSavingPath, "finishTrackingTime", FinishTrackingTimeString);
                        finishTrackingTime.setText(FinishTrackingTimeString);

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editorSave = sharedPreferences.edit();

                        Date finishDate = new Date(Calendar.getInstance().getTimeInMillis());
                        finishDate.setHours(hourOfDay);
                        finishDate.setMinutes(minute);
                        editorSave.putLong("finishMillisec", finishDate.getTime());
                        editorSave.apply();
                    }
                });
                timePickerDialog.show(getFragmentManager(), "time picker");
            }
        });

        Date finishDate = new Date(Calendar.getInstance().getTimeInMillis());
        finishDate.setHours(22);
        finishDate.setMinutes(0);
        editorSave.putLong("finishMillisec", finishDate.getTime());

        editorSave.apply();

        Spinner spinner = dialogView.findViewById(R.id.intervalRequest);

        List<IntervalTrackingTime> userList = new ArrayList<>();
        userList.add(new IntervalTrackingTime(15));
        userList.add(new IntervalTrackingTime(30));
        userList.add(new IntervalTrackingTime(60));
        userList.add(new IntervalTrackingTime(90));

        saveTrackingInterval(userList.get(0).getMinute() * 60 * 1000);

        ArrayAdapter<IntervalTrackingTime> adapter = new ArrayAdapter<>(getContext(), R.layout.interval_request_spinner_item, userList);
        adapter.setDropDownViewResource(R.layout.interval_request_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                IntervalTrackingTime interval = (IntervalTrackingTime)parent.getSelectedItem();
                saveTrackingInterval(interval.getMinute() * 60 * 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private long loadTrackingInterval()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getLong("trackingInterval", 1000 * 60 * 15);
    }

    private void saveTrackingInterval(long trackingInterval)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("trackingInterval", trackingInterval);

        editor.apply();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            updateLastItemsByBroadcast(intent);

            clearLastEvents();

            timeTrackingTabAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onResume()
    {
        updateLastItemsBySharedPref();
        clearLastEvents();

        timeTrackingTabAdapter.notifyDataSetChanged();

        super.onResume();
    }

    public void addEvents(ArrayList<Event> events)
    {
        if(events == null || events.size() == 0)
        {
            return;
        }

        for (Event event : events)
        {
            addEvent(event);
        }
    }


    public void addEvent(Event event)
    {
        int size = timeTrackingDays.size();
        if(size != 0)
        {
            if(event.getDay() > timeTrackingDays.get(0).getDay())
            {
                ArrayList<Event> newEventsArray = new ArrayList();
                newEventsArray.add(event);
                timeTrackingDays.add(0, new TimeTrackingDay(event.getDay(), event.getDayOfWeek(), event.getMonth(), event.getYear(), newEventsArray));
            }
            else
            {
                timeTrackingDays.get(0).AddTimeInterval(event);
            }
        }
        else
        {
            ArrayList<Event> newEventsArray = new ArrayList();
            newEventsArray.add(event);
            timeTrackingDays.add(new TimeTrackingDay(event.getDay(), event.getDayOfWeek(), event.getMonth(), event.getYear(), newEventsArray));
        }

        timeTrackingTabAdapter.notifyDataSetChanged();
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

    private String toHourFormat(int time)
    {
        if(time >= 10)
        {
            return String.valueOf(time);
        }
        else
        {
            return "0" + String.valueOf(time);
        }
    }
}
