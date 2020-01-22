package com.wingsmight.makeday.Tracker.TimeTracking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.wingsmight.makeday.SavingSystem.SaveLoad;
import com.wingsmight.makeday.TabName;
import com.wingsmight.makeday.R;
import com.wingsmight.makeday.TimePickerFragment;
import com.wingsmight.makeday.Tracker.Event;
import com.wingsmight.makeday.Tracker.ReceiveNotifyAnswer;

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
    }

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
                    pullNotification();
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
                    }
                });
                timePickerDialog.show(getFragmentManager(), "time picker");
            }
        });

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
                    }
                });
                timePickerDialog.show(getFragmentManager(), "time picker");
            }
        });

        Spinner spinner = dialogView.findViewById(R.id.intervalRequest);

        List<IntervalTrackingTime> userList = new ArrayList<>();
        userList.add(new IntervalTrackingTime(15));
        userList.add(new IntervalTrackingTime(30));
        userList.add(new IntervalTrackingTime(60));
        userList.add(new IntervalTrackingTime(90));

        ArrayAdapter<IntervalTrackingTime> adapter = new ArrayAdapter<>(getContext(), R.layout.interval_request_spinner_item, userList);
        adapter.setDropDownViewResource(R.layout.interval_request_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                IntervalTrackingTime interval = (IntervalTrackingTime)parent.getSelectedItem();
                displayUserData(interval);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static IntervalTrackingTime intervalTrackingTime;
    private void displayUserData(IntervalTrackingTime interval)
    {
        intervalTrackingTime = interval;
    }

    AlertDialog alert;
    Handler notifyHandler;
    public void pullNotification()
    {
        Date startDate = Calendar.getInstance().getTime();
        startDate.setHours(startHour);
        startDate.setMinutes(startMinutes);

        Date finishDate = Calendar.getInstance().getTime();
        finishDate.setHours(finishHour);
        finishDate.setMinutes(finishMinutes);


        int timeDiff = intervalTrackingTime.getMinute();
        long millisecondsDiff = timeDiff * 60000;

        Date beforeDate = Calendar.getInstance().getTime();
        int beforeHour = beforeDate.getHours();
        int beforeMinutes = beforeDate.getMinutes();;

        Date afterDate = new Date(Calendar.getInstance().getTimeInMillis() + millisecondsDiff);
        int afterHour = afterDate.getHours();
        int afterMinutes = afterDate.getMinutes();

        long currentMillisTime = Calendar.getInstance().getTimeInMillis();
        long finishMillisTime = finishDate.getTime();

        long millisTimeToFinish = finishMillisTime - currentMillisTime;
        if (millisTimeToFinish < 1000)
        {
            long nextDayStartTime = startDate.getTime() + 86400000;
            millisecondsDiff = nextDayStartTime - Calendar.getInstance().getTimeInMillis();//Notify at next day
        }
        else if(millisTimeToFinish <= millisecondsDiff)
        {
            if(millisTimeToFinish >= millisecondsDiff / 2)
            {
                millisecondsDiff = millisTimeToFinish;
            }
            else
            {
                long nextDayStartTime = startDate.getTime() + 86400000;
                millisecondsDiff = nextDayStartTime - Calendar.getInstance().getTimeInMillis();//Notify at next day
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView = createDialogView(beforeHour, beforeMinutes, afterHour, afterMinutes);

        builder.setView(dialogView);
        alert = builder.create();
        alert.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.getWindow().setGravity(Gravity.TOP);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alert.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        notifyHandler = new Handler();
        notifyHandler.removeCallbacksAndMessages(null);
        notifyHandler.postDelayed(new Runnable() {
            public void run() {
                alert.show();
            }
        }, millisecondsDiff);
    }

    private View createDialogView(int beforeHours, int beforeMinutes, int afterHours, int afterMinutes)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.notification_expanded, null);
        ((TextView)(dialogView.findViewById(R.id.WhatWasYouDoing))).setText("Чем занимались с " + beforeHours + ":" + minutesToFullFormat(Integer.toString(beforeMinutes))
                + " до " + afterHours + ":" + minutesToFullFormat(Integer.toString(afterMinutes)) + "?");

        Button[] notifyButtons = new Button[6];
        notifyButtons[0] = dialogView.findViewById(R.id.notifyButton1);
        notifyButtons[1] = dialogView.findViewById(R.id.notifyButton2);
        notifyButtons[2] = dialogView.findViewById(R.id.notifyButton3);
        notifyButtons[3] = dialogView.findViewById(R.id.notifyButton4);
        notifyButtons[4] = dialogView.findViewById(R.id.notifyButton5);
        notifyButtons[5] = dialogView.findViewById(R.id.notifyButton6);

        for (Button notifyButton : notifyButtons)
        {
            notifyButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    clickOnEventButton((Button)v);
                }
            });
        }

        ImageButton sendButton = dialogView.findViewById(R.id.sendEventButton);
        final EditText sendEventEditText = dialogView.findViewById(R.id.sendEventEditText);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clickOnEventSendButton(sendEventEditText.getText().toString());
            }
        });

        return dialogView;
    }

    public void clickOnEventButton(Button buttonView)
    {
        alert.dismiss();

        String eventText = buttonView.getText().toString();

        Date date = Calendar.getInstance().getTime();

        int timeDiff = TimeTrackingTabFragment.intervalTrackingTime.getMinute();
        long millisecondsDiff = timeDiff * 60000;
        long beforeDateTime = date.getTime() - millisecondsDiff;
        Date beforeDate = new Date(beforeDateTime);
        int beforeHours = beforeDate.getHours();
        int beforeMinutes = beforeDate.getMinutes();

        TimeTrackingTabFragment.addEvent(new Event(date, beforeHours, beforeMinutes, eventText));

        pullNotification();
    }

    public void clickOnEventSendButton(String eventText)
    {
        alert.dismiss();

        Date date = Calendar.getInstance().getTime();

        int timeDiff = TimeTrackingTabFragment.intervalTrackingTime.getMinute();
        long millisecondsDiff = timeDiff * 60000;
        long beforeDateTime = date.getTime() - millisecondsDiff;
        Date beforeDate = new Date(beforeDateTime);
        int beforeHours = beforeDate.getHours();
        int beforeMinutes = beforeDate.getMinutes();

        TimeTrackingTabFragment.addEvent(new Event(date, beforeHours, beforeMinutes, eventText));

        pullNotification();
    }


    private String minutesToFullFormat(String shortMinutes)
    {
        if(shortMinutes.length() == 1)
        {
            return "0" + shortMinutes;
        }
        else
        {
            return shortMinutes;
        }
    }

    private static NotificationManagerCompat notificationManager;
    public static void cancelNotification(int id)
    {
        if(notificationManager != null)
        {
            notificationManager.cancel(id);
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, ReceiveNotifyAnswer.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public static void addEvent(Event event)
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
